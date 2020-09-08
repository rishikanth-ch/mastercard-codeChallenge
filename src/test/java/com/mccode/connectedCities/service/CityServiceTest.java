package com.mccode.connectedCities.service;

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class CityServiceTest {

    private Map<String, Set<String>> connections;

    Set<String> pathTraversed = new HashSet<>();
    AtomicBoolean interruptProcess = new AtomicBoolean(false);

    @MockBean
    CityService cityService;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        connections = new HashMap<>();
        connections.put("Boston",new HashSet<>(Arrays.asList("New York","Newark")));
        connections.put("Albany",new HashSet<>(Arrays.asList("Trenton")));
        connections.put("Philadelphia",new HashSet<>(Arrays.asList("Boston")));
        when(cityService.traverseConnections("Philadelphia","Newark",null,connections)).thenReturn(true);
        when(cityService.areCitiesConnected("Boston","Newark")).thenReturn("yes");
        when(cityService.areCitiesConnected("Albany","Trenton")).thenReturn("yes");
        when(cityService.areCitiesConnected("Albany","Boston")).thenReturn("no");
        when(cityService.areCitiesConnected("Philadelphia","Newark")).thenReturn("yes");
     }

    @AfterEach
    void tearDown() {
        pathTraversed.clear();
        interruptProcess.set(false);
    }

    @Test
    void areCitiesConnectedWithHops() throws ExecutionException, InterruptedException {
        assertEquals("yes",cityService.areCitiesConnected("Philadelphia","Newark"));
        assertEquals(true,cityService.traverseConnections("Philadelphia","Newark",null,connections));
    }

    @Test
    void areCitiesConnectedDirectly() throws ExecutionException, InterruptedException {
        assertEquals("yes",cityService.areCitiesConnected("Boston","Newark"));
        assertEquals("yes",cityService.areCitiesConnected("Albany","Trenton"));
        assertEquals("no",cityService.areCitiesConnected("Albany","Boston"));
    }


    @Test
    void traverseConnections() {
    }
}