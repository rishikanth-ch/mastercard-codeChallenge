package com.mccode.connectedCities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
class ConnectedCitiesApplicationTests {

	@Test
	void contextLoads() {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(ConnectedCitiesApplication.class);
	}

}
