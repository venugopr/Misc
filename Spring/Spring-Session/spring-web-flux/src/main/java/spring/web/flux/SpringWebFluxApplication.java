package spring.web.flux;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SpringWebFluxApplication {

	@Value("${gateway.url}")
	private String gatewayUrl;

	public static void main(String[] args) {
		SpringApplication.run(SpringWebFluxApplication.class, args);
	}

	@Bean
	public WebClient webClient() {
		return WebClient.builder().baseUrl(this.gatewayUrl).build();
	}
}
