package com.moviesforyou.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
  private final Clock clock;
  private final KeyGenerator keyGenerator;

  public JwtService(Clock clock, KeyGenerator keyGenerator) {
    this.clock = clock;
    this.keyGenerator = keyGenerator;
  }

  public String generateToken(String username) {
    Instant instant = clock.instant();
    return Jwts
        .builder()
        .subject(username)
        .issuedAt(Date.from(instant))
        .expiration(Date.from(instant.plus(60, ChronoUnit.MINUTES)))
        .signWith(keyGenerator.generateKey())
        .compact();
  }
}
