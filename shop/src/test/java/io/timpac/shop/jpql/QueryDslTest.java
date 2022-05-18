package io.timpac.shop.jpql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;

import io.timpac.shop.Item.domain.Book;
import io.timpac.shop.Item.domain.Item;
import io.timpac.shop.Item.domain.Movie;
import io.timpac.shop.Item.domain.QItem;
import io.timpac.shop.member.domain.Member;
import io.timpac.shop.member.domain.QMember;
import io.timpac.shop.member.dto.MemberDTO;
import io.timpac.shop.order.domain.Order;
import io.timpac.shop.order.domain.QCustomer;
import io.timpac.shop.order.domain.QOrder;
import io.timpac.shop.order.domain.QOrderItem;

@DataJpaTest
public class QueryDslTest {
	@PersistenceContext
	private EntityManager em;
	
	public void basic() {
		JPAQuery<Member> query = new JPAQuery<>(em);
		QMember m = new QMember("m");
		
		List<Member> members = 
			query.select(m)
				.where(m.username.eq("회원1"))
				.orderBy(m.username.desc())
				.fetch();
	}
	
	@Test
	public void search() {
		insertItems();
		
		JPAQuery<Item> query = new JPAQuery<>(em);
		QItem item = QItem.item;
		
		query.select(item)
			.from(item)
			.where(item.name.eq("JPA 프로그래밍").and(item.price.gt(20000)));
		
		List<Item> resultList = query.fetch();
		Book book = (Book) resultList.get(0);
	
		assertEquals(28000, book.getPrice());
		assertEquals("이영한", book.getAuthor());
	}
	
	public void paging() {
		insertItems();
		JPAQuery<Item> query = new JPAQuery<>(em);
		QItem item = QItem.item;
		
		query.from(item)
			.where(item.price.gt(20000))
			.orderBy(item.price.desc(), item.stockQuantity.asc())
			.offset(10).limit(20)
			.fetch();
	}
	
	public void join() {
		JPAQuery<Order> query = new JPAQuery<>(em);
		QOrder order = QOrder.order;
		QCustomer customer = QCustomer.customer;
		QOrderItem orderItem = QOrderItem.orderItem;
		
		query.select(order)
			.from(order)
			.leftJoin(order.customer(), customer).fetchJoin()
			.fetch();
	}
	
	public void thetaJoin() {
		JPAQuery<Order> query = new JPAQuery<>(em);
		QOrder order = QOrder.order;
		QCustomer customer = QCustomer.customer;
		
		query.select(order)
			.from(order, customer)
			.where(order.customer().eq(customer));
	}
	
	public void subQuery() {
		JPAQuery<Item> query = new JPAQuery<>(em);
		QItem item = QItem.item;
		QItem itemSub = new QItem("itemSub");
		
		query.select(item)
			.from(item)
			.where(item.price.eq(
					JPAExpressions.select(itemSub.price.max()).from(itemSub)
			))
			.fetch();
		
		query.select(item)
			.from(item)
			.where(item.in(
					JPAExpressions.select(itemSub).from(itemSub).where(itemSub.name.eq(item.name))
			)).fetch();
	}
	
	public void projectionOne() {
		JPAQuery<Item> query = new JPAQuery<>(em);
		QItem item = QItem.item;
		
		List<String> itemNames = query.select(item.name).from(item).fetch();
	}
	
	public void projectionMulti() {
		JPAQuery<Tuple> query = new JPAQuery<>(em);
		QItem item = QItem.item;
		
		List<Tuple> tuples = query.select(item.name, item.price).from(item).fetch();
		for(Tuple row : tuples) {
			System.out.println("item name : " + row.get(item.name));
			System.out.println("item price : " + row.get(item.price));
		}
	}
	
	public void projectionDTO() {
		JPAQuery<MemberDTO> query = new JPAQuery<>(em);
		QMember member = QMember.member;
		
		List<MemberDTO> members =
			query.select(Projections.bean(MemberDTO.class, member.username, member.age))
				.from(member).fetch();
	}
	
	public void update() {
		QItem item = QItem.item;
		JPAUpdateClause query = new JPAUpdateClause(em, item);
		query.set(item.price, item.price.add(1000))
			.where(item.id.eq(1L))
			.execute();
	}
	
	public void delete() {
		QItem item = QItem.item;
		JPADeleteClause query = new JPADeleteClause(em, item);
		query.where(item.name.eq("JPA 프로그래밍")).execute();
	}
	
	public List<Item> dynamicQuery(String itemName, Integer price) {
		JPAQuery<Item> query = new JPAQuery<>(em);
		QItem item = QItem.item;
		
		BooleanBuilder builder = new BooleanBuilder();
		if(StringUtils.hasText(itemName)) {
			builder.and(item.name.contains(itemName));
		}
		if(price != null) {
			builder.and(item.price.gt(price));
		}
		
		return query.select(item)
			.from(item)
			.where(builder)
			.fetch();
	}
	
	
	private void insertItems() {
		Book item1 = new Book();
		item1.setAuthor("이영한");
		item1.setName("JPA 프로그래밍");
		item1.setPrice(28000);
		
		Movie item2 = new Movie();
		item2.setName("어벤져스");
		item2.setPrice(15000);
		
		em.persist(item1);
		em.persist(item2);
	}

}
