package com.modulink.Model.Utente;

import com.modulink.Model.Ruolo.RuoloEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementazione personalizzata dell'interfaccia {@link UserDetails} di Spring Security.
 * <p>
 * Questa classe funge da "Adapter" (ponte) tra l'entità del database {@link UtenteEntity}
 * e il sistema di autenticazione di Spring Security. Permette al framework di leggere
 * le credenziali (email/password) e le autorizzazioni (ruoli) dell'utente per gestire
 * il contesto di sicurezza (SecurityContext).
 *
 * @see UserDetails
 * @see UtenteEntity
 * @author Modulink Team
 * @version 1.0
 */
public class CustomUserDetails implements UserDetails {

    private final UtenteEntity user;

    /**
     * Costruttore che incapsula l'entità Utente.
     *
     * @param user L'oggetto utente recuperato dal database.
     */
    public CustomUserDetails(UtenteEntity user) {
        this.user = user;
    }

    /**
     * Restituisce le autorizzazioni concesse all'utente.
     * <p>
     * Il metodo itera sul {@code Set<RuoloEntity>} dell'utente e converte ogni ruolo
     * in un oggetto {@link SimpleGrantedAuthority}.
     * <p>
     * <strong>Nota:</strong> Viene aggiunto il prefisso "ROLE_" al nome del ruolo (convertito in maiuscolo).
     * Questo è lo standard di Spring Security (es. se il ruolo è "admin", diventa "ROLE_ADMIN")
     * necessario per l'utilizzo corretto di espressioni come {@code hasRole('ADMIN')}.
     *
     * @return Una collezione di autorità (ruoli) derivate dai ruoli dell'entità.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Usiamo il metodo helper che estrae i ruoli dalle associazioni
        Set<RuoloEntity> ruoli = user.getRuoli();

        if (ruoli == null) {
            return Collections.emptyList();
        }

        return ruoli.stream()
                .map(ruolo -> new SimpleGrantedAuthority("ROLE_" + ruolo.getNome().toUpperCase()))
                .collect(Collectors.toList());
    }

    /**
     * Restituisce la password dell'utente utilizzata per l'autenticazione.
     *
     * @return L'hash della password memorizzato nel database.
     */
    @Override
    public String getPassword() {
        return user.getHash_password();
    }

    /**
     * Restituisce il nome utente utilizzato per l'autenticazione.
     * <p>
     * Nel sistema Modulink, il nome utente univoco corrisponde all'indirizzo email.
     *
     * @return L'email dell'utente.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indica se l'account dell'utente è scaduto.
     * <p>
     * Attualmente restituisce sempre {@code true}, indicando che l'account non scade mai.
     * Implementare logica qui se si desidera gestire scadenze temporali degli account.
     *
     * @return {@code true} (account sempre valido in questa implementazione).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se l'account è bloccato o sospeso.
     * <p>
     * Attualmente restituisce sempre {@code true}.
     * Da implementare se si desidera bloccare utenti dopo troppi tentativi di accesso falliti.
     *
     * @return {@code true} (account non bloccato).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se le credenziali (password) sono scadute.
     * <p>
     * Attualmente restituisce sempre {@code true}.
     * Da implementare per forzare il cambio password periodico.
     *
     * @return {@code true} (credenziali sempre valide).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se l'utente è abilitato.
     * <p>
     * In futuro, questo metodo potrebbe restituire il valore di un campo "is_attivo"
     * presente nel database (es. per gestione ban o attivazione via email).
     * Attualmente assume che ogni utente presente nel DB sia attivo.
     *
     * @return {@code true} (utente sempre abilitato per default).
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Metodo getter aggiuntivo per recuperare l'entità Utente originale.
     * <p>
     * Utile nei Controller o Service layer quando si accede al {@code Principal}
     * e si ha bisogno di accedere a dati non standard di {@code UserDetails}
     * (es. ID Azienda, Nome, Cognome, Immagine Profilo).
     *
     * @return L'oggetto {@link UtenteEntity} originale completo.
     */
    public UtenteEntity getUser() {
        return user;
    }
}