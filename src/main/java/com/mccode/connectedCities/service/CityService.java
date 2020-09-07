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

@Service
public class CityService {

    @Autowired
    @Qualifier("Connections")
    private Map<String, Set<String>> connections;

    @Autowired
    CityConnections cityConnections;

    public String areCitiesConnected(String origin, String destination) throws ExecutionException, InterruptedException {
        final boolean areConnected;
        if(connections.containsKey(origin) && connections.containsKey(destination)) {
            System.out.println(cityConnections.pathTraversed);
            final CompletableFuture<Boolean> originConnections = CompletableFuture.supplyAsync(() -> cityConnections.traverseConnections(origin,destination,null));
            final CompletableFuture<Boolean> destConnections = CompletableFuture.supplyAsync(() -> cityConnections.traverseConnections(destination,origin,null));
            if(originConnections.get()) {
                System.out.println("Found destination...");
                destConnections.cancel(true);
                cityConnections.pathTraversed.clear();
                return "yes";
            }
            if(destConnections.get()) {
                System.out.println("Found origin...");
                originConnections.cancel(true);
                cityConnections.pathTraversed.clear();
                return "yes";
            }
        }
        cityConnections.pathTraversed.clear();
        return "no";
    }

    private void traversePath(String origin){

    }
}

