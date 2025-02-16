package com.moviesforyou.config;

import com.moviesforyou.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorization = request.getHeader("Authorization");
    String token = null;
    String username = null;

    if(authorization!=null && authorization.startsWith("Bearer ")) {
      token = authorization.substring(7);
      username = jwtService.extractUserName(token);
    }

    if(token!= null && username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {

      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      if(jwtService.verifyToken(token, userDetails)) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
