package com.example.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.example.security.AccountCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Filter to handle login authentication
 */
@Slf4j
@Component
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	public JWTLoginFilter(JWTLoginFilterConfig config) {
		super(new AntPathRequestMatcher(config.getPath(), HttpMethod.POST.name()));
	}

	@Autowired
	@Override
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
		if (!MediaType.valueOf(httpServletRequest.getContentType()).isCompatibleWith(MediaType.APPLICATION_JSON)) {
			httpServletResponse.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "Expecting " + MediaType.APPLICATION_JSON);
			return null;
		}

		AccountCredentials credentials = objectMapper.readValue(httpServletRequest.getInputStream(),
				AccountCredentials.class);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getUsername(),
				credentials.getPassword());
		return getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		tokenAuthenticationService.addAuthentication(response, authResult);
		super.successfulAuthentication(request, response, chain, authResult);
	}
}
