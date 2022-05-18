package io.timpac.shop.transaction;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.timpac.shop.menu.Menu;

@DataJpaTest
public class SecondaryCacheTest {
	@PersistenceUnit
	private EntityManagerFactory emf;
	
	@PersistenceContext
	private EntityManager em;
	
	@Test
	public void cache() {
		Menu menu = new Menu();
		menu.setId("menu1");
		menu.setName("메뉴1");
		
		em.setProperty("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
		em.setProperty("javax.persistence.cache.storeMode", CacheStoreMode.USE);
		
		em.persist(menu);
		em.flush();
		em.clear();
		
		Map<String, Object> props = new HashMap<>();
		props.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
		props.put("javax.persistence.cache.storeMode", CacheStoreMode.USE);
		Menu cacheMenu = em.find(Menu.class, "menu1", props);
		
		System.out.println(cacheMenu.toString());
		
		Cache cache = emf.getCache();
		assertTrue(cache.contains(Menu.class, menu.getId()));
	}

}
