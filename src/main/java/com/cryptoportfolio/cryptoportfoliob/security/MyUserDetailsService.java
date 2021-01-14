package com.cryptoportfolio.cryptoportfoliob.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cryptoportfolio.cryptoportfoliob.entity.Portfolio;
import com.cryptoportfolio.cryptoportfoliob.repositories.PortfolioRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	PortfolioRepository portfolioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Portfolio user;
		if (portfolioRepository.findPortfolioByUsername(username).isEmpty()) {
			throw new UsernameNotFoundException("Not Found" + username);
		} else {
			user = portfolioRepository.findPortfolioByUsername(username).get(0);
		}

		return new MyUserDetails(user);

	}

}
