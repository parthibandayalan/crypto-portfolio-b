package com.cryptoportfolio.cryptoportfoliob.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cryptoportfolio.cryptoportfoliob.entity.Portfolio;
import com.cryptoportfolio.cryptoportfoliob.repositories.PortfolioRepository;

@Service
public class PortfolioService {

	@Autowired
	private PortfolioRepository portfolioRepository;

	public Collection<Portfolio> getPortfolios() {
		return portfolioRepository.findAll();
	}

	String portfolioIdGenerator() {
		List<Portfolio> listPortfolio = portfolioRepository.findByOrderById();
		if (listPortfolio.isEmpty()) {
			return "PF001";
		} else {
			return "PF" + Integer.toString((Integer.parseInt(listPortfolio.get(0).getId().substring(2)) + 1));
		}

	}

	public ResponseEntity<String> createPortfolio(Portfolio portfolio) {

		// portfolioRepository.findById(id)
		portfolio.setId(portfolioIdGenerator());
		if (portfolioRepository.existsByUsername(portfolio.getUsername())) {
			return ResponseEntity.badRequest().body("Username is taken");
		} else {
			portfolioRepository.insert(portfolio);
			return ResponseEntity.ok("User Added and Portfolio Created");
		}
	}

	public ResponseEntity<Object> getPortfolioByUsername(String username) {
		if (portfolioRepository.existsByUsername(username)) {
			return ResponseEntity.ok(portfolioRepository.findPortfolioByUsername(username).get(0));
		} else {
			return ResponseEntity.badRequest().body("User doesnt exist");
		}
	}

	public Optional<Portfolio> deletePortfolioById(String id) {
		Optional<Portfolio> portfolio = portfolioRepository.findById(id);
		portfolio.ifPresent(p -> portfolioRepository.delete(p));
		return portfolio;
	}

	public ResponseEntity<String> updatePortfolio(String column, String value, String username) {
		if (!portfolioRepository.existsByUsername(username)) {
			return ResponseEntity.badRequest().body("Portfolio is not found");
		} else {
			Portfolio portfolio = portfolioRepository.findPortfolioByUsername(username).get(0);
			switch (column) {
			case "username":
				return ResponseEntity.badRequest().body("Username Cannot be updated");
			case "password":
				portfolio.setPassword(String.valueOf(value));
				portfolioRepository.save(portfolio);
				break;
			case "name":
				portfolio.setName(String.valueOf(value));
				portfolioRepository.save(portfolio);
				break;
			}
			return ResponseEntity.ok("Update Done");
		}

	}
	
	public ResponseEntity<String> addCoin(String coin, String tokens, String username) {
		if (!portfolioRepository.existsByUsername(username)) {
			return ResponseEntity.badRequest().body("Portfolio is not found");
		} else {
			Portfolio portfolio = portfolioRepository.findPortfolioByUsername(username).get(0);
			Map<String,String> coins;
			if(portfolio.getCoins() != null) {
				coins = portfolio.getCoins();
				tokens = Integer.toString((Integer.parseInt(coins.getOrDefault(coin, "0")) + Integer.parseInt(tokens)));
				coins.put(coin,tokens);
			} else {
				coins = new HashMap<String,String>();
				coins.put(coin,tokens);
			}
			portfolio.setCoins(coins);
			portfolioRepository.save(portfolio);
			return ResponseEntity.ok("Coin Added");
		}

	}	
	
	public ResponseEntity<String> removeCoin(String coin,String username){
		if (!portfolioRepository.existsByUsername(username)) {
			return ResponseEntity.badRequest().body("Portfolio is not found");
		} else {
			Portfolio portfolio = portfolioRepository.findPortfolioByUsername(username).get(0);
			Map<String,String> coins = portfolio.getCoins();
			coins.remove(coin);
			portfolio.setCoins(coins);
			portfolioRepository.save(portfolio);			
			return ResponseEntity.ok("Coin Removed");
		}
	}

}
