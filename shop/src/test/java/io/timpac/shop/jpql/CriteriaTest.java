package io.timpac.shop.jpql;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import io.timpac.shop.member.domain.Member;
import io.timpac.shop.member.domain.Team;
import io.timpac.shop.member.dto.MemberDTO;

public class CriteriaTest {
	@PersistenceContext
	private EntityManager em;
	
	private CriteriaBuilder cb;
	
	public CriteriaTest() {
		cb = em.getCriteriaBuilder();
	}
	
	public List<Member> start() {
		CriteriaQuery<Member> cq = cb.createQuery(Member.class);
		Root<Member> m  = cq.from(Member.class);
		cq.select(m);
		
		TypedQuery<Member> q = em.createQuery(cq);
		return q.getResultList();
	}
	
	public List<Member> atwhere() {
		CriteriaQuery<Member> cq = cb.createQuery(Member.class);
		Root<Member> m = cq.from(Member.class);
		
		Predicate p1 = cb.equal(m.get("username"), "회원1");
		Predicate p2 = cb.gt(m.<Integer>get("age"), 10);
		Predicate where = cb.and(p1, p2);
		
		cq.multiselect(cb.construct(MemberDTO.class, m.get("username"), m.get("age")))
		  .where(where)
		  .orderBy(cb.desc(m.get("age")));
		
		return em.createQuery(cq).getResultList();
	}
	
	public void tuple() {
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Member> m = cq.from(Member.class);
		cq.multiselect(
				m.get("username").alias("username"),
				m.get("age").alias("age")
		);
		
		List<Tuple> resultList = em.createQuery(cq).getResultList();
		for(Tuple tuple : resultList) {
			String username = tuple.get("username", String.class);
			Integer age = tuple.get("age", Integer.class);
		}
	}
	
	public List<Object[]> groupBy() {
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Member> m = cq.from(Member.class);
		cq.multiselect(
				m.get("team").get("name"),
				cb.max(m.<Integer>get("age")),
				cb.min(m.<Integer>get("age"))
		);
		cq.groupBy(m.get("team").get("name"));
		
		return em.createQuery(cq).getResultList();
	}
	
	public void join() {
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Member> m = cq.from(Member.class);
		Join<Member, Team> t = m.join("team", JoinType.INNER);
		
		cq.multiselect(m, t)
		  .where(cb.equal(t.get("name"), "팀A"));
	}
	
	public void subQuery() {
		CriteriaQuery<Member> cq = cb.createQuery(Member.class);
		
		Subquery<Double> subquery = cq.subquery(Double.class);
		Root<Member> m2 = subquery.from(Member.class);
		subquery.select(cb.avg(m2.get("age")));
		
		Root<Member> m = cq.from(Member.class);
		cq.select(m)
		  .where(cb.ge(m.<Integer>get("age"), subquery));
	}
	
	public void relateSubQuery() {
		// select m from Member m
		// where exists (select t from m.team t where t.name='팀A')
		
		CriteriaQuery<Member> cq = cb.createQuery(Member.class);
		Root<Member> m = cq.from(Member.class);
		
		Subquery<Team> subquery = cq.subquery(Team.class);
		Root<Member> subM = subquery.correlate(m);
		Join<Member, Team> t = subM.join("team");
		subquery.select(t).where(cb.equal(t.get("name"), "팀A"));
		
		cq.select(m)
		  .where(cb.exists(subquery));
	}
	
	public void useCase() {
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Member> m = cq.from(Member.class);
		
		cq.multiselect(
				m.get("username").alias("username"),
				cb.selectCase()
					.when(cb.ge(m.<Integer>get("age"), 60), 600)
					.when(cb.le(m.<Integer>get("age"), 15), 500)
					.otherwise(1000).alias("agePo")
		);
	}
	
	public List<Member> dynamicQuery(Integer age, String username, String teamName) {
		// select m from Member m join m.team t where m.age = :age and m.username = :username and t.name = :teamName
		
		CriteriaQuery<Member> cq = cb.createQuery(Member.class);
		Root<Member> m = cq.from(Member.class);
		Join<Member, Team> t = m.join("team");
		
		List<Predicate> criteria = new ArrayList<>();
		if(age != null)
			criteria.add(cb.equal(m.<Integer>get("age"), cb.parameter(Integer.class, "age")));
		if(username != null)
			criteria.add(cb.equal(m.get("username"), cb.parameter(String.class, "username")));
		if(teamName != null)
			criteria.add(cb.equal(t.get("name"), cb.parameter(String.class, "teamName")));
		
		cq.select(m);
		cq.where(cb.and(criteria.toArray(new Predicate[0])));
		
		TypedQuery<Member> q = em.createQuery(cq);
		if(age != null) q.setParameter("age", age);
		if(username != null) q.setParameter("username", username);
		if(teamName != null) q.setParameter("teamName", teamName);
		
		return q.getResultList();
	}

}
