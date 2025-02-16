package com.moviesforyou.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {
JwtService jwtService;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService();
  }

  @Test
  void shouldGenerateTokenUsingUserName() {
    String username = "username";
    assertThat(jwtService.generateToken(username)).isEqualTo("token for username");
  }
}