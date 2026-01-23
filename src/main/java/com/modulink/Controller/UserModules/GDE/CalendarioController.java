package com.modulink.Controller.UserModules.GDE;

import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Eventi.EventoService;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller dedicato alla gestione della visualizzazione del calendario per il modulo <strong>GDE (Gestione Eventi)</strong>.
 * <p>
 * Questa classe si occupa di servire la pagina principale del calendario aziendale, orchestrando il caricamento
 * degli eventi a cui l'utente loggato partecipa. Implementa i meccanismi di controllo accesso ereditati da {@link ModuloController}
 * per garantire l'isolamento dei dati tra i diversi tenant (Aziende).
 * </p>
 *
 * @author Modulink Team
 * @version 1.4.2
 * @since 1.1.0
 */
@Controller
public class CalendarioController extends ModuloController {
    private final CustomUserDetailsService customUserDetailsService;
    private final EventoService eventoService;

    /**
     * Costruttore per l'iniezione delle dipendenze e configurazione del modulo.
     * 
     * @param moduloService            Servizio per la verifica dei moduli attivi.
     * @param customUserDetailsService Servizio per il recupero dell'utente corrente.
     * @param eventoService            Servizio per la logica di business degli eventi.
     * @since 1.1.0
     */
    public CalendarioController(ModuloService moduloService, CustomUserDetailsService customUserDetailsService, EventoService eventoService) {
        super(moduloService, 4);
        this.customUserDetailsService = customUserDetailsService;
        this.eventoService = eventoService;
    }

    /**
     * Gestisce la richiesta GET per la visualizzazione della dashboard del calendario.
     * <p>
     * Recupera l'identità dell'utente autenticato, verifica i permessi di accesso al modulo GDE (ID: 4)
     * e carica la lista degli eventi filtrata per l'utente specifico.
     * </p>
     *
     * @param principal Identità dell'utente fornita da Spring Security.
     * @param model     Modello UI per il passaggio dei dati alla vista Thymeleaf.
     * @return Il nome della vista "moduli/gde/calendario" o un redirect in caso di accesso non autorizzato.
     * @since 1.1.0
     */
    @GetMapping({"/dashboard/calendar","/dashboard/calendar/"})
    public String calendario(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        
        if (utenteOpt.isEmpty() || !isAccessibleModulo(utenteOpt)){
            return "redirect:/dashboard/";
        }

        List<EventoEntity> eventi = eventoService.findAllByUtente(utenteOpt.get());
        model.addAttribute("eventi", eventi != null ? eventi : new ArrayList<EventoEntity>());

        return "moduli/gde/calendario";
    }

    /**
     * Esegue le operazioni di pulizia necessarie durante la disinstallazione del modulo GDE.
     * <p>
     * Rimuove massivamente tutti gli eventi e le partecipazioni correlate per l'azienda specificata
     * per garantire la coerenza del database post-disattivazione.
     * </p>
     *
     * @param azienda L'entità {@link AziendaEntity} che sta dismettendo il modulo.
     * @since 1.2.0
     */
    @Override
    public void disinstallaModulo(AziendaEntity azienda) {
        eventoService.deleteAllByAzienda(azienda);
    }
}