package com.aliyuneventbridge.httptarget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HttpTargetApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpTargetApplication.class, args);
	}

    @RequestMapping("/cloudevents")
    public String onEvents() {
        return "OK";
    }
}

