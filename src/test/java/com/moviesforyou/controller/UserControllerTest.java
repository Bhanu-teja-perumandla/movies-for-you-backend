package com.moviesforyou.controller;

import com.moviesforyou.model.User;
import com.moviesforyou.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {
UserController userController;
UserService userService;

  @BeforeEach
  void setUp() {
    userService = mock(UserService.class);
    userController = new UserController(userService);
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
}