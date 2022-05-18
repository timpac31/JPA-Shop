package io.timpac.shop.start;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import io.timpac.shop.member.domain.Member;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class StartTest {
	@PersistenceContext
	private EntityManager em;
	
	@Test
	@Transactional
	public void create() {
		String id = "id1";
		String username = "영덕";
		Member member = new Member(id, username);
		member.setAge(2);
		
		em.persist(member);
		member.setAge(20);
		
		Member findedMember = em.find(Member.class, id);
		List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
		
		assertEquals(username, findedMember.getUsername());
		assertEquals(20, findedMember.getAge());
		assertEquals(1, members.size());
	}
	
	

}
