package spring.discovery.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class SpringDiscoveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDiscoveryServerApplication.class, args);
	}

}
