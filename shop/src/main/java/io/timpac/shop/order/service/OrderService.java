package io.timpac.shop.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.timpac.shop.Item.domain.Item;
import io.timpac.shop.Item.repository.ItemRepository;
import io.timpac.shop.order.domain.Customer;
import io.timpac.shop.order.domain.Delivery;
import io.timpac.shop.order.domain.Order;
import io.timpac.shop.order.domain.OrderItem;
import io.timpac.shop.order.dto.OrderSearch;
import io.timpac.shop.order.repository.CustomerRepository;
import io.timpac.shop.order.repository.OrderRepository;

@Service
@Transactional
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	public Long order(Long customerId, Long itemId, int count) {
		Customer customer = customerRepository.findById(customerId).orElseThrow();
		Item item = itemRepository.findById(itemId).orElseThrow();
		
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		Order order = Order.createOrder(customer, new Delivery(customer.getAddress()), orderItem);
		
		orderRepository.save(order);
		
		return order.getId();
	}
	
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow();
		order.cancel();
	}
	
	public Order findOne(Long orderId) {
		return orderRepository.findById(orderId).orElseGet(Order::new);
	}
	
	public List<Order> find(OrderSearch orderSearch) {
		return orderRepository.findAll(orderSearch.toSpecification());
		//return orderRepository.search(orderSearch);
	}
	
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

}
