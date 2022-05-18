package io.timpac.shop.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import io.timpac.shop.common.domain.Address;

@Entity
public class Customer {
	@Id @GeneratedValue
	@Column(name = "CUSTOMER_ID")
	private Long id;
	
	private String name;

	@Embedded
	private Address address;
	
	@OneToMany(mappedBy = "customer")
	private List<Order> orders = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Order> getOrders() {
		return orders;
	}

	protected void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Customer)) return false;
		Customer other = (Customer) obj;
		if(this.id.equals(other.id)) return true;
		return false;
	}
}
