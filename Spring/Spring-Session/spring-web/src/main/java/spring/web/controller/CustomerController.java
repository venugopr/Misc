package spring.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import spring.web.dto.Customer;
import spring.web.service.CustomerService;
import spring.web.util.JWTUtils;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService customerServcie;
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@GetMapping("/findAllCustomers")
	public String findAll(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String token = jwtUtils.generateJwtToken(authentication);
		List<Customer> customers = this.customerServcie.findAll(token);
		model.addAttribute("customers", customers);
		return "customers";
	}
}
