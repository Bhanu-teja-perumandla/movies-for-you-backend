package com.moviesforyou.config;

import com.moviesforyou.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class JwtFilterTest {
  JwtFilter jwtFilter;
  JwtService jwtService;
  UserDetailsService userDetailsService;
  SecurityContext securityContext;

  @BeforeEach
  void setUp() {
    jwtService = mock(JwtService.class);
    userDetailsService = mock(UserDetailsService.class);
    securityContext = mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    jwtFilter = new JwtFilter(jwtService, userDetailsService);
  }

  @Test
  void shouldVerifyAndFilterTokenIfValid() throws ServletException, IOException {
    String token = "token";
    String username = "username";
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn(username);
    when(userDetails.getPassword()).thenReturn("password");
    when(userDetails.getAuthorities()).thenReturn(List.of());
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    when(request.getHeader("Authorization")).thenReturn("Bearer token");
    doNothing().when(filterChain).doFilter(request, response);
    when(jwtService.extractUserName(token)).thenReturn(username);
    when(securityContext.getAuthentication()).thenReturn(null);
    ArgumentCaptor<Authentication> authenticationArgumentCaptor = ArgumentCaptor.forClass(Authentication.class);
    doNothing().when(securityContext).setAuthentication(authenticationArgumentCaptor.capture());
    when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
    when(jwtService.verifyToken(token, userDetails)).thenReturn(true);

    jwtFilter.doFilterInternal(request, response, filterChain);
    assertThat(authenticationArgumentCaptor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(new UsernamePasswordAuthenticationToken(username, "password", List.of()));
    verify(request).getHeader("Authorization");
    verify(jwtService).extractUserName(token);
    verify(securityContext).getAuthentication();
    verify(userDetailsService).loadUserByUsername(username);
    verify(securityContext).setAuthentication(authenticationArgumentCaptor.getValue());
    verify(jwtService).verifyToken(token, userDetails);
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldVerifyAndFilterTokenNotPresent() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    when(request.getHeader("Authorization")).thenReturn(null);
    doNothing().when(filterChain).doFilter(request, response);

    jwtFilter.doFilterInternal(request, response, filterChain);

    verify(request).getHeader("Authorization");
    verify(filterChain).doFilter(request, response);
    verifyNoMoreInteractions(jwtService, securityContext, userDetailsService);
  }

  @Test
  void shouldVerifyAndFilterTokenIsNotBearer() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    when(request.getHeader("Authorization")).thenReturn("token");
    doNothing().when(filterChain).doFilter(request, response);

    jwtFilter.doFilterInternal(request, response, filterChain);

    verify(request).getHeader("Authorization");
    verify(filterChain).doFilter(request, response);
    verifyNoMoreInteractions(jwtService, securityContext, userDetailsService);
  }

  @Test
  void shouldVerifyAndFilterTokenHasNoSubject() throws ServletException, IOException {
    String token = "token";
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    when(request.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtService.extractUserName(token)).thenReturn(null);
    doNothing().when(filterChain).doFilter(request, response);

    jwtFilter.doFilterInternal(request, response, filterChain);

    verify(request).getHeader("Authorization");
    verify(filterChain).doFilter(request, response);
    verify(jwtService).extractUserName(token);
    verifyNoMoreInteractions(jwtService, securityContext, userDetailsService);
  }

  @Test
  void shouldVerifyAndFilterWhenAuthenticationIsAlreadyDone() throws ServletException, IOException {
    String token = "token";
    String username = "username";
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    when(request.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtService.extractUserName(token)).thenReturn(username);
    when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
    doNothing().when(filterChain).doFilter(request, response);

    jwtFilter.doFilterInternal(request, response, filterChain);

    verify(request).getHeader("Authorization");
    verify(filterChain).doFilter(request, response);
    verify(jwtService).extractUserName(token);
    verify(securityContext).getAuthentication();
    verifyNoMoreInteractions(jwtService, securityContext, userDetailsService);
  }

  @Test
  void shouldVerifyAndFilterWhenTokenIsNotValidOrExpired() throws ServletException, IOException {
    String token = "token";
    String username = "username";
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    when(request.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtService.extractUserName(token)).thenReturn(username);
    when(securityContext.getAuthentication()).thenReturn(null);
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
    when(jwtService.verifyToken(token, userDetails)).thenReturn(false);
    doNothing().when(filterChain).doFilter(request, response);

    jwtFilter.doFilterInternal(request, response, filterChain);

    verify(request).getHeader("Authorization");
    verify(filterChain).doFilter(request, response);
    verify(jwtService).extractUserName(token);
    verify(securityContext).getAuthentication();
    verify(userDetailsService).loadUserByUsername(username);
    verify(jwtService).verifyToken(token, userDetails);
    verifyNoMoreInteractions(jwtService, securityContext, userDetailsService);
  }
}