package com.cryptoportfolio.cryptoportfoliob.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document(collection = "Book")
public class Book {

	@Id
	private String id;
	Integer latestid;
	
}
