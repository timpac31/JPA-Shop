package io.timpac.shop.order.repository;

import java.util.List;

import io.timpac.shop.order.domain.Order;
import io.timpac.shop.order.dto.OrderSearch;

public interface OrderRepositoryCustom {
	List<Order> search(OrderSearch orderSearch);
}
