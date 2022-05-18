package io.timpac.shop.Item.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.timpac.shop.member.domain.Member;
import io.timpac.shop.member.domain.Team;

@RestController
public class TestController {
	@PersistenceContext
	private EntityManager em;
	
	@GetMapping("test")
	@Transactional
	public void test() {
		Team team1 = new Team("team1", "팀1");
		em.persist(team1);
		
		Member member1 = new Member("member1", "회원1");
		member1.setTeam(team1);
		em.persist(member1);
		
		Member member2 = new Member("member2", "회원2");
		member2.setTeam(team1);
		em.persist(member2);
		
		Team team = em.find(Team.class, "team1");
		List<Member> members = team.getMembers();
		
		System.out.println(members.size());
		System.out.println(members.get(0).getUsername());
		System.out.println(members.get(1).getUsername());
	}

}
