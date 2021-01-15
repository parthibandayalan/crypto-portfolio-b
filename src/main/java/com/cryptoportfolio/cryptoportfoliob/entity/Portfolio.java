package com.cryptoportfolio.cryptoportfoliob.entity;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

}
