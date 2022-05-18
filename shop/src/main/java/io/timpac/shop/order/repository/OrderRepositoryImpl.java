package io.timpac.shop.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import com.querydsl.jpa.JPQLQuery;

import io.timpac.shop.order.domain.Order;
import io.timpac.shop.order.domain.QCustomer;
import io.timpac.shop.order.domain.QOrder;
import io.timpac.shop.order.dto.OrderSearch;

public class OrderRepositoryImpl extends QuerydslRepositorySupport implements OrderRepositoryCustom {

	public OrderRepositoryImpl() {
		super(Order.class);
	}

	@Override
	public List<Order> search(OrderSearch orderSearch) {
		QOrder order = QOrder.order;
		QCustomer customer = QCustomer.customer;
		
		JPQLQuery<Order> query = from(order);
		
		if(StringUtils.hasText(orderSearch.getCustomerName())) {
			query.leftJoin(order.customer(), customer)
				.where(customer.name.contains(orderSearch.getCustomerName()));
		}
		
		if(orderSearch.getOrderStatus() != null) {
			query.where(order.status.eq(orderSearch.getOrderStatus()));
		}
		
		return query.fetch();
	}
}
