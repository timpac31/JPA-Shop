package io.timpac.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ShopApplicationTests {
	
	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		System.out.println("============= Spring Bean List ====================");
		for(String bean : context.getBeanDefinitionNames()) {
			System.out.println(bean);
		}
		System.out.println("============= Spring Bean List ====================");
	}

}
