package com.modulink.Controller.Dashboard;

import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneRepository;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneRepository;
import com.modulink.Model.Utente.UtenteEntity;
import com.modulink.Model.Utente.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AffiliazioneRepository affiliazioneRepository;


    @GetMapping("/dashboard")
    public String dashboardDispatcher(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }

        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = userRepository.findByEmail(email);


        if (utenteOpt.isPresent()) {
            UtenteEntity utente = utenteOpt.get();
            List<ModuloEntity> moduli;
            moduli = affiliazioneRepository.findModuliByRuolo(utente.getRuoli().stream().findFirst().get());

            model.addAttribute("moduli", moduli);
            model.addAttribute("utente", utente);


            return "user/dashboard";
        } else {
            return "redirect:/logout";
        }
    }
}
