package com.mccode.connectedCities.service;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jndi.JndiAccessor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class CityServiceTest {

    Set<String> pathTraversed = new HashSet<>();
    AtomicBoolean interruptProcess = new AtomicBoolean(false);

    @Autowired
    CityService cityService;

    @Autowired
    @Qualifier("Connections")
    private volatile Map<String, Set<String>> connections;

    @Before
    public void setUp() throws ExecutionException, InterruptedException {
//        connections = new HashMap<>();
//        connections.put("Boston",new HashSet<>(Arrays.asList("New York","Newark")));
//        connections.put("Albany",new HashSet<>(Arrays.asList("Trenton")));
//        connections.put("Philadelphia",new HashSet<>(Arrays.asList("Boston")));
        when(cityService.traverseConnections("Philadelphia","Newark",null,connections)).thenReturn(true);
        when(cityService.traverseConnections("Newark","Philadelphia",null,connections)).thenReturn(true);
        when(cityService.traverseConnections("Albany","Newark",null,connections)).thenReturn(false);
        when(cityService.traverseConnections("Newark","Albany",null,connections)).thenReturn(false);
//        when(cityService.areCitiesConnected("Boston","Newark")).thenReturn("yes");
//        when(cityService.areCitiesConnected("Albany","Trenton")).thenReturn("yes");
//        when(cityService.areCitiesConnected("Albany","Boston")).thenReturn("no");
//        when(cityService.areCitiesConnected("Philadelphia","Newark")).thenReturn("yes");
     }

    @AfterEach
    void tearDown() {
        pathTraversed.clear();
        interruptProcess.set(false);
    }

    @Test
    void areCitiesConnectedWithHops_connected() throws ExecutionException, InterruptedException {
        boolean areConnected = false;
        String origin ="Boston", destination = "Newark";
        if(connections.containsKey(origin) && connections.containsKey(destination)) {
            if (connections.get(origin).contains(destination) || connections.get(destination).contains(origin)) {
                areConnected = true;
            } else {
                final CompletableFuture<Boolean> originConnections = CompletableFuture.supplyAsync(() -> cityService.traverseConnections(origin, destination, null, connections));
                final CompletableFuture<Boolean> destConnections = CompletableFuture.supplyAsync(() -> cityService.traverseConnections(destination, origin, null, connections));
                assertTrue(originConnections.get());
                assertTrue(destConnections.get());
            }
        }
    }

    @Test
    void areCitiesConnectedWithHops_notConnected() throws ExecutionException, InterruptedException {
        boolean areConnected = false;
        String origin ="Albany", destination = "Newark";
        if(connections.containsKey(origin) && connections.containsKey(destination)) {
            if (connections.get(origin).contains(destination) || connections.get(destination).contains(origin)) {
                areConnected = true;
            } else {
                final CompletableFuture<Boolean> originConnections = CompletableFuture.supplyAsync(() -> cityService.traverseConnections(origin, destination, null, connections));
                final CompletableFuture<Boolean> destConnections = CompletableFuture.supplyAsync(() -> cityService.traverseConnections(destination, origin, null, connections));
                assertFalse(originConnections.get());
                assertFalse(destConnections.get());
            }
        }
    }

    @Test
    void areCitiesConnectedDirectly() throws ExecutionException, InterruptedException {
        boolean areConnected = false;
        String origin ="Boston", destination = "Newark";
        if(connections.containsKey(origin) && connections.containsKey(destination)) {
            if(connections.get(origin).contains(destination) || connections.get(destination).contains(origin)){
                areConnected = true;
            }
        }
        assertTrue(areConnected);
    }

    @Test
    void areCitiesConnected_incorrectCities() throws ExecutionException, InterruptedException {
        boolean areConnected = false;
        String origin ="x", destination = "y";
        if(connections.containsKey(origin) && connections.containsKey(destination)) {
            if(connections.get(origin).contains(destination) || connections.get(destination).contains(origin)){
                areConnected = true;
            }
        }
        assertFalse(areConnected);
    }


    @Test
    void traverseConnections() {
    }
}