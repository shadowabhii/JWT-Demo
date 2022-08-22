package com.roken.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.roken.demo.service.UserService;
import com.roken.demo.utility.JWTUtility;

public class JWTFilter extends OncePerRequestFilter {
	
	@Autowired
	private JWTUtility jwtUtility;
	
	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest HttpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorization = HttpServletRequest.getHeader("Authorization");
		String token = null;
		String userName = null;
		
		if(null!=authorization && authorization.startsWith("Bearer "))
		{
			token = authorization.substring(7);
			userName = jwtUtility.getUsernameFromToken(token);
		}
		// TODO Auto-generated method stub
		
		if(null!=userName && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UserDetails userDetails = userService.loadUserByUsername(userName);
			
		if(jwtUtility.validateToken(token, userDetails))
		{
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
					new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
			
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(HttpServletRequest));
			
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
		
		filterChain.doFilter(HttpServletRequest, httpServletResponse);
			
		}
		
	}

}
