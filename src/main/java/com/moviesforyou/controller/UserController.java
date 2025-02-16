package com.moviesforyou.controller;

import com.moviesforyou.model.User;
import com.moviesforyou.service.JwtService;
import com.moviesforyou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
  public String register(@RequestBody User user) {
    return userService.registerUser(user).getUsername();
  }

  @PostMapping("/login")
  public String login(@RequestBody User user) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    if(authentication.isAuthenticated()) {
      return jwtService.generateToken(user.getUsername());
    }
    return "Login Failed :(";
  }
}
