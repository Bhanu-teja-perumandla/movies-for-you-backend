package com.moviesforyou.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class JwtService {
  private final Clock clock;
  private final SecretKey secretKey;
  Logger logger = Logger.getLogger("JwtService");

  public JwtService(Clock clock, KeyGenerator keyGenerator) {
    this.clock = clock;
    this.secretKey = keyGenerator.generateKey();
  }

  public String generateToken(String username) {
    Instant instant = clock.instant();
    return Jwts
        .builder()
        .subject(username)
        .issuedAt(Date.from(instant))
        .expiration(Date.from(instant.plus(60, ChronoUnit.MINUTES)))
        .signWith(secretKey)
        .compact();
  }

  public String extractUserName(String token) {
    try {
      return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
    } catch (Exception e) {
      logger.log(Level.WARNING, "Exception while parsing token: " + e.getMessage());
    }
    return null;
  }

  public boolean verifyToken(String token, UserDetails userDetails) {
    try {
      Claims payload = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
      return payload.getSubject().equals(userDetails.getUsername()) && payload.getExpiration().toInstant().isAfter(clock.instant());
    } catch (Exception e) {
      logger.log(Level.WARNING, "Exception while parsing token: " + e.getMessage());
    }
    return false;
  }
}
