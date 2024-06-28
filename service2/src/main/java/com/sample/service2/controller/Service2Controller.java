package com.sample.service2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RequestMapping("/api/service2")
public class Service2Controller {

	@GetMapping("/hello")
	@RateLimiter(name = "rateLimiterApi")
	public String hello() {
		return "Hello from Service 2!";
	}
}
