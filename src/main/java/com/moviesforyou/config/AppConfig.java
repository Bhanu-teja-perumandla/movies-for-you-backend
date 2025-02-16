package com.moviesforyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;

@Configuration
public class AppConfig {
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder(5);
  }

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }

  @Bean
  public KeyGenerator keyGenerator() throws NoSuchAlgorithmException {
    return KeyGenerator.getInstance("HmacSHA256");
  }
}
