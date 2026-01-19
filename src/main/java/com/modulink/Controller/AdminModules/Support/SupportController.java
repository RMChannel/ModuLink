package com.modulink.Controller.AdminModules.Support;

import com.modulink.Alert;
import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.SupportForm.SupportFormEntity;
import com.modulink.Model.SupportForm.SupportFormService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller dedicato alla gestione del modulo Supporto/Ticket.
 * <p>
 * Permette agli utenti di inviare richieste di assistenza tramite un form pubblico
 * e agli amministratori di visualizzare e gestire (cancellare) i ticket ricevuti.
 * Estende {@link ModuloController} per l'integrazione architetturale con il sistema moduli.
 * </p>
 *
 * @author Modulink Team
 * @version 1.8.1
 * @since 1.2.0
 */
@Controller
public class SupportController extends ModuloController {
    private final SupportFormService supportFormService;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Costruttore per l'iniezione dei servizi.
     *
     * @param moduloService            Servizio base gestione moduli.
     * @param supportFormService       Servizio gestione ticket di supporto.
     * @param customUserDetailsService Servizio recupero utenti.
     * @since 1.2.0
     */
    public SupportController(ModuloService moduloService, SupportFormService supportFormService, CustomUserDetailsService customUserDetailsService) {
        super(moduloService, -2);
        this.supportFormService = supportFormService;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Gestisce l'invio di una richiesta di supporto da parte di un utente.
     *
     * @param supportForm   DTO contenente i dati del ticket (email, categoria, messaggio).
     * @param bindingResult Risultato della validazione del form.
     * @param model         Modello UI.
     * @return Nome della vista da renderizzare.
     * @since 1.2.0
     */
    @PostMapping("/supporto")
    public String support(@Valid @ModelAttribute("support") SupportForm supportForm, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("support",supportForm);
            return "homepage/supporto";
        }

        SupportFormEntity form=new SupportFormEntity(supportForm.getEmail(),supportForm.getCategory(),supportForm.getMessage());
        supportFormService.save(form);

        model.addAttribute("success",true);
        model.addAttribute("message","Il messaggio è stato inviato con successo");
        return "homepage/supporto";
    }

    /**
     * Visualizza la lista dei messaggi di supporto nel pannello di amministrazione.
     * Accessibile solo agli amministratori con permessi adeguati.
     *
     * @param principal Utente amministratore.
     * @param model     Modello UI.
     * @return Vista amministrazione supporto o redirect.
     * @since 1.2.0
     */
    @GetMapping({"/dashboard/support","/dashboard/support/"})
    public String getMessaggi(Principal principal, Model model) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            model.addAttribute("messages",supportFormService.findAll());
            return "admin/support/supportPage";
        }
        else return "redirect:/";
    }

    /**
     * Rimuove un messaggio di supporto dal sistema.
     *
     * @param principal   Utente amministratore.
     * @param model       Modello UI.
     * @param idMessaggio ID del ticket da cancellare.
     * @return Redirect alla pagina di supporto con messaggio di successo.
     * @since 1.2.0
     */
    @PostMapping({"/dashboard/remove-support","/dashboard/remove-support/"})
    public String removeMessaggio(Principal principal, Model model, @RequestParam int idMessaggio) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            supportFormService.delete(idMessaggio);
            return "redirect:/dashboard/support"+ Alert.success("Messaggio cancellato con successo");
        }
        else return "redirect:/";
    }

    /**
     * Inibisce la disinstallazione del modulo Supporto.
     *
     * @param azienda Azienda target.
     * @since 1.2.0
     */
    //Modulo non disinstallabile
    @Override
    public void disinstallaModulo(AziendaEntity azienda) {
        System.err.println("Modulo non disinstallabile");
        return;
    }
}