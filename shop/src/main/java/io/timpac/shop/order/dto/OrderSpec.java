package io.timpac.shop.order.dto;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import io.timpac.shop.order.domain.Customer;
import io.timpac.shop.order.domain.Order;
import io.timpac.shop.order.domain.OrderStatus;

public class OrderSpec {
	public static Specification<Order> customerNameLike(final String memberName) {
		return new Specification<Order>() {
			public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if(!StringUtils.hasText(memberName)) return null;
				
				Join<Order, Customer> c = root.join("customer", JoinType.INNER);
				
				return builder.like(c.<String>get("name"), "%"+memberName+"%");
			}
		};
	}
	
	public static Specification<Order> orderStatusEq(final OrderStatus orderStatus) {
		return new Specification<Order>() {
			public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if(orderStatus == null) return null;
				
				return builder.equal(root.get("status"), orderStatus);
			}
		};
	}
}
