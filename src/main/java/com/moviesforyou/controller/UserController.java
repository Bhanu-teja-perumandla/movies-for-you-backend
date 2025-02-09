package com.moviesforyou.controller;

import com.moviesforyou.model.User;
import com.moviesforyou.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public String register(@RequestBody User user) {
    return userService.registerUser(user).getUsername();
  }
}
