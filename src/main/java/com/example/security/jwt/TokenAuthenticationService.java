package com.example.security.jwt;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.security.AuthenticatedUser;
import com.fasterxml.jackson.databind.util.ISO8601Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ConfigurationProperties("jwt.auth.service")
@Slf4j
public class TokenAuthenticationService {
	private Duration tokenTimeToLive = Duration.ofMinutes(15);

	@Setter
	private String secret = UUID.randomUUID().toString();

	@Setter
	private String tokenPrefix = "Bearer";

	/**
	 * Custom String setter to allow properties file config conversion to
	 * Duration object
	 * 
	 * @param durationStr
	 */
	public void setTokenTimeToLive(String tokenTimeToLive) {
		log.info("Parsing token TTL string : '{}'", tokenTimeToLive);
		this.tokenTimeToLive = Duration.parse(tokenTimeToLive);
	}

	/**
	 * Add JWT token auth header to response
	 * 
	 * @param response
	 * @param authentication
	 */
	public void addAuthentication(HttpServletResponse response, Authentication authentication) {
		// extract granted auths to a string list
		List<String> roleStrs = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		// create a new JWT token to include the roles (so we can avoid querying
		// the Auth Manager for each request)
		Date expiration = new Date(System.currentTimeMillis() + tokenTimeToLive.toMillis());
		String token = Jwts.builder().claim("roles", roleStrs).setSubject(authentication.getName())
				.setExpiration(expiration).signWith(SignatureAlgorithm.HS512, secret).compact();
		log.debug("Created JWT for user {} : {}", authentication, token);

		response.addHeader(HttpHeaders.AUTHORIZATION, tokenPrefix + " " + token);
		addAuthExpirationHeader(response, expiration);
	}

	/**
	 * Extract JWT token from request
	 * 
	 * @param request
	 * @return
	 */
	public AuthenticatedUser getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (token != null && token.startsWith(tokenPrefix)) {
			// parse the token.
			log.trace("Extracted JWT token: {}", token);

			Claims claims = Jwts.parser().setSigningKey(secret)
					.parseClaimsJws(token.substring(tokenPrefix.length()).trim()).getBody();

			log.trace("Extracted JWT user: {}", claims.getSubject());

			@SuppressWarnings("unchecked")
			List<String> roleStrs = Optional.ofNullable(claims.get("roles", List.class)).orElseGet(ArrayList::new);
			log.trace("{} roles: {}", claims.getSubject(), roleStrs);
			List<SimpleGrantedAuthority> roles = roleStrs.stream().map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			addAuthExpirationHeader(response, claims.getExpiration());

			return new AuthenticatedUser(claims.getSubject(), true, roles, claims.getExpiration());
		}

		// default is to return null (not authenticated)
		return null;
	}

	/**
	 * Add auth expiration header, to let user know when their
	 * 
	 * @param response
	 * @param authentication
	 */
	public void addAuthExpirationHeader(HttpServletResponse response, Date expiration) {
		response.addHeader(HttpHeaders.AUTHORIZATION + "-Expires", ISO8601Utils.format(expiration));
	}
}
