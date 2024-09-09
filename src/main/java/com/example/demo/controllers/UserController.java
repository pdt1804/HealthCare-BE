package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.responseEntities.Token;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public ResponseEntity<Token> login(@RequestParam("userName") String userName, @RequestParam("passWord") String passWord)
	{
		return ResponseEntity.ok(userService.login(userName, passWord));
	}
	
	@PostMapping("/createAccount")
	public ResponseEntity<Token> login(@RequestBody User user, @RequestParam("roleName") String roleName)
	{
		return ResponseEntity.ok(userService.createAccount(user, roleName));
	}
	
	@GetMapping("/getAccessToken")
	public ResponseEntity<Token> getAccessToken(@RequestParam("refreshToken") String refreshToken)
	{
		return ResponseEntity.ok(userService.reGenerateToken(refreshToken));
	}
	
	@GetMapping("/authenticateEmail")
	public ResponseEntity<Integer> getOTP(@RequestParam("email") String email)
	{
		return ResponseEntity.ok(userService.authenticateEmail(email));
	}
	
	@GetMapping("/getOTPByUsername")
	public ResponseEntity<Integer> getOTPByUsername(@RequestParam("userName") String userName)
	{
		return ResponseEntity.ok(userService.getOTPByUsername(userName));
	}
}
