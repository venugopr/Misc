package spring.gateway.controller;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import spring.gateway.reqres.LoginRequest;
import spring.gateway.reqres.LoginResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerServiceControllerTest {

	private String token;

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Before
	public void setup() {

		LoginRequest loginRequest = new LoginRequest("user1", "user1");
		try {
			ResponseEntity<LoginResponse> response = restTemplate.postForEntity("http://localhost:8080/authenticate",
					loginRequest, LoginResponse.class);
			LoginResponse loginResponse = response.getBody();
			token = loginResponse.getJwt();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void findAllCustomersTest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/findAll", HttpMethod.GET, new HttpEntity<>(headers), String.class);
		
		System.out.println(response.getBody());
		assertThat(response.getStatusCode().equals(HttpStatus.OK));
	}


}
