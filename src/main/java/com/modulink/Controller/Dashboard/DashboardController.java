package com.modulink.Controller.Dashboard;

import com.modulink.Model.Utente.UtenteEntity;
import com.modulink.Model.Utente.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Dispatcher endpoint.
     * Reindirizza l'utente alla sua dashboard personale basata sull'ID.
     */
    @GetMapping("/dashboard")
    public String dashboardDispatcher(Principal principal) {
        if(principal == null) {
            return "redirect:/";
        }

        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = userRepository.findByEmail(email);

        if (utenteOpt.isPresent()) {
            // Redirect alla dashboard specifica dell'utente
            return "redirect:/user/" + utenteOpt.get().getId_utente() + "/dashboard";
        } else {
            return "redirect:/logout";
        }
    }

    /**
     * Endpoint specifico per la dashboard utente.
     * Include un controllo di sicurezza per evitare che l'utente A acceda alla dashboard dell'utente B.
     */
    @GetMapping("/user/{id}/dashboard")
    public String userDashboard(@PathVariable("id") int id, Principal principal, Model model) {
        if(principal == null) {
            return "redirect:/";
        }

        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = userRepository.findByEmail(email);

        if (utenteOpt.isPresent()) {
            UtenteEntity utente = utenteOpt.get();

            // IDOR SECURITY CHECK: Verifica che l'ID nell'URL corrisponda all'utente loggato
            if(utente.getId_utente() != id) {
                // Se non corrispondono, reindirizza alla dashboard corretta
                return "redirect:/user/" + utente.getId_utente() + "/dashboard";
            }

            model.addAttribute("utente", utente);
            return "user/dashboard";
        } else {
            return "redirect:/logout";
        }
    }
}
