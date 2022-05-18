package io.timpac.shop.member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.timpac.mapping.Mother;
import io.timpac.mapping.Son;

@DataJpaTest
public class MappingTest {
	@PersistenceContext
	private EntityManager em;
	
	public void saveNoCascade() {
		Mother mother = new Mother();
		em.persist(mother);
		
		Son son1 = new Son();
		son1.setMother(mother);
		mother.getSon().add(son1);
		em.persist(son1);
		
		Son son2 = new Son();
		son2.setMother(mother);
		mother.getSon().add(son2);
		em.persist(son2);
	}
	
	public void saveWithCascade() {
		Mother mother = new Mother();
		Son son1 = new Son();
		Son son2 = new Son();
		
		son1.setMother(mother);
		son2.setMother(mother);
		mother.getSon().add(son1);
		mother.getSon().add(son2);
		
		em.persist(mother);
	}
	
}
