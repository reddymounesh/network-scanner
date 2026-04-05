package com.scanner;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ResultCollector {

    private final Map<String,List<ScanResult>> results = new ConcurrentHashMap<>();

    public void add(ScanResult result){
        
        results.computeIfAbsent(
            result.getHost(),
            k -> Collections.synchronizedList(new ArrayList<>())
        ).add(result);

    }

    public Map<String,List<ScanResult>> getResults(){
        return Collections.unmodifiableMap(results);

    }

    public void printSummary() {
        System.out.println("\n ==== SCAN RESULTS ====");
        results.forEach((host , ports) -> {
            System.out.println("\nHost: "+host);
            ports.stream().sorted((a,b) -> Integer.compare(a.getPort(), b.getPort())).forEach(r -> System.out.println(" " + r.getPort() + "/tcp open " + (r.getBanner() != null ? r.getBanner():"unknown")));

        });

        System.out.println("\n Total open ports found : " + results.values().stream().mapToInt(List::size).sum());

    }

    public void exportJson(String filename){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<Map<String,Object>> output = new ArrayList<>();
        results.forEach((host,ports) -> {
            Map<String,Object> entry = new LinkedHashMap<>();
            entry.put("host",host);
            entry.put("openPorts",ports.stream().map(r -> {
                Map<String,Object> p = new LinkedHashMap<>();
                p.put("port", r.getPort());
                p.put("state", r.getState().toString());
                p.put("banner",r.getBanner());
                return p;

            }).toList());
            output.add(entry);


        });
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(output,writer);
            System.out.println("\n[*] Results exported to : " + filename);
            
        } catch (Exception e) {
            System.err.println("[!] Export failed : "+e.getMessage());
            
        }
    }
    
}

