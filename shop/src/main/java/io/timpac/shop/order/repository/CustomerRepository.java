package io.timpac.shop.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.timpac.shop.common.domain.Address;
import io.timpac.shop.order.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	List<Customer> findByName(String name);
	List<Customer> findByNameAndAddress(String name, Address address);
}
