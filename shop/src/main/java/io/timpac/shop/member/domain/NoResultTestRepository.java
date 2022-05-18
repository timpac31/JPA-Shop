package io.timpac.shop.member.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class NoResultTestRepository {
	@PersistenceContext
	private EntityManager em;
	
	public Member findMember() {
		return em.createQuery("select m from Member m", Member.class).getSingleResult();
	}
}
