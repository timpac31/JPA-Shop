package io.timpac.shop.member;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.timpac.shop.Item.domain.Book;
import io.timpac.shop.Item.domain.Item;
import io.timpac.shop.member.domain.Member;
import io.timpac.shop.order.domain.OrderItem;

@DataJpaTest
public class ProxyTest {
	@PersistenceContext
	private EntityManager em;
	
	@Test
	public void 영속성컨텍스트와_프록시() {
		Member newMember = new Member("member1", "회원1");
		em.persist(newMember);
		em.flush();
		em.clear();
		
		Member refMember = em.getReference(Member.class, "member1");
		Member findMember = em.find(Member.class, "member1");

		assertTrue(refMember == findMember);
	}
	
	@Test
	public void 프록시와_동등성비교() {
		Member saveMember = new Member("member1", "회원1");
		em.persist(saveMember);
		em.flush();
		em.clear();

		Member newMember = new Member("member1", "회원1"); 
		Member refMember = em.getReference(Member.class, "member1");
		
		assertTrue(newMember.equals(refMember));
	}
	
	@Test
	public void 상속관계와_프록시() {
		Book book = new Book();
		book.setName("jpaBook");
		book.setAuthor("Kim");
		em.persist(book);
		
		OrderItem saveOrderItem = new OrderItem();
		saveOrderItem.setItem(book);
		em.persist(saveOrderItem);
		
		em.flush();
		em.clear();
		
		OrderItem orderItem = em.find(OrderItem.class, saveOrderItem.getId());
		Item item = orderItem.getItem();
		Item unproxyItem = (Item) Hibernate.unproxy(item);
		
		System.out.println("item class : " + item.getClass());
		
		assertFalse(item.getClass() == Book.class, "Lazy loading 이라서 프록시객체가 반환됨");
		assertFalse(item instanceof Book, "Lazy loading 이라서 프록시객체가 반환됨");
		assertTrue(unproxyItem instanceof Book, "unproxy()로 원본데이터 가져옴");
		assertTrue(item instanceof Item);
	}

}
