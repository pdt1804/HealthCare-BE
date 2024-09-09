package com.example.demo.exceptions;

public class ExistedUsername extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExistedUsername(String msg)
	{
		super(msg);
	}
}
