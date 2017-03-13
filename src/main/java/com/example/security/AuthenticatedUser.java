package com.example.security;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticatedUser implements Authentication {
	private static final long serialVersionUID = 1L;

	private String name;
	private boolean authenticated;
	private Collection<? extends GrantedAuthority> authorities;
	private Date expires;

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return name;
	}
}
