package com.prmorais.ecommerce.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(requests -> requests
        .requestMatchers("/api/orders/**")
        .authenticated()
        .anyRequest().permitAll()
        )
        .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(withDefaults()))
    //add CORS filters
    .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configure(httpSecurityCorsConfigurer.disable()))
    .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.configure(httpSecurityCsrfConfigurer.disable()));

    //add content negotiation strategy
//    .setSharedObject(ContentNegotiationStrategy.class, new
//        HeaderContentNegotiationStrategy());

    //force a non-empty response body for 401's to make the response more friendly

    Okta.configureResourceServer401ResponseBody(http);

    // disable CSRF since we are not using Cookies for session tracking

    return http.build();
  }
}
