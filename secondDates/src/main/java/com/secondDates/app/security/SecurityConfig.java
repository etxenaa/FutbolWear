package com.secondDates.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/erabiltzaileak", "/produktua/**", "/cesta", "/css/**", "/login/**", "/erabiltzaileak/**").permitAll() // Permitir acceso público a estas rutas
                .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
            )
            .formLogin(form -> form
                .loginPage("/login") // Página personalizada para login
                .defaultSuccessUrl("/home", true) // Redirección tras login exitoso
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login") // Redirección tras logout
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
