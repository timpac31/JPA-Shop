package io.timpac.shop.jpql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.timpac.shop.member.domain.Member;
import io.timpac.shop.member.domain.RoleType;
import io.timpac.shop.member.domain.Team;
import io.timpac.shop.member.dto.MemberDTO;

@DataJpaTest
public class BasicJpqlTest {
	@PersistenceContext
	private EntityManager em;
	
	//@Test
	public void multiProjectionPrimitive() {
		insertMembers();
		
		Query q = em.createQuery("select m.username, m.age from Member m");
		List<Object[]> resultList = q.getResultList();
		
		assertEquals("회원1", (String) resultList.get(0)[0]);
		assertEquals(20, (Integer) resultList.get(0)[1]);
		assertEquals("회원2", (String) resultList.get(1)[0]);
		assertEquals(30, (Integer) resultList.get(1)[1]);
	}
	
	//@Test
	public void multiProjectionByDto() {
		insertMembers();
		
		TypedQuery<MemberDTO> q = em.createQuery("select new io.timpac.shop.member.dto.MemberDTO(m.username, m.age) from Member m", MemberDTO.class);
		List<MemberDTO> resultList = q.getResultList();
		
		assertEquals("회원1",  resultList.get(0).getUsername());
		assertEquals(20, (Integer) resultList.get(0).getAge());
		assertEquals("회원2", (String) resultList.get(1).getUsername());
		assertEquals(30, (Integer) resultList.get(1).getAge());
	}
	
	//@Test
	public void groupBy() {
		insertMembers();
		
		String sql = "select COUNT(m.age), AVG(m.age), MAX(m.age) from Member m GROUP BY m.roleType";
		List<Object[]> resultList = em.createQuery(sql).getResultList();
		
		long adminCount = (Long) resultList.get(0)[0];
		Double adminAvg = (Double) resultList.get(0)[1];
		Integer adminMax = (Integer) resultList.get(0)[2];
		long userCount = (Long) resultList.get(1)[0];
		Double userAvg = (Double) resultList.get(1)[1];
		Integer userMax = (Integer) resultList.get(1)[2];
		
		assertEquals(2, resultList.size());
		assertEquals(1, adminCount);
		assertEquals(20, adminAvg);
		assertEquals(20, adminMax);
		assertEquals(2, userCount);
		assertEquals(35, userAvg);
		assertEquals(40, userMax);
	}
	
	@Test
	public void join() {
		insertMembersAndTeam();
		
		String teamName = "development";
		String query = "SELECT m FROM Member m LEFT JOIN m.team t ON t.name = :teamName";
		TypedQuery<Member> q = em.createQuery(query, Member.class);
		q.setParameter("teamName", teamName);
		List<Member> resultList = q.getResultList();
		
		assertEquals(3, resultList.size());
		assertEquals(teamName, resultList.get(0).getTeam().getName());
	}
	
	@Test
	public void sebQuery() {
		insertMembers();
		
		int overAvgAge = em.createQuery("select m from Member m where m.age >= (select avg(m2.age) from Member m2)", Member.class)
					.getResultList().size();
		
		assertEquals(2, overAvgAge);
	}
	
	private void insertMembers() {
		Member member1 = new Member("member1", "회원1");
		member1.setAge(20);
		member1.setRoleType(RoleType.ADMIN);
		em.persist(member1);
		
		Member member2 = new Member("member2", "회원2");
		member2.setAge(30);
		member2.setRoleType(RoleType.USER);
		em.persist(member2);
		
		Member member3 = new Member("member3", "회원3");
		member3.setAge(40);
		member3.setRoleType(RoleType.USER);
		em.persist(member3);
	}
	
	private void insertMembersAndTeam() {
		Team team1 = new Team("team1", "development");
	    em.persist(team1);
		
		Member member1 = new Member("member1", "회원1");
		member1.setAge(20);
		member1.setTeam(team1);
		member1.setRoleType(RoleType.ADMIN);
		em.persist(member1);
		
		Member member2 = new Member("member2", "회원2");
		member2.setAge(30);
		member2.setTeam(team1);
		member2.setRoleType(RoleType.USER);
		em.persist(member2);
		
		Member member3 = new Member("member3", "회원3");
		member3.setAge(40);
		member3.setRoleType(RoleType.USER);
		em.persist(member3);
	}
	
}
