package com.example.security.jwt;

import javax.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Component
@ConfigurationProperties("jwt.login.filter")
@Data
@Validated
public class JWTLoginFilterConfig {
	@Pattern(regexp = "/.*")
	String path = "/login";
}
