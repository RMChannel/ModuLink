package com.modulink.Controller.Dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardControll {
    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        if(principal == null)
            return "redirect:/";
        else
            return "user/dashboard";
    }
}
