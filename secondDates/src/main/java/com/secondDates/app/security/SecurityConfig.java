package com.secondDates.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/erabiltzaileak", "/produktua/**", "/cesta", "/css/**", "/inicio/**", "/erabiltzaileak/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/loginOndo")
                .defaultSuccessUrl("/loginOndo", true)
                .failureUrl("/")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/loginOndo")
                .permitAll()
            )
            .userDetailsService(userDetailsService); // Agrega el servicio de detalles del usuario

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


