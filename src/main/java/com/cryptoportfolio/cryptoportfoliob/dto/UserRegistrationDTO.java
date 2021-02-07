package com.cryptoportfolio.cryptoportfoliob.dto;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegistrationDTO {
	
	private String username;
	private String name;
	private String password;
	private String passwordConfirmation;
	
}
