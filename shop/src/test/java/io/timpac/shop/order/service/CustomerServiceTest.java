package io.timpac.shop.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.timpac.shop.common.domain.Address;
import io.timpac.shop.order.domain.Customer;

@SpringBootTest
@Transactional
public class CustomerServiceTest {
	@Autowired
	private CustomerService customerService;
	
	@Test
	public void join() {
		Customer customer = new Customer();
		customer.setName("조영덕");
		
		Long customerId = customerService.join(customer);
		
		assertEquals(customer, customerService.findCustomer(customerId));
	}
	
	@Test
	public void 중복회원_예외() {
		Customer customer1 = new Customer();
		customer1.setName("조영덕");
		customer1.setAddress(new Address("123456", "산성대로13", "1층"));
		Customer customer2 = new Customer();
		customer2.setName("조영덕");
		customer2.setAddress(new Address("123456", "산성대로13", "1층"));
		
		customerService.join(customer1);
		
		assertThrows(IllegalStateException.class, () -> {
			customerService.join(customer2);
		});
	}
	
	
}
