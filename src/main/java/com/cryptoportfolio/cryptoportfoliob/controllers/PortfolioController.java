package com.cryptoportfolio.cryptoportfoliob.controllers;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cryptoportfolio.cryptoportfoliob.dto.UserRegistrationDTO;
import com.cryptoportfolio.cryptoportfoliob.entity.Portfolio;
import com.cryptoportfolio.cryptoportfoliob.mapper.PortfolioMapper;
import com.cryptoportfolio.cryptoportfoliob.services.PortfolioService;

@RestController
public class PortfolioController {

	@Autowired
	PortfolioService portfolioService;
	@Autowired
	PortfolioMapper portfolioMapper;

	@GetMapping("portfolios")
	public Collection<Portfolio> getHelloWorld() {
		return portfolioService.getPortfolios();
	}

	@GetMapping("/portfolio/{username}")
	public ResponseEntity<Object> getPortfolioById(@PathVariable("username") String username) {
		return portfolioService.getPortfolioByUsername(username);
	}
	
	@PostMapping("/coins/add")
	public ResponseEntity<String> addCoins(@RequestBody Map<String, Object> payload){
		String coin = payload.get("coin").toString();
		String tokens = payload.get("tokens").toString();
		String username = payload.get("username").toString();
		return portfolioService.addCoin(coin,tokens,username);
	}
	
	@PostMapping("/coins/remove")
	public ResponseEntity<String> removeCoins(@RequestBody Map<String, Object> payload){
		String coin = payload.get("coin").toString();		
		String username = payload.get("username").toString();
		return portfolioService.removeCoin(coin,username);
	}

	@PostMapping("/portfolio/create")
	public ResponseEntity<String> insertPortfolio(@RequestBody UserRegistrationDTO user) {

		if (user.getPassword().equals(user.getPasswordConfirmation())) {
			Portfolio portfolio = portfolioMapper.toEntity(user);
			return portfolioService.createPortfolio(portfolio);
		} else {
			return ResponseEntity.badRequest().body("Password dont match");
		}

	}

	@DeleteMapping("/portfolio/delete/{id}")
	public Optional<Portfolio> deletePortfolioById(@PathVariable("id") String id) {
		return portfolioService.deletePortfolioById(id);
	}

	@PutMapping("/portfolio/update")
	ResponseEntity<String> updatePortfolio(@RequestBody Map<String, Object> payload) {
		// logger.info(payload.toString());
		String column = payload.get("column").toString();
		String value = payload.get("value").toString();
		String id = payload.get("id").toString();
		return portfolioService.updatePortfolio(column, value, id);
	}

}
