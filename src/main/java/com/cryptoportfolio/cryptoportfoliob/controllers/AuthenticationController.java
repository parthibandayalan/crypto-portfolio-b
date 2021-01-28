package com.cryptoportfolio.cryptoportfoliob.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.cryptoportfolio.cryptoportfoliob.security.AuthenticationRequest;

import com.cryptoportfolio.cryptoportfoliob.security.JwtUtil;
import com.cryptoportfolio.cryptoportfoliob.security.MyUserDetailsService;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AuthenticationController {

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	MyUserDetailsService userDetailsService;
	@Autowired
	JwtUtil jwtTokenUtil;

	@PostMapping("/authenticate")
	public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
			HttpServletResponse res) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
		
//		Cookie cookie = new Cookie("jwt", jwt);
//		cookie.setMaxAge(30*60);
//		cookie.setSecure(false);//----------> need to set this as false for localhost connections. set as true for https connections 
//		cookie.setPath("/");
//		//cookie.setDomain("localhost");
//		cookie.setHttpOnly(true);
		
		ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
	            .maxAge(30*60)	            
	            .sameSite("None")
	            .secure(false)
	            .path("/")
	            .build();
		
	 
		
		res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		logger.info("Inside Authentication. Cookie added.");
		
		//res.addCookie(cookie);
		
		res.setStatus(HttpServletResponse.SC_OK);
		res.getWriter().write("jwt : "+jwt.toString());
		res.getWriter().flush();
		
		//////////////////
//		Cookie cookieNew = new Cookie("Bearer", jwt);
//		cookieNew.setMaxAge(30*60);
//		cookieNew.setSecure(false);//----------> need to set this as false for localhost connections. set as true for https connections 
//		cookieNew.setPath("/");
//		//cookieNew.setDomain("localhost");		
//		res.addCookie(cookieNew);
		//////////////////

		return;
	}
	
	@PostMapping("/refreshtoken")
	public void refreshToken(HttpServletRequest request,HttpServletResponse res) {
		
		//final String authorizationHeader = request.getHeader("Authorization");
		
		String username = null;
		String jwt = null;
		Cookie cookieJwt = null;
		
		
		
		if(WebUtils.getCookie(request, "jwt") != null ) {
			cookieJwt = WebUtils.getCookie(request, "jwt");
			jwt = cookieJwt.getValue();
			username = jwtTokenUtil.extractUsername(jwt);
		}
		
		Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
		logger.info("Cookie read : " + cookieJwt.getValue().toString() + "Username : "+username);
		
		if (username != null && jwtTokenUtil.canTokenBeRefreshed(jwt)) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			logger.info("username received"+userDetails.getUsername());
			if (jwtTokenUtil.validateToken(jwt, userDetails)) {

				String refreshedToken = jwtTokenUtil.refreshToken(jwt);
				
				Cookie cookie = new Cookie("jwt", refreshedToken);
				cookie.setMaxAge(5*60);
				cookie.setSecure(false);
				cookie.setPath("/");
				cookie.setHttpOnly(true);
				
				logger.info("Cookie refreshed : " + cookie.toString());

				res.addCookie(cookie);
				
				return;
				/*UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);*/
			} else {
				logger.info("Token Invalid");
			}
		}
	}
	
	@PostMapping("/canceltoken")
	public void logoutToken(HttpServletRequest request,HttpServletResponse res) {
		String username = null;
		String jwt = null;
		Cookie cookieJwt = null;
		
		if(WebUtils.getCookie(request, "jwt") != null ) {
			cookieJwt = WebUtils.getCookie(request, "jwt");
			jwt = cookieJwt.getValue();
			username = jwtTokenUtil.extractUsername(jwt);
		}
		
		Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
		logger.info("Cookie read : " + cookieJwt.getValue().toString() + "Username : "+username);
		
		if (username != null && jwtTokenUtil.canTokenBeRefreshed(jwt)) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
			if (jwtTokenUtil.validateToken(jwt, userDetails)) {

				String refreshedToken = jwtTokenUtil.refreshToken(jwt);
				
				Cookie cookie = new Cookie("jwt", refreshedToken);
				cookie.setMaxAge(0);
				cookie.setSecure(false);
				cookie.setPath("/");
				cookie.setHttpOnly(true);
				
				//logger.info("Cookie refreshed : " + cookie.toString());

				res.addCookie(cookie);
				
				return;
				/*UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);*/
			} else {
				logger.info("Token Invalid");
			}
		}
		
		
	}

}
