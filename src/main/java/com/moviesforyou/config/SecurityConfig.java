package com.moviesforyou.config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final UserDetailsService userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final String clientUrl;
  private final JwtFilter jwtFilter;

  public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, @Value("${CLIENT_URL}") String clientUrl, JwtFilter jwtFilter) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.clientUrl = clientUrl;
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(a-> a
            .requestMatchers("register", "login", "movies").permitAll()
            .anyRequest().authenticated())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .cors(c-> c.configurationSource(request -> {
          CorsConfiguration corsConfiguration = new CorsConfiguration();
          corsConfiguration.addAllowedOrigin(clientUrl);
          corsConfiguration.addAllowedHeader("*");
          corsConfiguration.addAllowedMethod("GET");
          return corsConfiguration;
        }));
    return httpSecurity.build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider
        .setUserDetailsService(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
    return daoAuthenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
