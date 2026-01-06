package com.modulink.Controller.GDM.Store;

import com.modulink.Alert;
import com.modulink.Controller.ModuloController;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneService;
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
    private final ModuloService moduloService;

    public StoreController(CustomUserDetailsService customUserDetailsService, AttivazioneService attivazioneService, ModuloService moduloService) {
        super(moduloService,3);
        this.customUserDetailsService = customUserDetailsService;
        this.attivazioneService = attivazioneService;
        this.moduloService = moduloService;
    }

    @GetMapping({"/dashboard/store/", "/dashboard/store"})
    public String Store(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);

        if (utenteOpt.isPresent()) {
            List<ModuloEntity> moduliNonAcquistati = attivazioneService.getNotPurchased(utenteOpt.get().getAzienda());
            List<ModuloEntity> moduli = moduloService.findModuliByUtente(utenteOpt.get());
            model.addAttribute("moduliNon", moduliNonAcquistati);
            model.addAttribute("utente", utenteOpt.get());
            model.addAttribute("moduli", moduli);
        }

        return "moduli/gdm/store/StoreModuli";
    }

    @PostMapping("/dashboard/store/buy")
    public String buyModule(@RequestParam("moduloId") int moduloId, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);

        if (utenteOpt.isPresent()) {
            boolean success = attivazioneService.purchaseModulo(utenteOpt.get().getAzienda(), moduloId);
            if (success) {
                return "redirect:/dashboard/store" + Alert.success("Modulo acquistato con successo!");
            }
        }

        return "redirect:/dashboard/store" + Alert.error("Errore durante l'acquisto del modulo.");
    }
}
