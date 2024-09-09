package com.example.demo.entities;

import java.util.Date;

import com.example.demo.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	private String userName;
	private String passWord;
	private String email;
	private Role roleName;
	private Date dateCreated;
	private boolean isValid;
	
}
