package com.moviesforyou.controller;

import com.moviesforyou.model.LoginResponse;
import com.moviesforyou.model.RegisterResponse;
import com.moviesforyou.model.User;
import com.moviesforyou.service.JwtService;
import com.moviesforyou.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
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
    String username = "username";
    when(user.getUsername()).thenReturn(username);
    when(userService.findUser(username)).thenReturn(false);
    when(userService.registerUser(user)).thenReturn(user);
    assertThat(userController.register(user)).isEqualTo(ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse("User created: username")));
    verify(userService).findUser(username);
    verify(userService).registerUser(user);
  }

  @Test
  void shouldNotRegisterUserIfAlreadyExists() {
    User user = mock(User.class);
    String username = "username";
    when(user.getUsername()).thenReturn(username);
    when(userService.findUser(username)).thenReturn(true);
    assertThat(userController.register(user)).isEqualTo(ResponseEntity.status(HttpStatus.CONFLICT).body(new RegisterResponse("User already exists")));
    verify(userService).findUser(username);
  }

  @Test
  void shouldLoginAndReturnJwtTokenIfUserIsValid() {
    User user = mock(User.class);
    String username = "username";
    String password = "password";
    Authentication authentication = mock(Authentication.class);
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);
    LoginResponse expected = new LoginResponse("token", "login successful");

    when(user.getUsername()).thenReturn(username);
    when(user.getPassword()).thenReturn(password);
    when(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(jwtService.generateToken(username)).thenReturn("token");

    assertThat(userController.login(user)).isEqualTo(expected);
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

    assertThat( userController.login(user)).isEqualTo(new LoginResponse(null, "login failed :("));
    verify(authenticationManager).authenticate(authenticationToken);
    verify(authentication).isAuthenticated();
    verifyNoMoreInteractions(jwtService, authenticationManager, authentication);
  }
}