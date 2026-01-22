package com.modulink.Controller.Dashboard;

import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class DashboardController {
    private final CustomUserDetailsService customUserDetailsService;


    public DashboardController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService=customUserDetailsService;
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
