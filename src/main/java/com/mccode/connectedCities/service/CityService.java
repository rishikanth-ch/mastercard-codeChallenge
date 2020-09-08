package com.mccode.connectedCities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CityService {

    @Autowired
    @Qualifier("Connections")
    private volatile Map<String, Set<String>> connections;

    volatile Set<String> pathTraversed = new HashSet<>();
    volatile AtomicBoolean interruptProcess = new AtomicBoolean(false);


    public String areCitiesConnected(String origin, String destination) throws ExecutionException, InterruptedException {
        boolean areConnected = false;
        interruptProcess.set(false);
        System.out.println(connections);
        if(connections.containsKey(origin) && connections.containsKey(destination)) {
            if(connections.get(origin).contains(destination) || connections.get(destination).contains(origin)){
                areConnected = true;
            }else {
                System.out.println(pathTraversed);

                final CompletableFuture<Boolean> originConnections = CompletableFuture.supplyAsync(() -> traverseConnections(origin, destination, null,connections));
                final CompletableFuture<Boolean> destConnections = CompletableFuture.supplyAsync(() -> traverseConnections(destination, origin, null,connections));
                if (originConnections.get()) {
                    System.out.println("Found destination...");
                    destConnections.cancel(true);
                    pathTraversed.clear();
                    areConnected = areConnected || true;
                }
                if (destConnections.get()) {
                    System.out.println("Found origin...");
                    originConnections.cancel(true);
                    areConnected = areConnected || true;
                }
            }
        }
        System.out.println(connections);
        pathTraversed.clear();
        return areConnected?"yes":"no";
    }

    public boolean traverseConnections(String origin, String destination, String city, Map<String, Set<String>> connections){
        if(interruptProcess.get()){
            System.out.println("Processing stopped for "+origin);
            return false;
        }
        String currentCity = city==null?origin:city;
        System.out.println("Started looking connections for "+currentCity);
        for(String conn : connections.get(currentCity)){
            //TODO:Dynamically update the connections with the traversed path
//            if(!conn.equals(origin)) connections.get(origin).add(conn);
            if(!conn.equals(origin) && (pathTraversed.contains(conn) || conn.equals(destination))){
                System.out.println("Destination Found for "+origin);
                interruptProcess.set(true);
                return true;
            }else{
                pathTraversed.add(conn);
                System.out.println(conn);
                traverseConnections(origin,destination,conn,connections);
            }
        }
        System.out.println(destination+" is not in path");
        return false;
    }

}

