package com.moviesforyou.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {
  JwtService jwtService;
  Clock clock;
  KeyGenerator keyGenerator;
  String secretKey = "dGhpcyBpcyBhIHZlcnkgdmVyeSBsb25nIHNlY3JldCBrZXksIEltIHR5cmluZyB0byBiZSB2ZXJ5IHZlcnkgdmVyeSBsb25nIHNvIHRoYXQgdGhlIGp3dCBkb2VzIG5vdCB0aHJvdyBhbnkgZXJyb3I=";
  Instant instant = Instant.now();

  @BeforeEach
  void setUp() {
    clock = mock(Clock.class);
    keyGenerator = mock(KeyGenerator.class);
    jwtService = new JwtService(clock, keyGenerator);
  }

  @Test
  void shouldGenerateTokenUsingUserName() {
    String username = "username";

    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    when(keyGenerator.generateKey()).thenReturn(key);
    when(clock.instant()).thenReturn(instant);

    String actual = jwtService.generateToken(username);
    JwtParser jwtParser = Jwts.parser()
        .verifyWith(key)
        .build();
    Claims payload = jwtParser.parseSignedClaims(actual).getPayload();

    assertThat(payload.getExpiration()).isEqualTo(instant.plus(60, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.SECONDS));
    assertThat(payload.getSubject()).isEqualTo(username);
    assertThat(payload.getIssuedAt()).isEqualTo(instant.truncatedTo(ChronoUnit.SECONDS));
  }
}