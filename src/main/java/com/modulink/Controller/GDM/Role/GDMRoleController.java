package com.modulink.Controller.GDM.Role;

import com.modulink.Controller.ModuloController;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloNotFoundException;
import com.modulink.Model.Ruolo.RuoloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class GDMRoleController extends ModuloController {
    private final CustomUserDetailsService customUserDetailsService;
    private final ModuloService moduloService;
    private final RuoloService ruoloService;

    public GDMRoleController(CustomUserDetailsService customUserDetailsService, ModuloService moduloService, RuoloService ruoloService) {
        super(moduloService,3);
        this.customUserDetailsService = customUserDetailsService;
        this.moduloService = moduloService;
        this.ruoloService = ruoloService;
    }

    @GetMapping({"dashboard/gdm","dashboard/gdm/"})
    public String dashboardDispatcher(Principal principal, Model model) {
        String email =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);
            model.addAttribute("moduli", moduli != null ? moduli : List.of());
            model.addAttribute("utente", utente);
            model.addAttribute("ruoli", ruoloService.getAllRolesByAzienda(utente.getAzienda()));
            return "moduli/gdm/role/RoleModuli";
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping("dashboard/edit-modulo")
    public String editModulo(Principal principal, Model model, @RequestParam int idModulo, @RequestParam(required = false) List<Integer> roleIds) {
        String email =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);
            model.addAttribute("utente", utente);
            List<RuoloEntity> ruoli;
            if(roleIds==null || roleIds.isEmpty()) {
                ruoli = Collections.emptyList();
            } else {
                try {
                    ruoli = ruoloService.getAllRolesFromIds(roleIds,utente.getAzienda().getId_azienda());
                } catch (RuoloNotFoundException re) {
                    model.addAttribute("moduli", moduli != null ? moduli : List.of());
                    model.addAttribute("ruoli", ruoloService.getAllRolesByAzienda(utente.getAzienda()));
                    model.addAttribute("error",true);
                    model.addAttribute("message","Uno dei ruoli selezionati non è stato trovato");
                    return "moduli/gdm/role/RoleModuli";
                }
            }
            moduloService.updateModuloAffiliations(utente.getAzienda(),idModulo,ruoli);
            model.addAttribute("success",true);
            model.addAttribute("message","Assegnazione completata con successo");
            model.addAttribute("ruoli", ruoloService.getAllRolesByAzienda(utente.getAzienda()));
            moduli = moduloService.findModuliByUtente(utente);
            model.addAttribute("moduli", moduli != null ? moduli : List.of());
            return "moduli/gdm/role/RoleModuli";
        }
        else {
            return "redirect:/";
        }
    }
}
