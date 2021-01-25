package com.aliyuneventbridge.httptarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;

import com.aliyuneventbridge.httptarget.siginature.SignatureVerify;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public static void main(String[] args) {
        SpringApplication.run(HttpTargetApplication.class, args);
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HttpServletRequest httpServletRequest;

    @RequestMapping("/cloudevents")
    public String onEvents(@RequestHeader Map<String, Object> headers, @RequestBody String body) {
        Map<String, Object> request = new HashMap<>();
        try {
            boolean isLegal = SignatureVerify.verify(buildUrlWithQueryString(httpServletRequest),
                getHeaders(httpServletRequest), body.getBytes());
            if (isLegal) {
                final Map<String, Object> bodyJsonMap = new Gson().fromJson(body, Map.class);
                request.put("HttpBody", bodyJsonMap);
                request.put("HttpHeaders", headers);
                requestLists.add(0, request);

                if (requestLists.size() > 10) {
                    requestLists.remove(requestLists.size() - 1);
                }
            }
        } catch (Throwable e) {
            request.put("HttpBody", body);
        }
        return "OK";
    }

    public String buildUrlWithQueryString(HttpServletRequest request) {
        if (Strings.isBlank(request.getQueryString())) {
            return request.getRequestURL()
                .toString();
        } else {
            return request.getRequestURL() + "?" + request.getQueryString();
        }
    }

    public List<Header> getHeaders(HttpServletRequest request) {
        List<Header> headerList = new ArrayList<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String)headerNames.nextElement();
            String value = request.getHeader(key);
            headerList.add(new BasicHeader(key, value));
        }
        return headerList;
    }

    @RequestMapping("/httptrace")
    public String requestLists() {
        Gson gson = new GsonBuilder().setPrettyPrinting()
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

