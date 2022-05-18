package io.timpac.shop.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.timpac.shop.order.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, 
			JpaSpecificationExecutor<Order>, OrderRepositoryCustom {
}
