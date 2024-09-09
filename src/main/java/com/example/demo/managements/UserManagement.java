package com.example.demo.managements;

import com.example.demo.entities.User;
import com.example.demo.responseEntities.Token;

public interface UserManagement {

	public Token login(String userName, String passWord);
	public Token reGenerateToken(String refreshToken);
	public Token createAccount(User user, String roleName);
	public int authenticateEmail(String email);
	public int getOTPByUsername(String userName);
	public void deleteAccount(String userName);
}
