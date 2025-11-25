package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth.
                                requestMatchers("/user/create").permitAll().
                                requestMatchers("/user/sign-in").permitAll().
                                requestMatchers("/user/change-password").permitAll().
                                requestMatchers(
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/webjars/**"
                                ).permitAll().
                                requestMatchers("/course/create").hasRole("ADMIN").
                                requestMatchers(HttpMethod.GET, "/course/**").permitAll().
                                requestMatchers(HttpMethod.PUT, "/course/update/*").hasRole("ADMIN").
                                requestMatchers(HttpMethod.DELETE, "/course/delete/*").hasRole("ADMIN").
                                requestMatchers("/chapter/create").hasRole("ADMIN").
                                requestMatchers(HttpMethod.GET, "/chapter/**").permitAll().
                                requestMatchers(HttpMethod.PUT, "/chapter/**").hasRole("ADMIN").
                                requestMatchers(HttpMethod.DELETE, "/chapter/**").hasRole("ADMIN").
                                requestMatchers(HttpMethod.GET, "/lesson/**").permitAll().
                                requestMatchers("/lesson/create").hasRole("ADMIN").
                                requestMatchers(HttpMethod.PUT, "/lesson/**").hasRole("ADMIN").
                                requestMatchers(HttpMethod.DELETE, "/lesson/**").hasRole("ADMIN").


                                requestMatchers(HttpMethod.GET, "/course").permitAll().

                                anyRequest().authenticated()


                )
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(o -> o
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(keycloakRoleConverter()))
                );

        return httpSecurity.build();
    }

    @Bean
    public KeycloakRoleConverter keycloakRoleConverter() {
        return new KeycloakRoleConverter();
    }

}
