package com.moviesforyou.service;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
  public String generateToken(String username) {
    return "token for "+ username;
  }
}
