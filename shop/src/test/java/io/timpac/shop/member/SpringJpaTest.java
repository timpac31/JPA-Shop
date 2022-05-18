package io.timpac.shop.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;

import io.timpac.shop.member.domain.Member;
import io.timpac.shop.member.domain.MemberSpec;
import io.timpac.shop.member.domain.NoResultTestRepository;
import io.timpac.shop.member.domain.Team;
import io.timpac.shop.member.repository.MemberRepository;

@SpringBootTest
@Transactional
public class SpringJpaTest {
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private NoResultTestRepository noRepository;
		
	@PersistenceContext
	private EntityManager em;
	
	
	@Test
	public void findByUsernameAndAge() {
		insertTwoMember();
		
		List<Member> findedMembers = memberRepository.findByUsernameAndAge("회원1", 20);
		List<Member> notMatchMembers = memberRepository.findByUsernameAndAge("회원1", 30);
		
		assertEquals(1, findedMembers.size());
		assertEquals(0, notMatchMembers.size(), "찾는 값이 없으면 빈컬렉션 리턴");
		assertEquals("회원1", findedMembers.get(0).getUsername());
	}

	@Test
	public void namedQuery() {
		insertTwoMember();
		
		List<Member> members = memberRepository.findByCreateDateAfter(new Date());
		
		assertEquals(1, members.size());
		assertEquals("회원2", members.get(0).getUsername());
	}
	
	@Test
	public void paging() {
		insertMembers();
		
		PageRequest pageReq = PageRequest.of(0, 10, Sort.by(new Order(Direction.DESC, "username")));
		Page<Member> result = memberRepository.findByUsernameStartingWith("회원", pageReq);
		
		List<Member> members = result.getContent();
		Member firstMember = members.get(0);
		
		assertEquals("회원9", firstMember.getUsername());
		assertEquals(2, result.getTotalPages());
		assertEquals(19, result.getTotalElements());
		assertTrue(result.isFirst());
	}
	
	@Test
	public void specification() {
		//given
		Team team = new Team("team1", "개발팀");
		em.persist(team);
		
		Member member1 = new Member("member1", "회원1");
		Member member2 = new Member("member2", "회원2");
		Member member3 = new Member("member3", "회원3");
		member2.setTeam(team);
		
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		
		//when
		List<Member> developMembers = memberRepository.findAll(MemberSpec.teamName("개발팀"));
		
		//then
		assertEquals(1, developMembers.size());
		assertEquals("회원2", developMembers.get(0).getUsername());
	}
	
	@Test
	public void 예외변환테스트() {
		assertNotNull(noRepository);
		
		assertThrows(EmptyResultDataAccessException.class, () -> {
			noRepository.findMember();
		});
	}
	

	private void insertMembers() {
		List<Member> members = new ArrayList<>();
		for(int i=1; i<20; i++) {
			members.add(new Member("member" + i, "회원" + i));
		}
		memberRepository.saveAll(members);
	}
	
	private void insertTwoMember() {
		Member member1 = new Member("member1", "회원1");
		member1.setAge(20);
		memberRepository.save(member1);
		
		Member member2 = new Member("member2", "회원2");
		member2.setAge(35);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 1);
		member2.setCreateDate(cal.getTime());
		memberRepository.save(member2);
	}
	
}
