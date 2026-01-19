package com.modulink.Controller.Dashboard;


import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Componente di tipo {@link ControllerAdvice} per la gestione globale degli attributi del Model.
 * <p>
 * Questa classe agisce come un interceptor per tutti i Controller dell'applicazione.
 * La sua funzione principale è popolare automaticamente il Model con dati comuni necessari
 * in quasi tutte le viste (es. layout base, sidebar), come l'utente loggato
 * e la lista dei moduli a cui ha accesso.
 * </p>
 * <p>
 * Questo approccio evita la duplicazione di codice nei singoli metodi dei controller (DRY).
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.5
 * @since 1.0.0
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    private final CustomUserDetailsService customUserDetailsService;
    private final ModuloService moduloService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param customUserDetailsService Servizio per recuperare i dettagli dell'utente.
     * @param moduloService            Servizio per recuperare i moduli accessibili.
     * @since 1.0.0
     */
    public GlobalControllerAdvice(CustomUserDetailsService customUserDetailsService, ModuloService moduloService) {
        this.customUserDetailsService = customUserDetailsService;
        this.moduloService = moduloService;
    }

    /**
     * Metodo annotato con {@link ModelAttribute} eseguito prima di ogni metodo di gestione richiesta (@RequestMapping).
     * <p>
     * Se un utente è autenticato (Principal presente), recupera:
     * <ul>
     *     <li>L'oggetto {@link UtenteEntity} completo.</li>
     *     <li>La lista di {@link ModuloEntity} abilitati per l'utente (basata su attivazioni azienda e permessi ruolo).</li>
     * </ul>
     * Questi dati vengono aggiunti al Model e resi disponibili alle viste Thymeleaf.
     *
     *
     * @param principal L'utente autenticato.
     * @param model     Il modello condiviso della richiesta.
     * @since 1.0.0
     */
    @ModelAttribute
    public void addCommonAttributes(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);

            if (utenteOpt.isPresent()) {
                UtenteEntity utente = utenteOpt.get();
                model.addAttribute("loggedUser", utente); // Use a distinct name to avoid conflicts if needed, or stick to "utente"
                model.addAttribute("utente", utente);     // Keeping "utente" for backward compatibility with existing templates

                List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);
                model.addAttribute("moduli", moduli);
            }
        }
    }
}

