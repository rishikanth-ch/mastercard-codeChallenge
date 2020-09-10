package com.mccode.connectedCities.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.ExecutionException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(CityController.class)
//@SpringBootTest
public class CityControllerEndpointTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CityService cityService;

    @Test
    public void connectedCities() throws Exception {
        given(cityService.areCitiesConnected("Boston","Newark")).willReturn("yes");
        given(cityService.areCitiesConnected("Albany","Boston")).willReturn("no");
        given(cityService.areCitiesConnected("Philadelphia","Newark")).willReturn("yes");
        given(cityService.areCitiesConnected("x","y")).willReturn("no");

        mvc.perform(MockMvcRequestBuilders.get("/connected?origin=Boston&destination=Newark")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("yes"));

        mvc.perform(MockMvcRequestBuilders.get("/connected?origin=Albany&destination=Boston")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("no"));

        mvc.perform(MockMvcRequestBuilders.get("/connected?origin=Philadelphia&destination=Newark")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("yes"));

        mvc.perform(MockMvcRequestBuilders.get("/connected?origin=x&destination=y")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("no"));

    }

//    @Test
//    public void areCitiesConnectedDirectly_connected() throws ExecutionException, InterruptedException {
//        String areConnected = cityService.areCitiesConnected("Boston","Newark");
//        assertEquals("yes",areConnected);
//    }
//
//    @Test
//    public void areCitiesConnecte_notConnected() throws ExecutionException, InterruptedException {
//        String areConnected = cityService.areCitiesConnected("Albany","Boston");
//        assertEquals("no",areConnected);
//    }
//
//    @Test
//    public void areCitiesConnectedWithHops_connected() throws ExecutionException, InterruptedException {
//        String areConnected = cityService.areCitiesConnected("Philadelphia","Newark");
//        assertEquals("yes",areConnected);
//    }
//
//    @Test
//    public void areCitiesConnected_incorrectInput() throws ExecutionException, InterruptedException {
//        String areConnected = cityService.areCitiesConnected("x","y");
//        assertEquals("no",areConnected);
//    }

}