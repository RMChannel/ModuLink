package com.modulink.Controller.GDU;

import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneRepository;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneRepository;
import com.modulink.Model.Utente.CustomUserDetailsService;
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
public class GDUController {
    private final ModuloService moduloService;
    private final CustomUserDetailsService customUserDetailsService;

    public GDUController(ModuloService moduloService, CustomUserDetailsService customUserDetailsService) {
        this.moduloService=moduloService;
        this.customUserDetailsService=customUserDetailsService;
    }

    @GetMapping("dashboard/gdu/")
    public String dashboardDispatcher(Principal principal, Model model) {

        if (principal == null) {
            return "redirect:/";
        }

        String email =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (utenteOpt.isPresent()) {

            UtenteEntity utente = utenteOpt.get();
            List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);

            //per non far gestire al th le eccezioni perchè se succede è una bestemia
            model.addAttribute("moduli", moduli != null ? moduli : List.of());
            model.addAttribute("utente", utente);
            model.addAttribute("utenti", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
            return "moduli/gdu/GestioneUtenti";

        }else  {
            return "redirect:/";
        }
    }
}
