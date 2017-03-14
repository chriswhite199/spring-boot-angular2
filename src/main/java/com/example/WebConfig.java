package com.example;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class WebConfig {
	@Bean
	ErrorViewResolver supportPathBasedLocationStrategy() {
		return new ErrorViewResolver() {
			@Override
			public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status,
					Map<String, Object> model) {
				return status == HttpStatus.NOT_FOUND && !request.getRequestURI().startsWith("/api/")
						? new ModelAndView("index.html", Collections.emptyMap(), HttpStatus.OK) : null;
			}
		};
	}
}
