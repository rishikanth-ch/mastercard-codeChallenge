package com.mccode.connectedCities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CityConnections {

    public volatile Set<String> pathTraversed = new HashSet<>();

    @Autowired
    @Qualifier("Connections")
    private Map<String, Set<String>> connections;

    public boolean traverseConnections(String origin, String destination, String city){
        String currentCity = city==null?origin:city;
        System.out.println("Started looking connections for "+currentCity);
        for(String conn : connections.get(currentCity)){
            if(!conn.equals(origin) && (pathTraversed.contains(conn) || conn.equals(destination))){
                System.out.println("Destination Found for "+origin);
                return true;
            }else{
                synchronized (pathTraversed){
                    pathTraversed.add(conn);
                    System.out.println(conn);
                }
                traverseConnections(origin,destination,conn);
            }
        }
        System.out.println(destination+" is not in path");
        return false;
    }
}
