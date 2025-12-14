package com.modulink.Controller.Login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        if(principal != null) {return "redirect:/home";}
        else return "login/login";
    }
}
