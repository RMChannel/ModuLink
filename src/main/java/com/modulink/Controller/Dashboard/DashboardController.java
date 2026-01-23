package com.modulink.Controller.Dashboard;

import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller principale per la gestione del routing alla dashboard utente.
 * <p>
 * Questa classe funge da dispatcher centrale dopo l'autenticazione. Analizza lo stato dell'account utente
 * per determinare la corretta vista di atterraggio (landing page).
 * In particolare, gestisce la logica del "Primo Accesso" (First Login) forzando il completamento del profilo
 * se necessario.
 * </p>
 *
 * @author Modulink Team
 * @version 1.2.0
 * @since 1.0.0
 */
@Controller
public class DashboardController {
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param customUserDetailsService Servizio per il recupero e l'analisi dello stato utente.
     * @since 1.0.0
     */
    public DashboardController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService=customUserDetailsService;
    }

    /**
     * Gestisce le richieste verso la dashboard ("/" o "/dashboard").
     * <p>
     * Esegue i seguenti controlli:
     * <ol>
     *     <li>Verifica l'autenticazione (Principal non null).</li>
     *     <li>Recupera l'entità utente dal database.</li>
     *     <li>Controlla se l'utente è al primo accesso tramite {@link CustomUserDetailsService#isThisaNewUtente(UtenteEntity)}.</li>
     * </ol>
     * Se è il primo accesso, reindirizza alla procedura guidata di configurazione profilo.
     * Altrimenti, mostra la dashboard standard.
     *
     *
     * @param principal L'utente autenticato.
     * @param model     Il modello UI (non utilizzato direttamente qui ma disponibile per estensioni).
     * @return Il nome della vista ("user/firstlogin", "user/dashboard") o un redirect ("redirect:/...").
     * @since 1.0.0
     */
    @GetMapping({"/dashboard","/dashboard/"})
    public String dashboardDispatcher(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }
        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (utenteOpt.isPresent()) {
            UtenteEntity utente = utenteOpt.get();
            if(customUserDetailsService.isThisaNewUtente(utente)) { //Se è un nuovo utente allora viene portato alla schermata del 1°login
                return "user/firstlogin";
            }
            else { //Altrimenti viene caricata la dashboard normalmente
                return "user/dashboard";
            }
        } else {
            return "redirect:/logout";
        }
    }
}
