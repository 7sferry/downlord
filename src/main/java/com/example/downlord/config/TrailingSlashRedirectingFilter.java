package com.example.downlord.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/************************
 * Made by [MR Ferry™]  *
 * on Desember 2022     *
 ************************/

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TrailingSlashRedirectingFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
		String fullURL = ServletUriComponentsBuilder
				.fromRequest(request)
				.build()
				.toString();
		String uri = request.getRequestURI();
		if (fullURL.endsWith("/") && !uri.equals("/")) {
			fullURL = fullURL.substring(0, fullURL.length() - 1);
			response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
			response.setHeader(HttpHeaders.LOCATION, fullURL);
		} else if(uri.isEmpty()){
			response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
			response.setHeader(HttpHeaders.LOCATION, fullURL + '/');
		} else {
			filterChain.doFilter(request, response);
		}
	}
}
