package com.cryptoportfolio.cryptoportfoliob.security;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

//		logger.info("Inside Filter " + request.getMethod().toString());
		
		final String authorizationHeader = request.getHeader("Authorization") != null
				? request.getHeader("Authorization").toString()
				: "null";
//		logger.info("Authorization : " + authorizationHeader.toString());

		final String testHeader = request.getHeader("TestHeader") != null ? request.getHeader("TestHeader").toString()
				: "null";
//		logger.info("TestHeader : " + testHeader.toString());

//		logger.info("Headers : ");
//		Enumeration<String> headerNames = request.getHeaderNames();
//		while (headerNames.hasMoreElements()) {
//			String headerName = headerNames.nextElement();
//			String headerValue = request.getHeader(headerName);
//			logger.info(headerName + " : " + headerValue);
//		}

		String username = null;
		String jwt = null;
		Cookie cookieJwt = null;

		Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
		// logger.info("Cookie read : " + cookie.getValue().toString());
//		logger.info("Inside Filter");

		if (WebUtils.getCookie(request, "jwt") != null) {
			cookieJwt = WebUtils.getCookie(request, "jwt");
			jwt = cookieJwt.getValue();
			username = jwtUtil.extractUsername(jwt);
//			logger.info("Cookie Found " + jwt.toString());
		} else {
//			logger.info("Cookie not found");
		}

		/*
		 * if (authorizationHeader != null && authorizationHeader.startsWith("Bearer"))
		 * { jwt = authorizationHeader.substring(7); username =
		 * jwtUtil.extractUsername(jwt); }
		 */
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);
			if (jwtUtil.validateToken(jwt, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}
