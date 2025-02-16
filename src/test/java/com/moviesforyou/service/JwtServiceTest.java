package com.moviesforyou.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {
  JwtService jwtService;
  Clock clock;
  KeyGenerator keyGenerator;
  SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("dGhpcyBpcyBhIHZlcnkgdmVyeSBsb25nIHNlY3JldCBrZXksIEltIHR5cmluZyB0byBiZSB2ZXJ5IHZlcnkgdmVyeSBsb25nIHNvIHRoYXQgdGhlIGp3dCBkb2VzIG5vdCB0aHJvdyBhbnkgZXJyb3I="));
  Instant instant = Instant.now();

  @BeforeEach
  void setUp() {
    clock = mock(Clock.class);
    when(clock.instant()).thenReturn(instant);
    keyGenerator = mock(KeyGenerator.class);
    when(keyGenerator.generateKey()).thenReturn(key);
    jwtService = new JwtService(clock, keyGenerator);
  }

  @Test
  void shouldGenerateTokenUsingUserName() {
    String username = "username";
    String actual = jwtService.generateToken(username);
    JwtParser jwtParser = Jwts.parser()
        .verifyWith(key)
        .build();
    Claims payload = jwtParser.parseSignedClaims(actual).getPayload();

    assertThat(payload.getExpiration()).isEqualTo(instant.plus(60, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.SECONDS));
    assertThat(payload.getSubject()).isEqualTo(username);
    assertThat(payload.getIssuedAt()).isEqualTo(instant.truncatedTo(ChronoUnit.SECONDS));
  }

  @Test
  void shouldExtractUsernameFromToken() {
    String username = "username";
    String token = Jwts.builder().signWith(key).subject(username).compact();

    String actual = jwtService.extractUserName(token);
    assertThat(actual).isEqualTo(username);
  }

  @Test
  void shouldVerifyTokenWhenExpired() {
    String username = "username";
    String token = Jwts.builder().signWith(key).subject(username).expiration(Date.from(instant.plus(10, ChronoUnit.SECONDS))).compact();
    when(clock.instant()).thenReturn(instant.plus(20, ChronoUnit.SECONDS));

    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn(username);
    assertThat(jwtService.verifyToken(token, userDetails)).isFalse();
  }

  @Test
  void shouldVerifyTokenWhenUserIsDifferent() {
    String username = "username";
    String token = Jwts.builder().signWith(key).subject(username).expiration(Date.from(instant.plus(10, ChronoUnit.SECONDS))).compact();

    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn("anotherUser");
    assertThat(jwtService.verifyToken(token, userDetails)).isFalse();
  }

  @Test
  void shouldVerifyToken() {
    String username = "username";
    String token = Jwts.builder().signWith(key).subject(username).expiration(Date.from(instant.plus(10, ChronoUnit.SECONDS))).compact();

    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn(username);
    assertThat(jwtService.verifyToken(token, userDetails)).isTrue();
  }
}