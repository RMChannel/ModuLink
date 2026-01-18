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

@Controller
public class StoreController extends ModuloController {
    private final CustomUserDetailsService customUserDetailsService;
    private final AttivazioneService attivazioneService;
    private final SupportFormService supportFormService;
    private final List<ModuloController> moduloControllers;
    private final ModuloService moduloService;

    public StoreController(CustomUserDetailsService customUserDetailsService, AttivazioneService attivazioneService, ModuloService moduloService, SupportFormService supportFormService, List<ModuloController> moduloControllers) {
        super(moduloService,9999);
        this.moduloService = moduloService;
        this.customUserDetailsService = customUserDetailsService;
        this.attivazioneService = attivazioneService;
        this.supportFormService = supportFormService;
        this.moduloControllers = moduloControllers;
    }

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

    @GetMapping({"/dashboard/store/request","/dashboard/store/request/"})
    public String requestModulePage(Principal principal, Model model) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) return "moduli/gma/store/RequestModuli";
        else return "redirect:/";
    }

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

    @Override
    //Modulo non disinstallabile
    public void disinstallaModulo(AziendaEntity azienda) {
        System.err.println("Modulo non disinstallabile");
        return;
    }
}
