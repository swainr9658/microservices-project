package com.ranjatech.microservices.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/test-log")
    public String test() {
        log.info("API Gateway test log to CloudWatch");
        return "ok";
    }
}
