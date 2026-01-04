package com.modulink.Controller.HomePage;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homepage(Model model, Principal principal) {
        if(principal != null) {
            return "redirect:/dashboard";
        }
        else {
            return "index";
        }
    }

    @GetMapping("/contactus")
    public String contactUs(Model model, Principal principal) {
        if(principal != null) {
            return "redirect:/home";
        }
        else {
            return "homepage/contactus";
        }
    }

    @GetMapping("/privacy")
    public String privacy(Model model, Principal principal) {
        if(principal != null) {
            return "redirect:/home";
        }
        else {
            return "homepage/privacy";
        }
    }
    @GetMapping("/termini")
    public String termini(Model model, Principal principal) {
        if(principal != null) {
            return "redirect:/home";
        }
        else {
            return "homepage/termini";
        }
    }

    @GetMapping("/news")
    public String news(Model model, Principal principal){
        return "homepage/news";
    }

    @GetMapping("/supporto")
    public String supporto(Model model, Principal principal) {
        return "homepage/supporto";
    }

    @GetMapping("/pacchetti")
    public String pacchetti(Model model, Principal principal) {
        return "homepage/pacchetti";
    }

    @GetMapping("/manuale")
    public String manuale(Model model, Principal principal) {
        return "homepage/manuale";
    }

}