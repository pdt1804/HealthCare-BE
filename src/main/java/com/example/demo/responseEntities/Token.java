package com.example.demo.responseEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {

	private String bearerToken;
	private String refreshToken;
	
}
