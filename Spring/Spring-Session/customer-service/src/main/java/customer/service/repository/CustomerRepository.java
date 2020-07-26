package customer.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import customer.service.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
