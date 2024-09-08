package com.example.demo.springsecurity;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority, Serializable {

	private static final long serialVersionUID = 1L;
	private String roleName;
	
	@Override
	public String getAuthority() {
		return roleName;
	}

}
