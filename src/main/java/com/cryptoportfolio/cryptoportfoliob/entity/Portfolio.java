package com.cryptoportfolio.cryptoportfoliob.entity;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cryptoportfolio.cryptoportfoliob.dto.UserRegistrationDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document(collection = "Portfolio")
public class Portfolio {
	@Id
	private String id;
	private String username;
	private String name;
	private String password;	
	private Map<String,String> coins;
	
	public Portfolio(UserRegistrationDTO user) {
		this.id = user.getId();
		this.name = user.getName();
		this.username= user.getUsername();
		this.password=user.getPassword();
	}

}
