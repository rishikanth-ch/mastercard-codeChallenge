package com.mccode.connectedCities.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityControllerTest {

    @Autowired
    private CityService cityService;

    @Before
    public void setup() throws ExecutionException, InterruptedException {
//        when(cityService.areCitiesConnected("Boston","Newark")).thenReturn("yes");
//        when(cityService.areCitiesConnected("Albany","Boston")).thenReturn("no");
//        when(cityService.areCitiesConnected("Philadelphia","Newark")).thenReturn("yes");
//        when(cityService.areCitiesConnected("x","y")).thenReturn("no");
    }

     @Test
    public void areCitiesConnectedDirectly_connected() throws ExecutionException, InterruptedException {
        String areConnected = cityService.areCitiesConnected("Boston","Newark");
        assertEquals("yes",areConnected);
    }

    @Test
    public void areCitiesConnecte_notConnected() throws ExecutionException, InterruptedException {
        String areConnected = cityService.areCitiesConnected("Albany","Boston");
        assertEquals("no",areConnected);
    }

    @Test
    public void areCitiesConnectedWithHops_connected() throws ExecutionException, InterruptedException {
        String areConnected = cityService.areCitiesConnected("Philadelphia","Newark");
        assertEquals("yes",areConnected);
    }

    @Test
    public void areCitiesConnected_incorrectInput() throws ExecutionException, InterruptedException {
        String areConnected = cityService.areCitiesConnected("x","y");
        assertEquals("no",areConnected);
    }

}