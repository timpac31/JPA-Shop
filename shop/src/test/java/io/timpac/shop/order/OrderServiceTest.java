package io.timpac.shop.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.timpac.shop.Item.domain.Book;
import io.timpac.shop.Item.domain.NotEnoughStockException;
import io.timpac.shop.common.domain.Address;
import io.timpac.shop.order.domain.Customer;
import io.timpac.shop.order.domain.Order;
import io.timpac.shop.order.domain.OrderStatus;
import io.timpac.shop.order.service.OrderService;

@SpringBootTest
@Transactional
public class OrderServiceTest {
	@PersistenceContext
	private EntityManager em;
	
	@Autowired private OrderService orderService;

	@Test
	public void order() {
		Customer customer = createCustomer();
		Book book = createBook("JPA", 20000, 10);
		int orderCount = 2;
		
		Long orderId = orderService.order(customer.getId(), book.getId(), orderCount);
		
		Order order = orderService.findOne(orderId);
		
		assertEquals(OrderStatus.ORDER, order.getStatus(), "상품주문상태는 ORDER");
		assertEquals(20000 * 2, order.calcTotalPrice(), "주문가격은 가격*수량이다.");
		assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야한다.");
	}
	
	@Test
	public void 주문재고수량초과() {
		Customer customer = createCustomer();
		Book book = createBook("JPA", 20000, 10);
		int orderCount = 11;
		
		assertThrows(NotEnoughStockException.class, () -> {
			orderService.order(customer.getId(), book.getId(), orderCount);
		});
	}
	
	@Test
	public void 주문자없으면오류() {
		Long notExistCustomerId = 0L;
		Book book = createBook("JPA", 20000, 10);
		
		assertThrows(NoSuchElementException.class, () -> {
			orderService.order(notExistCustomerId, book.getId(), 1);
		});
	}
	
	@Test
	public void 주문취소() {
		Customer customer = createCustomer();
		Book book = createBook("JPA", 20000, 10);
		int orderCount = 2;
		
		Long orderId = orderService.order(customer.getId(), book.getId(), orderCount);
		orderService.cancelOrder(orderId);
		
		Order order = orderService.findOne(orderId);
		
		assertEquals(OrderStatus.CANCEL, order.getStatus());
		assertEquals(10, book.getStockQuantity());
	}
	
	private Customer createCustomer() {
		Customer customer = new Customer();
		customer.setName("조영덕");
		customer.setAddress(new Address("123456", "산성대로 157번길", "13-7"));
		em.persist(customer);
		return customer;
	}
	
	private Book createBook(String name, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		em.persist(book);
		return book;
	}
}
