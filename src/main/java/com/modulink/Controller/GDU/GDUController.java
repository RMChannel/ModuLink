package com.modulink.Controller.GDU;

import com.modulink.Model.Azienda.AziendaService;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneRepository;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneRepository;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import com.modulink.Model.Utente.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    private boolean isAccessibleModulo(Optional<UtenteEntity> user) {
        if(user.isEmpty()) return false;
        UtenteEntity utente=user.get();
        return moduloService.isAccessibleModulo(0, utente);
    }

    @GetMapping("dashboard/gdu/")
    public String dashboardDispatcher(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }
        String email =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (isAccessibleModulo(utenteOpt)) {
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

    @PostMapping("dashboard/gdu/remove-user")
    public String removeUser(Principal principal, Model model, @RequestParam("email") String email) {
        String emailLogged =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(emailLogged);
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);
            model.addAttribute("moduli", moduli != null ? moduli : List.of());
            model.addAttribute("utente", utente);
            Optional<UtenteEntity> userToDeleteOPT=customUserDetailsService.findByEmail(email);
            if(userToDeleteOPT.isEmpty()) {
                model.addAttribute("error",true);
                model.addAttribute("message","L'utente indicato non è stato trovato");
                model.addAttribute("utenti", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
                return "moduli/gdu/GestioneUtenti";
            }
            UtenteEntity utenteToDelete=userToDeleteOPT.get();
            if(utenteToDelete.getAzienda().getId_azienda()!=utente.getAzienda().getId_azienda()) { //Utente di un'altra azienda
                System.err.println("Sto stronzo sta provando ad eliminare un cristiano non suo");
                return "redirect:/dashboard";
            }
            else if(utenteToDelete.getId_utente()==utente.getId_utente()) {
                model.addAttribute("error",true);
                model.addAttribute("message","Sei un coglione");
                model.addAttribute("utenti", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
                return "moduli/gdu/GestioneUtenti";
            }
            customUserDetailsService.rimuoviUtente(utenteToDelete); //Cancello l'utente e tutte le sue associazioni
            model.addAttribute("utenti", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
            model.addAttribute("success",true);
            model.addAttribute("message","L'utente "+utenteToDelete.getNome()+" "+utenteToDelete.getCognome()+" è stato eliminato con successo");
            return "moduli/gdu/GestioneUtenti";
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping("dashboard/gdu/add-user")
    public String addUser(Principal principal, Model model) {
        return "";
    }
}
