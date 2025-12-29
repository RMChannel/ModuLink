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
     * Reindirizza l'utente alla sua dashboard personale con ID formattato (000000001).
     */
    @GetMapping("/dashboard")
    public String dashboardDispatcher(Principal principal) {
        if(principal == null) {
            return "redirect:/";
        }

        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = userRepository.findByEmail(email);

        if (utenteOpt.isPresent()) {
            // Formatta l'ID a 9 cifre con zeri iniziali
            String formattedId = String.format("%09d", utenteOpt.get().getId_utente());
            return "redirect:/" + formattedId + "/dashboard";
        } else {
            return "redirect:/logout";
        }
    }

    /**
     * Endpoint specifico per la dashboard utente.
     * URL pattern: /{id-9-cifre}/dashboard (es. /000000001/dashboard)
     */
    @GetMapping("/{id}/dashboard")
    public String userDashboard(@PathVariable("id") String idStr, Principal principal, Model model) {
        if(principal == null) {
            return "redirect:/";
        }

        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = userRepository.findByEmail(email);

        if (utenteOpt.isPresent()) {
            UtenteEntity utente = utenteOpt.get();
            int id;

            // Tenta il parsing dell'ID
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                // Se l'ID non è un numero, reindirizza alla dashboard corretta
                String correctId = String.format("%09d", utente.getId_utente());
                return "redirect:/" + correctId + "/dashboard";
            }

            // IDOR SECURITY CHECK & FORMAT CHECK
            // Verifica che l'ID numerico corrisponda E che la stringa sia formattata correttamente (9 cifre)
            String expectedIdStr = String.format("%09d", utente.getId_utente());
            
            if (id != utente.getId_utente() || !idStr.equals(expectedIdStr)) {
                return "redirect:/" + expectedIdStr + "/dashboard";
            }

            model.addAttribute("utente", utente);
            return "user/dashboard";
        } else {
            return "redirect:/logout";
        }
    }
}
