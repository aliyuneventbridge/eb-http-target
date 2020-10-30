package com.aliyuneventbridge.httptarget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HttpTargetApplication {

    List<Map<String, Map<String, Object>>> requestLists = new CopyOnWriteArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(HttpTargetApplication.class, args);
	}

    @RequestMapping("/cloudevents")
    public String onEvents(@RequestHeader Map<String, Object> headers, @RequestBody Map<String, Object> event) {
        Map<String, Map<String, Object>> request = new HashMap<>();
        request.put("HttpHeaders", headers);
        request.put("HttpBody", event);
        requestLists.add(0, request);

        if (requestLists.size() > 10) {
            requestLists.remove(requestLists.size() - 1);
        }

	    return "OK";
    }

    @RequestMapping("/httptrace")
    public String requestLists() {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        return gson.toJson(requestLists);
    }
}

