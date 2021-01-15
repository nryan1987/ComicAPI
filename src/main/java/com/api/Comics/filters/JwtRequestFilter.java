package com.api.Comics.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.Comics.service.ComicUserDetailsService;
import com.api.Comics.util.JwtUtil;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private ComicUserDetailsService udService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		
		String username = null, token = null;
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			//String[] bearerToken = authHeader.split(" ");
			token = authHeader.split(" ")[1];
			username = jwtUtil.extractUsername(token);
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails ud = this.udService.loadUserByUsername(username);
			if(jwtUtil.validateToken(token, ud)) {
				UsernamePasswordAuthenticationToken uNamePwdAuthToken = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
				uNamePwdAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(uNamePwdAuthToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}
