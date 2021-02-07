package com.cryptoportfolio.cryptoportfoliob.repositories;

import com.cryptoportfolio.cryptoportfoliob.entity.Book;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book,String>{	
	

}
