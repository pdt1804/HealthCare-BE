package com.example.demo.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.repositories.UserRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Bean
	public PasswordEncoder getPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public CustomUserDetailService getCustomDetailService() throws Exception 
	{
	    return new CustomUserDetailService();
	}
	
	@Bean
	public JwtAuthFilter getJwtAuthFilter() throws Exception
	{
		JwtAuthFilter jwtAuthFilter = new JwtAuthFilter();
		jwtAuthFilter.setCustomUserDetailService(getCustomDetailService());
		jwtAuthFilter.setJwtService(jwtService);
		
		return jwtAuthFilter;
	}
	
	@Bean
	public CustomAuthenticationProvider getAuthenticationProvider() throws Exception 
	{
		var provider = new CustomAuthenticationProvider();
		provider.setPasswordEncoder(getPasswordEncoder());
		provider.setUserRepository(userRepository);
	    return provider;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
	{
        return http.csrf().disable().cors().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/user/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationProvider(getAuthenticationProvider())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(getJwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
	}
	
	

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception 
    {
    	return config.getAuthenticationManager();
    }
}
