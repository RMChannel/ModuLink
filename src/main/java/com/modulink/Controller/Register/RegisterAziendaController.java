package com.modulink.Controller.Register;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class RegisterAziendaController {
    @GetMapping("/register")
    public String registerGet(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }
        else return "register/RegistraAzienda";
    }
}
