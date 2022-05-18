package io.timpac.shop.jpql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.timpac.shop.member.domain.Member;

@DataJpaTest
public class NativeSqlTest {
	@PersistenceContext
	private EntityManager em;
	
	@Test
	public void entity() {
		insertMembers();
		
		Query query = em.createNativeQuery("select id, age, name, team_id from member where age > ?", Member.class)
			.setParameter(1, 20);
		List<Member> resultList = query.getResultList();
		
		assertEquals(1, resultList.size());
		assertEquals("회원2", resultList.get(0).getUsername());
	}

	@Test
	public void namedNativeSQL() {
		List<Object[]> resultList = em.createNamedQuery("Member.memberWithOrderCountXml").getResultList();
		
	}
	
	private void insertMembers() {
		Member member1 = new Member("member1", "회원1");
		member1.setAge(20);
		em.persist(member1);
		
		Member member2 = new Member("member2", "회원2");
		member2.setAge(30);
		em.persist(member2);
	}
}
