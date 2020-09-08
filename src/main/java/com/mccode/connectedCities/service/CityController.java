package com.mccode.connectedCities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class CityController {
    @Autowired
    CityService cityService;

    /**
     *
     * @param origin
     * @param destination
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/connected")
    @ResponseBody
    public String connectedCities(@RequestParam("origin") String origin,
                                  @RequestParam("destination") String destination) throws ExecutionException, InterruptedException {
        return cityService.areCitiesConnected(origin,destination);
    }
}
