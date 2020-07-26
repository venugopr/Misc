package spring.gateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.gateway.dto.Customer;
import spring.gateway.service.CustomerService;

@RestController
public class CustomerServiceController {
	
	@Autowired
	private CustomerService customerService;

	@GetMapping(value = "/findCustomer")
	public Customer findCustomer(@RequestParam Long id) {
		Customer record = customerService.findCustomer(id);
		return record;
	}

	@GetMapping(value = "/findAll")
	public List<Customer> findAll() {
		List<Customer> customers = customerService.findAll();
		return customers;
	}
}
