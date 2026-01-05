package com.modulink.Controller.GDR;

import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import com.modulink.Model.Utente.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

@Controller
public class GDRController {
    private final ModuloService moduloService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RuoloService ruoloService;
    private final UserRepository userRepository;

    public GDRController(ModuloService moduloService, CustomUserDetailsService customUserDetailsService, RuoloService ruoloService, UserRepository userRepository) {
        this.moduloService = moduloService;
        this.customUserDetailsService = customUserDetailsService;
        this.ruoloService = ruoloService;
        this.userRepository = userRepository;
    }

    private boolean isAccessibleModulo(Optional<UtenteEntity> user) {
        if(user.isEmpty()) return false;
        UtenteEntity utente=user.get();
        return moduloService.isAccessibleModulo(1, utente);
    }

    @GetMapping({"dashboard/gdr/","dashboard/gdr"})
    public String dashboardDispatcher(Principal principal, Model model) {
        if(principal==null) {
            return "redirect:/";
        }
        String email =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);
            model.addAttribute("moduli", moduli != null ? moduli : List.of());
            model.addAttribute("utente", utente);
            model.addAttribute("ruoli", ruoloService.getAllRolesByAzienda(utente.getAzienda()));
            
            // For assigning users
            model.addAttribute("allUsers", userRepository.getAllByAziendaIs(utente.getAzienda()));
            
            // Form for new role
            if (!model.containsAttribute("newRoleForm")) {
                model.addAttribute("newRoleForm", new NewRoleForm());
            }
            
            return "moduli/gdr/GestioneRuoli";
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping("dashboard/gdr/add-role")
    public String addRole(@ModelAttribute("newRoleForm") @Valid NewRoleForm newRoleForm, BindingResult bindingResult, Principal principal, Model model) {
        if (principal == null) return "redirect:/";
        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);
            model.addAttribute("moduli", moduli != null ? moduli : List.of());
            model.addAttribute("utente", utente);
            List<RuoloEntity> ruoli=ruoloService.getAllRolesByAzienda(utente.getAzienda());
            model.addAttribute("allUsers", userRepository.getAllByAziendaIs(utente.getAzienda()));
            if (bindingResult.hasErrors()) {
                model.addAttribute("newRoleForm",newRoleForm);
                model.addAttribute("ruoli", ruoli);
                return "moduli/gdr/GestioneRuoli";
            }
            else {
                RuoloEntity newRole=new RuoloEntity(utente.getAzienda(), newRoleForm.getNome(), newRoleForm.getColore(), newRoleForm.getDescrizione());
                ruoli.add(ruoloService.createRole(utenteOpt.get().getAzienda(), newRole));
                model.addAttribute("ruoli", ruoli);
                model.addAttribute("success",true);
                model.addAttribute("message","Il ruolo "+newRoleForm.getNome()+" è stato creato correttamente.");
                return "moduli/gdr/GestioneRuoli";
            }
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping("dashboard/gdr/modify-role")
    public String modifyRole(@RequestParam int idRuolo, @RequestParam String nome, @RequestParam String colore, @RequestParam String descrizione, Principal principal, Model model, @ModelAttribute("newRoleForm") NewRoleForm newRoleForm) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            List<RuoloEntity> ruoli=ruoloService.getAllRolesByAzienda(utente.getAzienda());
            List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);
            model.addAttribute("moduli", moduli != null ? moduli : List.of());
            model.addAttribute("utente", utente);
            model.addAttribute("allUsers", userRepository.getAllByAziendaIs(utente.getAzienda()));
            if(nome.length()<2 || nome.length()>50) {
                model.addAttribute("error",true);
                model.addAttribute("message","Il nome dev'essere compreso tra i 2 e i 50 caratteri");
            }
            else if(colore.length()!=7) {
                model.addAttribute("error",true);
                model.addAttribute("message","Il colore non è in un formato standard esadecimale");
            }
            else if(descrizione.length()>255) {
                model.addAttribute("error",true);
                model.addAttribute("message","La descrizione non dev'essere più grande di 255 caratteri");
            }
            else {
                ruoli.remove(ruoloService.getRoleById(idRuolo,utente.getAzienda()));
                ruoli.add(ruoloService.updateRole(utente.getAzienda(), idRuolo, nome, colore, descrizione));
                model.addAttribute("success",true);
                model.addAttribute("message","Il ruolo "+nome+" è stato aggiornato correttamente.");
            }
            model.addAttribute("ruoli",ruoli);
            return "moduli/gdr/GestioneRuoli";
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping("dashboard/gdr/delete-role")
    public String deleteRole(@RequestParam int idRuolo, Principal principal, Model model, @ModelAttribute("newRoleForm") NewRoleForm newRoleForm) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);
            model.addAttribute("moduli", moduli != null ? moduli : List.of());
            model.addAttribute("utente", utente);
            List<RuoloEntity> ruoli=ruoloService.getAllRolesByAzienda(utente.getAzienda());
            model.addAttribute("allUsers", userRepository.getAllByAziendaIs(utente.getAzienda()));
            RuoloEntity ruolo=ruoloService.getRoleById(idRuolo,utente.getAzienda());
            try {
                ruoloService.deleteRole(utenteOpt.get().getAzienda(), idRuolo);
                ruoli.remove(ruolo);
                model.addAttribute("ruoli",ruoli);
            } catch (IllegalArgumentException e) {
                model.addAttribute("ruoli",ruoli);
                model.addAttribute("error",true);
                model.addAttribute("message","Non puoi eliminare un ruolo di default");
                return "moduli/gdr/GestioneRuoli";
            }
            model.addAttribute("success",true);
            model.addAttribute("message","Il ruolo è stato eliminato con successo");
            return "moduli/gdr/GestioneRuoli";
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping("dashboard/gdr/assign-role")
    public String assignRole(@RequestParam int idRuolo, @RequestParam(required = false) List<Integer> userIds, Principal principal, Model model, @ModelAttribute("newRoleForm") NewRoleForm newRoleForm) {
        if (principal == null) return "redirect:/";
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        
        if (isAccessibleModulo(utenteOpt)) {
            List<UtenteEntity> usersToAssign;
            if (userIds == null || userIds.isEmpty()) {
                usersToAssign = Collections.emptyList();
            } else {
                usersToAssign = null;
            }
            ruoloService.updateRoleAssociations(utenteOpt.get().getAzienda(), idRuolo, usersToAssign);
        }
        return "redirect:/dashboard/gdr";
    }
}