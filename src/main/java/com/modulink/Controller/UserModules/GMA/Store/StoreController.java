package com.modulink.Controller.UserModules.GMA.Store;

import com.modulink.Alert;
import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneService;
import com.modulink.Model.SupportForm.SupportFormEntity;
import com.modulink.Model.SupportForm.SupportFormService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller per la gestione dello Store/Marketplace dei moduli.
 * <p>
 * Permette alle aziende di sfogliare il catalogo dei moduli disponibili, acquistare nuove funzionalità
 * e disinstallare quelle non più necessarie. Gestisce anche le richieste di sviluppo per nuovi moduli custom.
 * </p>
 *
 * @author Modulink Team
 * @version 2.0.2
 * @since 1.3.0
 */
@Controller
public class StoreController extends ModuloController {
    private final CustomUserDetailsService customUserDetailsService;
    private final AttivazioneService attivazioneService;
    private final SupportFormService supportFormService;
    private final List<ModuloController> moduloControllers;
    private final ModuloService moduloService;

    /**
     * Costruttore con iniezione delle dipendenze.
     * 
     * @param customUserDetailsService Servizio utenti.
     * @param attivazioneService       Servizio gestione attivazioni/acquisti.
     * @param moduloService            Servizio moduli.
     * @param supportFormService       Servizio ticket supporto.
     * @param moduloControllers        Lista di tutti i controller dei moduli installati (per callback disinstallazione).
     * @since 1.3.0
     */
    public StoreController(CustomUserDetailsService customUserDetailsService, AttivazioneService attivazioneService, ModuloService moduloService, SupportFormService supportFormService, List<ModuloController> moduloControllers) {
        super(moduloService,9999);
        this.moduloService = moduloService;
        this.customUserDetailsService = customUserDetailsService;
        this.attivazioneService = attivazioneService;
        this.supportFormService = supportFormService;
        this.moduloControllers = moduloControllers;
    }

    /**
     * Visualizza la vetrina dello store.
     * <p>
     * Separa i moduli in due liste: acquistati e non acquistati, per una chiara presentazione all'utente.
     * </p>
     *
     * @param principal Identità utente.
     * @param model     Modello UI.
     * @return Vista "moduli/gma/store/StoreModuli" o redirect.
     * @since 1.3.0
     */
    @GetMapping({"/dashboard/store/", "/dashboard/store"})
    public String Store(Principal principal, Model model) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            List<ModuloEntity> moduliNonAcquistati = attivazioneService.getNotPurchased(utenteOpt.get().getAzienda());
            List<ModuloEntity> moduliAcquistati = attivazioneService.getAllPurchased(utenteOpt.get().getAzienda());
            model.addAttribute("moduliNon", moduliNonAcquistati);
            model.addAttribute("moduliAcquistati", moduliAcquistati);
            return "moduli/gma/store/StoreModuli";
        }
        else return "redirect:/";
    }

    /**
     * Gestisce l'acquisto di un nuovo modulo.
     *
     * @param moduloId  ID del modulo da attivare.
     * @param principal Identità utente.
     * @return Redirect allo store con esito.
     * @since 1.3.0
     */
    @PostMapping("/dashboard/store/buy")
    public String buyModule(@RequestParam("moduloId") int moduloId, Principal principal) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            boolean success = attivazioneService.purchaseModulo(utenteOpt.get().getAzienda(), moduloId);
            if (success) return "redirect:/dashboard/store" + Alert.success("Modulo acquistato con successo!");
            else return "redirect:/dashboard/store" + Alert.error("Errore durante l'acquisto del modulo.");
        }
        return "redirect:/";
    }

    /**
     * Gestisce la disinstallazione di un modulo.
     * <p>
     * Esegue una procedura complessa:
     * <ol>
     *     <li>Verifica che il modulo sia disinstallabile (non di sistema).</li>
     *     <li>Rimuove l'attivazione a livello di database.</li>
     *     <li>Invoca il metodo {@code disinstallaModulo} sul controller specifico del modulo per pulire i dati correlati.</li>
     * </ol>
     *
     *
     * @param moduloId  ID del modulo da rimuovere.
     * @param principal Identità utente.
     * @return Redirect allo store con esito.
     * @since 1.3.0
     */
    @PostMapping("dashboard/store/uninstall")
    public String uninstallModule(@RequestParam("moduloId") int moduloId, Principal principal) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            ModuloEntity modulo=moduloService.getModuloById(moduloId);
            if(modulo==null) return "redirect:/dashboard/store"+Alert.error("Modulo non trovato.");
            else if(!modulo.isVisible()) return "redirect:/dashboard/store"+Alert.error("Non è possibile disinstallare questo modulo.");
            if(attivazioneService.sellModulo(utente.getAzienda(), moduloId)) {
                for (ModuloController controller : moduloControllers) {
                    if (controller.getId() == moduloId) {
                        controller.disinstallaModulo(utente.getAzienda());
                        break;
                    }
                }
                return "redirect:/dashboard/store"+Alert.success("Modulo rimosso con successo!");
            }
            else return "redirect:/dashboard/store"+Alert.error("Errore durante la rimozione del modulo.");
        }
        else return "redirect:/";
    }

    /**
     * Visualizza il form per richiedere nuovi moduli.
     *
     * @param principal Identità utente.
     * @param model     Modello UI.
     * @return Vista richiesta moduli.
     * @since 1.3.5
     */
    @GetMapping({"/dashboard/store/request","/dashboard/store/request/"})
    public String requestModulePage(Principal principal, Model model) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) return "moduli/gma/store/RequestModuli";
        else return "redirect:/";
    }

    /**
     * Invia una richiesta di sviluppo per un nuovo modulo.
     *
     * @param principal Identità utente.
     * @param model     Modello UI.
     * @param messaggio Descrizione della richiesta.
     * @return Redirect con conferma invio.
     * @since 1.3.5
     */
    @PostMapping({"/dashboard/store/request","/dashboard/store/request/"})
    public String requestModule(Principal principal, Model model, @RequestParam("messaggio") String messaggio) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            if(messaggio ==null || messaggio.isEmpty()) return "redirect:/dashboard/store/request"+Alert.error("Il messaggio non può essere vuoto");
            SupportFormEntity form=new SupportFormEntity(utente.getEmail()+" "+utente.getAzienda().getNome(),"Richiesta nuovo modulo", messaggio);
            supportFormService.save(form);
            return "redirect:/dashboard/store/request"+Alert.success("Richiesta inviata con successo!");
        }
        else return "redirect:/";
    }

    /**
     * Il modulo Store è un componente di sistema essenziale e non può essere disinstallato.
     *
     * @param azienda Azienda target.
     */
    @Override
    //Modulo non disinstallabile
    public void disinstallaModulo(AziendaEntity azienda) {
        System.err.println("Modulo non disinstallabile");
        return;
    }
}
