package com.example.demo.exceptions;

public class ExpiredRefreshToken extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExpiredRefreshToken(String msg)
	{
		super(msg);
	}
}
