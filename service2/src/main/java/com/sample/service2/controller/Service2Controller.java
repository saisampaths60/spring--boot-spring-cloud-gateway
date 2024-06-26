package com.sample.service2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service2")
public class Service2Controller {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Service 2!";
    }
}
