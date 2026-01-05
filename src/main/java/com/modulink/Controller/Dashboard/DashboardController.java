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

    @GetMapping({"/dashboard","/dashboard/"})
    public String dashboardDispatcher(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }
        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (utenteOpt.isPresent()) {
            UtenteEntity utente = utenteOpt.get();
            model.addAttribute("utente", utente);
            if(customUserDetailsService.isThisaNewUtente(utente)) { //Se è un nuovo utente allora viene portato alla schermata del 1°login
                return "user/firstlogin";
            }
            else { //Altrimenti viene caricata la dashboard normalmente
                List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);
                model.addAttribute("moduli", moduli != null ? moduli : List.of());
                return "user/dashboard";
            }
        } else {
            return "redirect:/logout";
        }
    }
}
