package com.example.demo.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;

public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		User user = userRepository.getById(username);
		if (user != null)
		{
			return new CustomUserDetails(user);
		}
		
		throw new UsernameNotFoundException("Invalid username !!!");
	}

}
