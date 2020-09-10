package com.mccode.connectedCities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CityService {

    @Autowired
    @Qualifier("Connections")
    private volatile Map<String, Set<String>> connections;

    volatile Set<String> pathTraversed = new HashSet<>(); //Collection to track the path or cities traversed
    volatile AtomicBoolean interruptProcess = new AtomicBoolean(false); //Used to terminate threads once the connection is found

    /**
     * This method will try to find if there is a direct connection between 2 cities. If not found, then it will try to
     * figure out if the connection exists with hops.(any common cities/roads)
     * @param origin
     * @param destination
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */

    public String areCitiesConnected(String origin, String destination) throws ExecutionException, InterruptedException {
        boolean areConnected = false;
        interruptProcess.set(false);
        if(connections.containsKey(origin) && connections.containsKey(destination)) {
            if(connections.get(origin).contains(destination) || connections.get(destination).contains(origin)){
                areConnected = true;
            }else {
                final CompletableFuture<Boolean> originConnections = CompletableFuture.supplyAsync(() -> traverseConnections(origin, destination, null,connections));
                final CompletableFuture<Boolean> destConnections = CompletableFuture.supplyAsync(() -> traverseConnections(destination, origin, null,connections));
                if (originConnections.get()) {
                    destConnections.cancel(true);
                    pathTraversed.clear();
                    areConnected = areConnected || true;
                }
                if (destConnections.get()) {
                    originConnections.cancel(true);
                    areConnected = areConnected || true;
                }
            }
        }
        pathTraversed.clear();
        return areConnected?"yes":"no";
    }

    /**
     * This method will traverse the hops and tries to find if destination is in the path/hops or any common city between
     * origin and destination.
     * The traversed cities can be added to the connection list so that the connections are readily available for next request.
     * @param origin
     * @param destination
     * @param city
     * @param connections
     * @return
     */
    public boolean traverseConnections(String origin, String destination, String city, Map<String, Set<String>> connections){
        if(interruptProcess.get()){
            return false;
        }
        String currentCity = city==null?origin:city;
        for(String conn : connections.get(currentCity)){
            //TODO:Dynamically update the connections with the traversed path
//            if(!conn.equals(origin)) connections.get(origin).add(conn);
            if(!conn.equals(origin) && (pathTraversed.contains(conn) || conn.equals(destination))){
                interruptProcess.set(true);
                return true;
            }else{
                pathTraversed.add(conn);
                traverseConnections(origin,destination,conn,connections);
            }
        }
        return false;
    }

}

