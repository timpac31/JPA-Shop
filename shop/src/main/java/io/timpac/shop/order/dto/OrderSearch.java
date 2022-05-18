package io.timpac.shop.order.dto;

import org.springframework.data.jpa.domain.Specification;

import io.timpac.shop.order.domain.Order;
import io.timpac.shop.order.domain.OrderStatus;

public class OrderSearch {
	private String customerName;
	private OrderStatus orderStatus;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public Specification<Order> toSpecification() {
		return Specification.where(OrderSpec.customerNameLike(customerName))
							.and(OrderSpec.orderStatusEq(orderStatus));
	}
}
