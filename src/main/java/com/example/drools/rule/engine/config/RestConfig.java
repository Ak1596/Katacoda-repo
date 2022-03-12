package com.example.drools.rule.engine.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RestConfig {

	@Bean("objectMapper")
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json().build()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@Bean
	public RestTemplateBuilder restTemmplateBuilder() {
		return new RestTemplateBuilder();
	}
	
}
