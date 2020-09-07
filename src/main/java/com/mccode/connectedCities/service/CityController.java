package com.mccode.connectedCities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class CityController {
    @Autowired
    CityService cityService;

    @GetMapping("/connected")
    @ResponseBody
    public String connectedCities(@RequestParam("origin") String origin,
                                  @RequestParam("destination") String destination) throws ExecutionException, InterruptedException {
        return cityService.areCitiesConnected(origin,destination);
    }
}
