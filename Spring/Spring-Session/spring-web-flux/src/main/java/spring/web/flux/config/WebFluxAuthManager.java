package spring.web.flux.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
import spring.web.flux.config.reqres.LoginRequest;
import spring.web.flux.config.reqres.LoginResponse;

@Component
public class WebFluxAuthManager implements ReactiveAuthenticationManager {

	@Value("${gateway.url}")
	private String gatewayUrl;

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		// return is already authenticated
		if (authentication.isAuthenticated()) {
			return Mono.just(authentication);
		}
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		LoginRequest loginRequest = new LoginRequest(username, password);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// Using HttpClient to call the authenticate which is blocking call
			
			HttpPost httpPost = new HttpPost(this.gatewayUrl + "/authenticate");
			httpPost.setHeader("Content-type", "application/json");
			String jsonReq = converObjectToJson(loginRequest);
			StringEntity requestEntity = new StringEntity(jsonReq);
			httpPost.setEntity(requestEntity);

			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
				HttpEntity entity = httpResponse.getEntity();
				Header encodingHeader = entity.getContentEncoding();

				// you need to know the encoding to parse correctly
				Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8
						: Charsets.toCharset(encodingHeader.getValue());
				// use org.apache.http.util.EntityUtils to read json as string
				String jsonRes = EntityUtils.toString(entity, encoding);
				LoginResponse loginResponse = converJsonToResponse(jsonRes);
				Collection<? extends GrantedAuthority> authorities = loginResponse.getRoles().stream()
						.map(item -> new SimpleGrantedAuthority(item)).collect(Collectors.toList());
				return Mono.just(new UsernamePasswordAuthenticationToken(username, password, authorities));
			} else {
				throw new BadCredentialsException("Authentication Failed!!!");
			}

		} catch (RestClientException | ParseException | IOException e) {
			throw new BadCredentialsException("Authentication Failed!!!", e);
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static LoginResponse converJsonToResponse(final String response)
			throws JsonMappingException, JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		LoginResponse loginResponse = mapper.readValue(response, LoginResponse.class);
		return loginResponse;
	}

	private static String converObjectToJson(final Object request) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(request);
		return json;
	}
}
