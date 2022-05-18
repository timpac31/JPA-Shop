package io.timpac.shop.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.timpac.shop.order.domain.Customer;
import io.timpac.shop.order.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	public Long join(Customer customer) {
		validateDuplicateMember(customer);
		customerRepository.save(customer);
		return customer.getId();
	}

	private void validateDuplicateMember(Customer customer) {
		List<Customer> findedMembers = customerRepository.findByNameAndAddress(customer.getName(), customer.getAddress());
		if(!findedMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}
	
	public Customer findCustomer(Long id) {
		return customerRepository.findById(id).orElse(new Customer());
	}
	
	public List<Customer> findCustomers() {
		return customerRepository.findAll();
	}
	
	public Long remove(Customer customer) {
		Long customerID = customer.getId();
		customerRepository.delete(customer);
		return customerID;
	}
}
