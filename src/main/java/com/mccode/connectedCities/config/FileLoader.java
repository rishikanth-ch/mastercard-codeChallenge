package com.mccode.connectedCities.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.*;

@Configuration
public class FileLoader {

    @Value("${file-to-load}")
    private String fileName;

    @Autowired
    ResourceLoader resourceLoader;

    /**
     * The connections file is loaded when app startup and connections map which maps direct connections to a city
     * is created to serve the requests.
     * @return
     */
    @Bean(name="Connections")
    public Map<String, Set<String>> getMapping() {
        //This method will create a list of all the connections to a given node from the input file provided
         Map<String,Set<String>> connections = new HashMap<>();
         Resource file = resourceLoader.getResource("classpath:"+fileName);
        try(Scanner sc = new Scanner(file.getInputStream())) {
            String[] cities;
            Set cityList;
            String city1,city2;
            while(sc.hasNextLine()){
                cities = sc.nextLine().split(",");
                city1 = cities[0].trim();
                city2 = cities[1].trim();
                getMapping(connections, city1, city2);
                getMapping(connections, city2, city1);
            }
        } catch (IOException e) {
            System.out.println("Unable to load file.");
        }
        return connections;
    }

    /**
     * This method will add a city to existing list of connections or create a new list if the city doesn't exist in map.
     * @param connections
     * @param origin
     * @param destination
     */
    private void getMapping(Map<String, Set<String>> connections, String origin, String destination) {
        Set<String> cityList;
        if(connections.containsKey(origin)){
            cityList = connections.get(origin);
        }else{
            cityList = new HashSet<>();
        }
        if(!cityList.contains(destination)) {
            cityList.add(destination);
            connections.put(origin, cityList);
        }
    }

}
