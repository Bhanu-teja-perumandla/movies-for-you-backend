package com.moviesforyou.service;

import com.moviesforyou.model.User;
import com.moviesforyou.model.UserPrincipal;
import com.moviesforyou.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

  UserService userService;
  UserRepository userRepository;
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    userService = new UserService(userRepository, bCryptPasswordEncoder);
  }

  @Test
  void shouldReturnUserDetailsFromUserName() {
    User user = mock(User.class);
    UserPrincipal expected = new UserPrincipal(user);
    when(userRepository.findByUsername("username")).thenReturn(user);
    UserDetails actual = userService.loadUserByUsername("username");

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void shouldSaveUser() {
    User user = mock(User.class);
    when(user.getPassword()).thenReturn("password");
    when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
    doNothing().when(user).setPassword("encodedPassword");
    when(userRepository.save(user)).thenReturn(user);
    User actual = userService.registerUser(user);
    assertThat(actual).isEqualTo(user);
    verify(userRepository).save(user);
    verify(bCryptPasswordEncoder).encode(user.getPassword());
    verify(user).setPassword("encodedPassword");
  }
}