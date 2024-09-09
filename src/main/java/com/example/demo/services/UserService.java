package com.example.demo.services;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.enums.Role;
import com.example.demo.exceptions.ExistedUsername;
import com.example.demo.exceptions.ExpiredRefreshToken;
import com.example.demo.managements.UserManagement;
import com.example.demo.repositories.UserRepository;
import com.example.demo.responseEntities.Token;
import com.example.demo.springsecurity.JwtService;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserManagement {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Token login(String userName, String passWord) 
	{
		var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, passWord));
		
		if (auth.isAuthenticated())
		{
			String bearerToken = jwtService.generateToken(userName);
			String refreshToken = jwtService.generateRefreshToken(userName);
			
			Token token = new Token(bearerToken, refreshToken);
			return token;
		}
		
		throw new UsernameNotFoundException("Invalid username !!!");
	}

	@Override
	public Token reGenerateToken(String refreshToken) 
	{
		Date expirationDate = jwtService.extractExpiration(refreshToken);
		if (expirationDate.after(new Date()))
		{
			String bearerToken = jwtService.generateToken(jwtService.extractUsername(refreshToken));
			return new Token(bearerToken, refreshToken);
		}
		
		throw new ExpiredRefreshToken("Expired refresh token. Please log in again !");
	}

	@Override
	@Transactional
	public Token createAccount(User user, String roleName) 
	{
		boolean isExisted = checkExistUser(user);

		if (isExisted == false)
		{
		    Role role = roleName.toString().equalsIgnoreCase("User") ? Role.User : Role.Hospital;
		    user.setRoleName(role);
			user.setDateCreated(new Date());
			user.setValid(true);
			user.setRoleName(role);
			user.setPassWord(passwordEncoder.encode(user.getPassWord()));
			
			userRepository.save(user);
			
			String bearerToken = jwtService.generateToken(user.getUserName());
			String refreshToken = jwtService.generateRefreshToken(user.getUserName());
			
			return new Token(bearerToken, refreshToken);
		}
		
		throw new ExistedUsername("Username has already existed");
	}

	private boolean checkExistUser(User user) 
	{
		for(var p : userRepository.findAll())
		{
			if (p.getUserName().equalsIgnoreCase(user.getUserName()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public int authenticateEmail(String email) 
	{
		int otp = generateOTP();
		SimpleMailMessage mail = getSimpleMailMessage(email, otp);
		javaMailSender.send(mail);
		return otp;
	}

	@Override
	public int getOTPByUsername(String userName) 
	{
		User user = userRepository.getById(userName);
		if (user != null && user.isValid() == true)
		{
			int otp = generateOTP();
			SimpleMailMessage mail = getSimpleMailMessage(user.getEmail(), otp);
			javaMailSender.send(mail);
			return otp;
		}
		
		throw new UsernameNotFoundException("Invalid username !!!");
	}
	
	@Override
	public void deleteAccount(String userName) 
	{
		User user = userRepository.getById(userName);
		if (user != null && user.isValid() == true)
		{
			user.setValid(false);
			userRepository.save(user);
		}
		
		throw new UsernameNotFoundException("Invalid username !!!");
	}
	
	private int generateOTP()
	{
		Random rd = new Random();
		return rd.nextInt(100000, 999999);
	}
	
	private SimpleMailMessage getSimpleMailMessage(String email, int otp)
	{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("phamduythong600@gmail.com");
		mail.setTo(email);
		mail.setSubject("OTP Code");
		mail.setText("Your OTP code is " + otp);
		return mail;
	}
}
