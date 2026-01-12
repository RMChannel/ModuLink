package com.modulink;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Per semplicità, ma considera di abilitarlo in produzione
                .authorizeHttpRequests(auth -> auth
                        // Permetti l'accesso alle risorse statiche e alle pagine pubbliche
                        .requestMatchers("/", "/login", "/register/**","/register-utente", "/css/**", "/photo/**", "/favicon.ico","/dashboard","/contactus", "/privacy", "/termini", "/forgot-password", "/resend-otp", "/confirm-new-password", "/news", "/supporto", "/pacchetti", "/manuale").permitAll()
                        // Richiedi l'autenticazione per qualsiasi altra richiesta
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // Specifica la pagina di login personalizzata
                        .loginPage("/login")
                        // L'URL a cui il form invia i dati (gestito da Spring Security)
                        .loginProcessingUrl("/login")
                        // Pagina di atterraggio dopo un login riuscito
                        .defaultSuccessUrl("/dashboard", true)
                        //Pagina login con errore
                        .failureUrl("/login?error=true&message=Credenziali+non+valide")
                        // Permetti a tutti di accedere alla pagina di login
                        .permitAll()
                )
                .logout(logout -> logout
                        // L'URL per eseguire il logout
                        .logoutUrl("/logout")
                        // Pagina di atterraggio dopo il logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new ForwardedHeaderFilter());
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filter;
    }
}
