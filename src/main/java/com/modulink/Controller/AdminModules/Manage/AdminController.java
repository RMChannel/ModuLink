package com.modulink.Controller.AdminModules.Manage;

import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaService;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller per la gestione amministrativa globale della piattaforma ModuLink.
 * <p>
 * Questo controller gestisce le operazioni di back-office riservate agli amministratori di sistema.
 * In particolare, fornisce le funzionalità per la gestione delle aziende tenant (modifica, eliminazione)
 * e la visualizzazione del pannello di controllo principale.
 * Estende {@link ModuloController} per sfruttare la verifica centralizzata dei permessi di accesso ai moduli.
 * </p>
 *
 * @author Modulink Team
 * @version 2.1.0
 * @since 1.0.0
 */
@Controller
@RequestMapping("/dashboard/admin")
public class AdminController extends ModuloController {

    private final CustomUserDetailsService customUserDetailsService;
    private final AziendaService aziendaService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param moduloService            Servizio per la gestione dei moduli.
     * @param customUserDetailsService Servizio per il recupero dettagli utente.
     * @param aziendaService           Servizio per la gestione delle aziende.
     * @since 1.0.0
     */
    public AdminController(ModuloService moduloService, CustomUserDetailsService customUserDetailsService, AziendaService aziendaService) {
        super(moduloService, -1);
        this.customUserDetailsService = customUserDetailsService;
        this.aziendaService = aziendaService;
    }

    /**
     * Gestisce la richiesta GET per la pagina principale del pannello amministrativo.
     * <p>
     * Recupera l'elenco di tutte le aziende registrate nel sistema, filtrando eventualmente
     * l'azienda di appartenenza dell'amministratore per prevenire cancellazioni accidentali.
     * </p>
     *
     * @param principal L'oggetto Principal che rappresenta l'utente loggato.
     * @param model     Il modello UI per passare i dati alla vista Thymeleaf.
     * @return Il nome logico della vista "admin/adminPage" o un redirect in caso di accesso negato.
     * @since 1.0.0
     */
    @GetMapping({"/", ""})
    public String index(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();
        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(email);
        
        if (!isAccessibleModulo(currentUserOpt)) {
            return "redirect:/dashboard";
        }




        List<AziendaEntity> aziende = aziendaService.getAllAziende();
        // Prevent admin from deleting their own company (if applicable) or the main admin company
        currentUserOpt.ifPresent(utenteEntity -> {
            if(utenteEntity.getAzienda() != null) {
                 aziende.removeIf(a -> a.getId_azienda() == utenteEntity.getAzienda().getId_azienda());
            }
        });

        model.addAttribute("aziende", aziende);
        return "admin/adminPage";
    }

    /**
     * Gestisce la creazione o l'aggiornamento di un'entità Azienda.
     * <p>
     * Se l'ID dell'azienda è 0, viene creata una nuova istanza. Altrimenti, si procede all'aggiornamento
     * dei dati esistenti. Gestisce feedback utente tramite messaggi flash.
     * </p>
     *
     * @param formAzienda        L'oggetto AziendaEntity popolato dai dati del form.
     * @param redirectAttributes Attributi per passare messaggi di successo/errore dopo il redirect.
     * @return Redirect alla dashboard amministrativa.
     * @since 1.1.0
     */
    @PostMapping("/azienda/save")
    public String saveAzienda(@ModelAttribute AziendaEntity formAzienda, RedirectAttributes redirectAttributes) {
        try {
            if (formAzienda.getId_azienda() == 0) {
                // New Azienda
                aziendaService.registraAzienda(formAzienda);
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "Azienda creata con successo.");
            } else {
                // Update Azienda
                Optional<AziendaEntity> existingOpt = aziendaService.getAziendaById(formAzienda.getId_azienda());
                if (existingOpt.isPresent()) {
                    AziendaEntity existing = existingOpt.get();
                    existing.setNome(formAzienda.getNome());
                    existing.setPiva(formAzienda.getPiva());
                    existing.setIndirizzo(formAzienda.getIndirizzo());
                    existing.setCitta(formAzienda.getCitta());
                    existing.setCap(formAzienda.getCap());
                    existing.setTelefono(formAzienda.getTelefono());
                    existing.setLogo(formAzienda.getLogo());
                    
                    aziendaService.updateAzienda(existing);
                    redirectAttributes.addFlashAttribute("success", true);
                    redirectAttributes.addFlashAttribute("message", "Azienda aggiornata con successo.");
                } else {
                    redirectAttributes.addFlashAttribute("error", true);
                    redirectAttributes.addFlashAttribute("message", "Azienda non trovata.");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Errore durante il salvataggio: " + e.getMessage());
        }
        return "redirect:/dashboard/admin/";
    }

    /**
     * Gestisce l'eliminazione di un'azienda dal sistema.
     * <p>
     * L'operazione è critica e irreversibile, comportando la cancellazione a cascata di tutti i dati associati
     * (utenti, moduli attivati, dati operativi) grazie ai vincoli di integrità del database.
     * </p>
     *
     * @param id                 L'identificativo univoco dell'azienda da eliminare.
     * @param redirectAttributes Attributi per il feedback utente.
     * @return Redirect alla dashboard amministrativa.
     * @since 1.1.0
     */
    @PostMapping("/azienda/delete")
    public String deleteAzienda(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        try {
            aziendaService.deleteAzienda(id);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Azienda eliminata con successo.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Errore durante l'eliminazione dell'azienda: " + e.getMessage());
        }

        return "redirect:/dashboard/admin/";
    }

    /**
     * Implementazione del metodo astratto per la disinstallazione del modulo.
     * <p>
     * Poiché questo controller gestisce un modulo di sistema (Core), l'operazione di disinstallazione
     * non è permessa e il metodo non esegue alcuna azione logica, limitandosi a loggare un errore.
     * </p>
     *
     * @param azienda L'azienda per cui disinstallare il modulo (ignorato).
     * @since 1.0.0
     */
    //Modulo non disinstallabile
    @Override
    public void disinstallaModulo(AziendaEntity azienda) {
        System.err.println("Modulo non disinstallabile");
        return;
    }
}