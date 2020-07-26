package spring.web.flux.controller;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import reactor.core.publisher.Flux;
import spring.web.flux.dto.Customer;
import spring.web.flux.service.CustomerService;
import spring.web.flux.util.JWTUtils;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerServcie;

	@Autowired
	private JWTUtils jwtUtils;

	@GetMapping("/findAllCustomers")
	public String findAll(Model model, Authentication authentication) {
		String token = jwtUtils.generateJwtToken(authentication);
		List<Customer> customers = this.customerServcie.findAllCustomers(token);
		model.addAttribute("customers", customers);
		return "customers";
	}

	@GetMapping("/findAllCustomersReactive")
	public String findAllReactive(Model model, Authentication authentication) {
		String token = jwtUtils.generateJwtToken(authentication);
		List<Customer> customers = this.customerServcie.findAllCustomers(token);
//		Flux<Customer> data = Flux.fromIterable(customers);
		// Introducing a delay of 200 ms to show the streaming behaviour
		IReactiveDataDriverContextVariable data = new ReactiveDataDriverContextVariable(Flux.fromIterable(customers).delayElements(Duration.ofMillis(200)));
		model.addAttribute("customers", data);

		return "customers-reactive";
	}
}
