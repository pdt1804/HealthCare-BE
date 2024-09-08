package com.example.demo.springsecurity;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.repositories.UserRepository;

import lombok.Data;

@Data
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = authentication.getName();
		String passWord = authentication.getCredentials().toString();
		
		var existingUser = userRepository.getById(userName);
		
		if (existingUser != null)
		{
			var authUser = new CustomUserDetails(existingUser);
			if (passwordEncoder.matches(passWord, authUser.getPassword()))
			{
				return new UsernamePasswordAuthenticationToken(authUser, authUser.getPassword(), authUser.getAuthorities());
			}
		}
		
		 throw new BadCredentialsException("Invalid username or password");
	}

	@Override
	public boolean supports(Class<?> authentication) {
	    return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
