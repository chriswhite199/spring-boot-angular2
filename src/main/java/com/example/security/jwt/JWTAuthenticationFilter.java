package com.example.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.example.security.AuthenticatedUser;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Extract Authentication from JWT token Auth header
 */
@Component
@Slf4j
public class JWTAuthenticationFilter extends GenericFilterBean {
	@Autowired
	@Setter
	private TokenAuthenticationService tokenAuthService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			AuthenticatedUser authentication = tokenAuthService.getAuthentication((HttpServletRequest) request,
					(HttpServletResponse) response);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		} catch (ExpiredJwtException expiredEx) {
			String msg = "JWT token has expired, re-auth and try again";
			((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(), msg);
		} catch (JwtException ex) {
			String sourceIp = Optional.ofNullable(((HttpServletRequest) request).getHeader("X-FORWARD-FOR"))
					.orElse(request.getRemoteAddr());
			log.error("Error while parsing JWT from {}", sourceIp, ex);

			String msg = "Error with provided JWT, re-auth and try again";
			((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(), msg);
		}
	}

}
