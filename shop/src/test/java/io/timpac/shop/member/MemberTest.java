package io.timpac.shop.member;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.timpac.shop.member.domain.Member;
import io.timpac.shop.member.domain.Team;

@DataJpaTest
public class MemberTest {
	@PersistenceContext
	private EntityManager em;
	
	@BeforeEach
	public void initInsert() {
		Team team1 = new Team("team1", "팀1");
		em.persist(team1);
		
		Member member1 = new Member("member1", "회원1");
		member1.setTeam(team1);
		em.persist(member1);
		
		Member member2 = new Member("member2", "회원2");
		member2.setTeam(team1);
		em.persist(member2);
	}
	
	@Test
	public void queryLogicJoin() {
		String jpql = "select m from Member m join m.team t where t.name=:teamName";
		List<Member> resultList = em.createQuery(jpql, Member.class)
				.setParameter("teamName", "팀1")
				.getResultList();
		
		assertEquals(2, resultList.size());
		
		for(Member member : resultList) {
			System.out.println("username: " + member.getUsername());
		}
	}
	
	@Test
	public void biDirection() {
		Team team = em.find(Team.class, "team1");
		List<Member> members = team.getMembers();
		
		assertEquals(2, members.size());
		assertEquals("회원1", members.get(0).getUsername());
		assertEquals("회원2", members.get(1).getUsername());
	}
	
	@Test
	public void useValueCollection() {
		Member member = em.find(Member.class, "member1");
		member.addFavoriteFoods("짜장");
		member.addFavoriteFoods("짬뽕");
		member.addFavoriteFoods("탕수육");
		
		em.persist(member);
	}
	
}
