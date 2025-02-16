package com.moviesforyou.controller;

import com.moviesforyou.model.User;
import com.moviesforyou.service.JwtService;
import com.moviesforyou.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class UserControllerTest {
UserController userController;
UserService userService;
JwtService jwtService;
AuthenticationManager authenticationManager;

  @BeforeEach
  void setUp() {
    userService = mock(UserService.class);
    jwtService = mock(JwtService.class);
    authenticationManager = mock(AuthenticationManager.class);
    userController = new UserController(userService, jwtService, authenticationManager);
  }

  @Test
  void shouldRegisterUser() {
    User user = mock(User.class);
    when(user.getUsername()).thenReturn("username");
    when(userService.registerUser(user)).thenReturn(user);
    String actual = userController.register(user);
    assertThat(actual).isEqualTo(user.getUsername());
    verify(userService).registerUser(user);
  }

  @Test
  void shouldLoginAndReturnJwtTokenIfUserIsValid() {
    User user = mock(User.class);
    String username = "username";
    String password = "password";
    Authentication authentication = mock(Authentication.class);
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);

    when(user.getUsername()).thenReturn(username);
    when(user.getPassword()).thenReturn(password);
    when(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(jwtService.generateToken(username)).thenReturn("token");

    String actual = userController.login(user);
    assertThat(actual).isEqualTo("token");
    verify(authenticationManager).authenticate(authenticationToken);
    verify(authentication).isAuthenticated();
    verify(jwtService).generateToken(username);
  }

  @Test
  void shouldReturnLoginFailedWhenUserIsInvalid() {
    User user = mock(User.class);
    String username = "username";
    String password = "password";
    Authentication authentication = mock(Authentication.class);
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);

    when(user.getUsername()).thenReturn(username);
    when(user.getPassword()).thenReturn(password);
    when(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(false);

    String actual = userController.login(user);
    assertThat(actual).isEqualTo("Login Failed :(");
    verify(authenticationManager).authenticate(authenticationToken);
    verify(authentication).isAuthenticated();
    verifyNoMoreInteractions(jwtService, authenticationManager, authentication);
  }
}