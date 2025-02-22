package com.moviesforyou.controller;

import com.moviesforyou.model.LoginResponse;
import com.moviesforyou.model.RegisterResponse;
import com.moviesforyou.model.User;
import com.moviesforyou.service.JwtService;
import com.moviesforyou.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  private final UserService userService;
  private final JwtService jwtService;
  private AuthenticationManager authenticationManager;

  public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody User user) {
    if(userService.findUser(user.getUsername())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new RegisterResponse("User already exists"));
    }
    User created = userService.registerUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse("User created: " + created.getUsername()));
  }

  @PostMapping("/login")
  public LoginResponse login(@RequestBody User user) {
    System.out.println(user + " is trying to log in ------------------");
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    if(authentication.isAuthenticated()) {
      return new LoginResponse(jwtService.generateToken(user.getUsername()), "login successful");
    }
    return new LoginResponse(null, "login failed :(");
  }
}
