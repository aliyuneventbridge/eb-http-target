package com.aliyuneventbridge.httptarget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HttpTargetApplication {

    public static List<Map<String, Object>> requestLists = new CopyOnWriteArrayList<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(HttpTargetApplication.class, args);
	}

    @RequestMapping("/cloudevents")
    public String onEvents(@RequestHeader Map<String, Object> headers, @RequestBody String body) {
        Map<String, Object> request = new HashMap<>();
        try {
            final Map<String, Object> bodyJsonMap = new Gson().fromJson(body, Map.class);
            request.put("HttpBody", bodyJsonMap);
        } catch (Throwable e) {
            logger.error("cloudevents", e);
            request.put("HttpBody", body);
        }
        request.put("HttpHeaders", headers);
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


    @RequestMapping("/smq_encode")
    public String smq_encode(@RequestParam("id") String uuid) {
	    return encodeUUID(Long.parseLong(uuid));
    }

    @RequestMapping("/smq_decode")
    public String smq_decode(@RequestParam("id") String uuid) {
        return decodeUUID(uuid);
    }

    public static String decodeUUID(String uuid) {
        try {
            long data = Long.parseLong(uuid, 36);
            return String.valueOf(data);
        } catch (Exception e) {
            return uuid;
        }
    }

    public static String encodeUUID(long uuid) {
        return Long.toString(uuid, 36);
    }
}

