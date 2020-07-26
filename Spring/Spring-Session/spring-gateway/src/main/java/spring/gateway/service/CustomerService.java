package spring.gateway.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spring.gateway.dto.Customer;

@FeignClient("customer-service")
public interface CustomerService {

	@GetMapping(value = "/findAll")
	public List<Customer> findAll();

	@GetMapping(value = "/findCustomer")
	public Customer findCustomer(@RequestParam Long id);

}
