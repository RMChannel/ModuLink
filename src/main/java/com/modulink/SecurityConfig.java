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

/**
 * Classe di configurazione principale per la sicurezza dell'applicazione (Spring Security).
 * <p>
 * Definisce le regole di autorizzazione HTTP, la gestione del login/logout, la crittografia delle password
 * e i filtri necessari per il corretto funzionamento dietro proxy o load balancer.
 * </p>
 *
 * @author Modulink Team
 * @version 2.1.3
 * @since 1.0.0
 */
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    /**
     * Costruttore per l'iniezione del servizio di dettagli utente.
     *
     * @param userDetailsService Implementazione custom per il recupero dei dati utente.
     * @since 1.0.0
     */
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configura la catena di filtri di sicurezza (Security Filter Chain).
     * <p>
     * Definisce:
     * <ul>
     *     <li>Disabilitazione CSRF (nota: valutare abilitazione in prod).</li>
     *     <li>Regole di accesso agli endpoint (whitelist per risorse pubbliche, autenticazione per il resto).</li>
     *     <li>Configurazione del Form Login (pagina custom, handler di successo/fallimento).</li>
     *     <li>Configurazione del Logout (pulizia sessione e cookie).</li>
     * </ul>
     *
     *
     * @param http Oggetto {@link HttpSecurity} per la configurazione fluente.
     * @return La catena di filtri costruita.
     * @throws Exception In caso di errori di configurazione.
     * @since 1.0.0
     */
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

    /**
     * Configura il provider di autenticazione DAO.
     * <p>
     * Collega il {@link UserDetailsService} custom e il {@link PasswordEncoder} per la verifica delle credenziali.
     * </p>
     *
     * @return Il provider configurato.
     * @since 1.0.0
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Espone il gestore delle autenticazioni come Bean.
     *
     * @param config Configurazione di autenticazione di Spring.
     * @return L'AuthenticationManager.
     * @throws Exception Se impossibile recuperare il manager.
     * @since 1.0.0
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Definisce l'algoritmo di hashing per le password.
     *
     * @return Un'istanza di {@link BCryptPasswordEncoder}.
     * @since 1.0.0
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Registra il filtro per la gestione degli header inoltrati (X-Forwarded-For, X-Forwarded-Proto).
     * <p>
     * Essenziale quando l'applicazione gira dietro un proxy inverso (es. Nginx, Load Balancer) per
     * risolvere correttamente l'indirizzo IP del client e il protocollo (HTTP/HTTPS).
     * </p>
     *
     * @return Il bean di registrazione del filtro con precedenza massima.
     * @since 1.1.0
     */
    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new ForwardedHeaderFilter());
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filter;
    }
}
