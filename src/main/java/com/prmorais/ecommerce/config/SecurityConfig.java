package com.prmorais.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuer;
  @Value("${auth0.audience}")
  private String audience;

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(requests -> requests
        .requestMatchers("/api/orders/**")
        .authenticated()
        .anyRequest().permitAll()
        )
        .cors(Customizer.withDefaults())
        .oauth2ResourceServer(oauth2ResourceServer ->
            oauth2ResourceServer.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));
    //add CORS filters
//    .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configure(httpSecurityCorsConfigurer.disable()))
//    .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.configure(httpSecurityCsrfConfigurer.disable()));

    //add content negotiation strategy
//    .setSharedObject(ContentNegotiationStrategy.class, new
//        HeaderContentNegotiationStrategy());

    //force a non-empty response body for 401's to make the response more friendly

//    Okta.configureResourceServer401ResponseBody(http);

    // disable CSRF since we are not using Cookies for session tracking

    return http.build();
  }

  @Bean
  JwtDecoder jwtDecoder() {
    NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer);

    OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
    OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
    OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

    jwtDecoder.setJwtValidator(withAudience);

    return jwtDecoder;
  }
}
