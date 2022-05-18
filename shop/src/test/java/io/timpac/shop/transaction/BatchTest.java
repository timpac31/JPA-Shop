package io.timpac.shop.transaction;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.timpac.shop.Item.domain.Item;
import io.timpac.shop.member.domain.Member;

@DataJpaTest
public class BatchTest {
	@PersistenceUnit
	private EntityManagerFactory emf;
	
	@PersistenceContext
	private EntityManager em;
	
	public void batchInsert() {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		for(int i=0; i<100000; i++) {
			Member member = new Member("member" + i, "회원" + i);
			em.persist(member);
			
			if(i % 100 == 0) {
				em.flush();
				em.clear();
			}
		}
		
		tx.commit();
		em.close();
	}

	public void batchUpdateByPaging() {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		int pageSize = 100;
		for(int i=0; i<10; i++) {
			List<Item> items = em.createQuery("select i from Item i", Item.class)
								.setFirstResult(i * pageSize)
								.setMaxResults(pageSize)
								.getResultList();
			
			for(Item item : items) {
				item.setPrice(item.getPrice() + 1000);
			}
			
			em.flush();
			em.clear();
		}
		
		tx.commit();
		em.close();
	}
	
	public void batchUpdateByScroll() {
		EntityTransaction tx = em.getTransaction();
		Session session = em.unwrap(Session.class);
		
		tx.begin();
		
		ScrollableResults scroll = session
				.createQuery("select i from Item i")
				.setCacheMode(CacheMode.IGNORE)
				.scroll(ScrollMode.FORWARD_ONLY);
		
		int count = 0;
		while(scroll.next()) {
			Item item = (Item) scroll.get(0);
			item.setPrice(item.getPrice() + 1000);
			
			if(count % 100 == 0) {
				session.flush();
				session.clear();
			}
		}
		
		tx.commit();
		session.close();
	}
	
	public void batchUpdateByStatelessSession() {
		SessionFactory factory = emf.unwrap(SessionFactory.class);
		StatelessSession session = factory.openStatelessSession();	//영속성 컨텍스트가 없는 세션
		Transaction tx = session.beginTransaction();
		
		ScrollableResults scroll = session.createQuery("select i from Item i").scroll();
		while(scroll.next()) {
			Item item = (Item) scroll.get(0);
			item.setPrice(item.getPrice() + 1000);
			session.update(item);
		}
		
		tx.commit();
		session.close();
	}	
	
}
