package com.roken.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.roken.demo.model.JwtRequest;
import com.roken.demo.model.JwtResponse;

@RestController
public class HomeController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/")
	public String home() {
		return "JWT DEMO PROJECT";
	}
	
	@PostMapping("/authenticate")
	public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception
	{
		try
		{
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
					(jwtRequest.getUserName(), jwtRequest.getPassword()));
					
		}
		catch(BadCredentialsException e)
		{
			throw new Exception("INVAL;ID CRED",e);	
		}
		return null;
		
	}
}
