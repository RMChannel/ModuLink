package com.modulink.Controller.GDE;

import com.modulink.Controller.ModuloController;
import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Eventi.EventoService;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CalendarioController extends ModuloController {
    private final CustomUserDetailsService customUserDetailsService;
    private final EventoService eventoService;

    public CalendarioController(ModuloService moduloService, CustomUserDetailsService customUserDetailsService, EventoService eventoService) {
        super(moduloService, 4);
        this.customUserDetailsService = customUserDetailsService;
        this.eventoService = eventoService;
    }

    @GetMapping({"/dashboard/calendar","/dashboard/calendar/"})
    public String calendario(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        
        if (utenteOpt.isEmpty() || !isAccessibleModulo(utenteOpt)){
            return "redirect:/dashboard/";
        }

        List<EventoEntity> eventi = eventoService.findAllByUtente(utenteOpt.get());
        model.addAttribute("eventi", eventi != null ? eventi : new ArrayList<EventoEntity>());

        return "moduli/gde/calendario";
    }
}