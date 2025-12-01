package org.example.dinoapi.security;

import org.example.dinoapi.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                        "/api/v1/usuarios/login",
                                        "/api/v1/usuarios",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/api/v1/preguntas/health",
                                        "/api/v1/dinosaurios",
                                        "/api/v1/dinosaurios/",
                                        "/api/v1/dinosaurios/paginar",
                                        "/api/v1/dinosaurios/search",
                                        "/api/v1/dinosaurios/*"
                                ).permitAll()
                        .requestMatchers("/api/v1/periodos/**").authenticated()
                        .requestMatchers("/api/v1/dinosaurios/**").authenticated()
                        .requestMatchers("/api/v1/preguntas/**").authenticated()
                        .requestMatchers("/api/v1/dinosaurios_favoritos/**").authenticated()
                        .requestMatchers("/api/v1/usuarios/**").authenticated()

                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
