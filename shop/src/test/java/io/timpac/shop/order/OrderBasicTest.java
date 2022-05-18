package io.timpac.shop.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.timpac.shop.Item.domain.Item;
import io.timpac.shop.Item.domain.Movie;
import io.timpac.shop.order.domain.Order;
import io.timpac.shop.order.domain.OrderItem;
import io.timpac.shop.order.domain.OrderStatus;

@DataJpaTest
public class OrderBasicTest {
	@PersistenceContext
	private EntityManager em;
	
	@Test
	public void save() {
		Order order = new Order();
		order.setOrderDate(new Date());
		order.setStatus(OrderStatus.ORDER);
		
		Item itemA = new Movie();
		itemA.setName("상품1");
		em.persist(itemA);
		
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(itemA);
		orderItem.setOrder(order);
		orderItem.setOrderPrice(1000);
		orderItem.setCount(2);
		em.persist(orderItem);

		check();
	}
	
	private void check() {
		OrderItem orderItem = em.find(OrderItem.class, 2L);
		Order order = orderItem.getOrder();
		Item item = orderItem.getItem();
		
		assertEquals(OrderStatus.ORDER, order.getStatus());
		assertEquals("상품1", item.getName());
		assertEquals(2, orderItem.getCount());
	}
}
