package com.cryptoportfolio.cryptoportfoliob.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.cryptoportfolio.cryptoportfoliob.entity.Portfolio;

public interface PortfolioRepository extends MongoRepository<Portfolio, String> {
	@Query("{ 'username' : ?0 }")
	List<Portfolio> findPortfolioByUsername(String username);

	boolean existsByUsername(String username);

	List<Portfolio> findByOrderById();

}
