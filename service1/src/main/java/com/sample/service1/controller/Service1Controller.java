package com.sample.service1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service1")
public class Service1Controller {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Service 1!";
    }
}
