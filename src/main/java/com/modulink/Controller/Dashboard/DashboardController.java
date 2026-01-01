package com.modulink.Controller.Dashboard;

import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneRepository;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import com.modulink.Model.Utente.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {
    private final CustomUserDetailsService customUserDetailsService;
    private final ModuloService moduloService;

    public DashboardController(CustomUserDetailsService customUserDetailsService, ModuloService moduloService) {
        this.customUserDetailsService=customUserDetailsService;
        this.moduloService=moduloService;
    }

    @GetMapping("/dashboard")
    public String dashboardDispatcher(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }
        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (utenteOpt.isPresent()) {
            UtenteEntity utente = utenteOpt.get();

            //List<ModuloEntity> moduli;
            //moduli = affiliazioneRepository.findModuliByRuolo(utente.getRuoli().stream().findFirst().get());


            List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);

            //per non far gestire al th le eccezioni perchè se succede è una bestemia
            model.addAttribute("moduli", moduli != null ? moduli : List.of());
            model.addAttribute("utente", utente);


            return "user/dashboard";
        } else {
            return "redirect:/logout";
        }
    }
}
