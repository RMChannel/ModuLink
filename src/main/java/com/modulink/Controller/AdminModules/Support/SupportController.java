package com.modulink.Controller.AdminModules.Support;

import com.modulink.Alert;
import com.modulink.Controller.ModuloController;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.SupportForm.SupportFormEntity;
import com.modulink.Model.SupportForm.SupportFormService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class SupportController extends ModuloController {
    private final SupportFormService supportFormService;
    private final CustomUserDetailsService customUserDetailsService;

    public SupportController(ModuloService moduloService, SupportFormService supportFormService, CustomUserDetailsService customUserDetailsService) {
        super(moduloService, -2);
        this.supportFormService = supportFormService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/supporto")
    public String support(@Valid @ModelAttribute("support") SupportForm supportForm, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("support",supportForm);
            return "homepage/supporto";
        }

        SupportFormEntity form=new SupportFormEntity(supportForm.getEmail(),supportForm.getCategory(),supportForm.getMessage());
        supportFormService.save(form);

        model.addAttribute("success",true);
        model.addAttribute("message","Il messaggio è stato inviato con successo");
        return "homepage/supporto";
    }

    @GetMapping({"/dashboard/support","/dashboard/support/"})
    public String getMessaggi(Principal principal, Model model) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            model.addAttribute("messages",supportFormService.findAll());
            return "admin/support/supportPage";
        }
        else return "redirect:/";
    }

    @PostMapping({"/dashboard/remove-support","/dashboard/remove-support/"})
    public String removeMessaggio(Principal principal, Model model, @RequestParam int idMessaggio) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            supportFormService.delete(idMessaggio);
            return "redirect:/dashboard/support"+ Alert.success("Messaggio cancellato con successo");
        }
        else return "redirect:/";
    }
}