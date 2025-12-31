package com.modulink.Controller.GDM.Store;

import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneRepository;
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
public class StoreController {

    private final ModuloService moduloService;
    private final CustomUserDetailsService customUserDetailsService;
    private final AttivazioneRepository attivazioneRepository;
    private final ModuloRepository moduloRepository;

    public StoreController(ModuloService moduloService, 
                           CustomUserDetailsService customUserDetailsService, 
                           AttivazioneRepository attivazioneRepository, 
                           ModuloRepository moduloRepository) {
        this.moduloService = moduloService;
        this.customUserDetailsService = customUserDetailsService;
        this.attivazioneRepository = attivazioneRepository;
        this.moduloRepository = moduloRepository;
    }

    @GetMapping("/dashboard/store")
    public String Store(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        
        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        
        if (utenteOpt.isPresent()) {
            List<ModuloEntity> moduliNonAcquistati = attivazioneRepository.getAllNotPurchased(utenteOpt.get().getAzienda());
            model.addAttribute("moduli", moduliNonAcquistati);
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
        Optional<ModuloEntity> moduloOpt = moduloRepository.findById(moduloId);

        if (utenteOpt.isPresent() && moduloOpt.isPresent()) {
            UtenteEntity utente = utenteOpt.get();
            ModuloEntity modulo = moduloOpt.get();

            // Check if already purchased to avoid duplicates
            if (!attivazioneRepository.existsByAziendaAndModulo(utente.getAzienda(), modulo)) {
                AttivazioneEntity nuovaAttivazione = new AttivazioneEntity(modulo, utente.getAzienda());
                attivazioneRepository.save(nuovaAttivazione);
                return "redirect:/dashboard/store?success&message=Modulo acquistato con successo!";
            }
        }

        return "redirect:/dashboard/store?error&message=Errore durante l'acquisto del modulo.";
    }
}
