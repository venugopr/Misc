package customer.service.controller;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import customer.service.entity.Customer;
import customer.service.repository.CustomerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
//@RequestMapping("/customer-service")
public class CustomerController {

	@Autowired
	CustomerRepository customerRepository;

	@GetMapping(value = "/data")
	public Mono<String> getData(ServerHttpRequest request, ServerHttpResponse response) {

		String path = request.getURI().getPath();
		Mono<String> data = Mono.just("Invoking " + path + " !!");
		return data;
	}

	@GetMapping(value = "/findCustomer")
	public Customer findCustomer(@RequestParam Long id) {
		Optional<Customer> record = customerRepository.findById(id);
		Customer data = record.get();
		return data;
	}

	@GetMapping(value = "/findAll")
	public List<Customer> findAll() {
		List<Customer> customers = customerRepository.findAll();
		return customers;
	}

	@GetMapping(value = "/findCustomerReactive")
	public Mono<Customer> findCustomerReactive(@RequestParam Long id) {
		Optional<Customer> record = customerRepository.findById(id);
		Mono<Customer> data = Mono.justOrEmpty(record);
		return data;
	}

	@GetMapping(value = "/findAllReactive")
	public Flux<Customer> findAllReactive() {
		List<Customer> customers = customerRepository.findAll();
		// Sending the events with 10 millisec delay
		Flux<Customer> data = Flux.fromIterable(customers).delayElements(Duration.ofMillis(10));

		return data;
	}
}
