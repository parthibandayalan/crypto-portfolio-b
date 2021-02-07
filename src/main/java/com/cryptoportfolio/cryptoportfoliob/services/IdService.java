package com.cryptoportfolio.cryptoportfoliob.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cryptoportfolio.cryptoportfoliob.repositories.BookRepository;
import com.cryptoportfolio.cryptoportfoliob.entity.Book;

@Service
public class IdService {

	@Autowired
	BookRepository bookRepository;

	String getNewId(){
		
		
		Book book = bookRepository.findById("1").get();
		Integer id = book.getLatestid() +1;
		String newId = "PF"+id;
		
		book.setLatestid(id);
		
		bookRepository.save(book);
		
		return newId;
		
	}

}
