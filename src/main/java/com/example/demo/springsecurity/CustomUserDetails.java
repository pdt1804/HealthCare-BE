package com.example.demo.springsecurity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entities.User;

public class CustomUserDetails implements UserDetails {

	private String userName;
	private String passWord;
	private List<GrantedAuthority> roles;
	
	public CustomUserDetails(User user)
	{
		this.userName = user.getUserName();
		this.passWord = user.getPassWord();
		
		roles.add(new CustomGrantedAuthority(user.getRoleName()));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return passWord;
	}

	@Override
	public String getUsername() {
		return userName;
	}

}
