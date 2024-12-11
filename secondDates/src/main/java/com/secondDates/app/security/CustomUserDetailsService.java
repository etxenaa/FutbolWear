package com.secondDates.app.security;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.secondDates.app.modelo.Erabiltzailea;
import com.secondDates.app.repository.ErabiltzaileaRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ErabiltzaileaRepository erabRepo;

    public CustomUserDetailsService(ErabiltzaileaRepository erabRepo) {
        this.erabRepo = erabRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al usuario por su email
        Optional<Erabiltzailea> userOpt = erabRepo.findByEmail(username);
        
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + username);
        }

        Erabiltzailea erabiltzailea = userOpt.get();
        
        // Crea un SimpleGrantedAuthority para el rol del usuario
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + erabiltzailea.getRola().toUpperCase());

        System.out.println(authority);
        // Crear el objeto UserDetails con la contraseña y los roles de la base de datos
        return new User(
                erabiltzailea.getEmail(),   // Email como nombre de usuario
                erabiltzailea.getPasahitza(),  // Contraseña encriptada
                Collections.singletonList(authority)  // Lista de roles/authorities
        );
    }
}
