package com.sample.service1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;

@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler({ RequestNotPermitted.class })
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public void handleRequestNotPermitted() {
	}

}
