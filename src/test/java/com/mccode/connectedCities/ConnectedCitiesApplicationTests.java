package com.mccode.connectedCities;

import com.mccode.connectedCities.config.FileLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ConnectedCitiesApplicationTests {

	@Test
	void contextLoads() {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(ConnectedCitiesApplication.class);
		FileLoader service = configurableApplicationContext.getBean(FileLoader.class);
	}

	@Autowired
	@Qualifier("Connections")
	private Map<String, Set<String>> connections;

	@Test
	void getMapping() {
		assertEquals(6,connections.size());
		assertEquals(2,connections.get("Boston").size());
	}

}
