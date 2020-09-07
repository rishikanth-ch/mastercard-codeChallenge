package com.mccode.connectedCities;

import com.mccode.connectedCities.config.FileLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ConnectedCitiesApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(ConnectedCitiesApplication.class, args);
		FileLoader service = configurableApplicationContext.getBean(FileLoader.class);

	}

}
