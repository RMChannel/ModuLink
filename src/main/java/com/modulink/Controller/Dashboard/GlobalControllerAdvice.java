package com.modulink.Controller.Dashboard;


import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/*
*
* This is a simple controller interceptor that loads every modul of a login user
*
* */


@ControllerAdvice
public class GlobalControllerAdvice {

    private final CustomUserDetailsService customUserDetailsService;
    private final ModuloService moduloService;

    public GlobalControllerAdvice(CustomUserDetailsService customUserDetailsService, ModuloService moduloService) {
        this.customUserDetailsService = customUserDetailsService;
        this.moduloService = moduloService;
    }

    @ModelAttribute
    public void addCommonAttributes(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);

            if (utenteOpt.isPresent()) {
                UtenteEntity utente = utenteOpt.get();
                model.addAttribute("loggedUser", utente); // Use a distinct name to avoid conflicts if needed, or stick to "utente"
                model.addAttribute("utente", utente);     // Keeping "utente" for backward compatibility with existing templates

                List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);
                for(ModuloEntity modulo : moduli) {
                    if(modulo.getId_modulo()==3) {
                        moduli.remove(modulo);
                        moduli.add(modulo);
                        break;
                    }
                }
                model.addAttribute("moduli", moduli != null ? moduli : List.of());
            }
        }
    }
}

