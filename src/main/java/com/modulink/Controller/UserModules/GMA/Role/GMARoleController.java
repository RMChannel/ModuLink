package com.modulink.Controller.UserModules.GMA.Role;

import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller per la gestione dei permessi RBAC (Role-Based Access Control) sui moduli attivi.
 * <p>
 * Parte integrante del modulo <strong>GMA (Gestione Moduli Aziendali)</strong>, questo controller
 * permette agli amministratori di configurare quali ruoli aziendali hanno diritto di accesso
 * a specifici moduli software acquistati dall'azienda.
 * </p>
 *
 * @author Modulink Team
 * @version 1.5.0
 * @since 1.2.5
 */
@Controller
public class GMARoleController extends ModuloController {
    private final CustomUserDetailsService customUserDetailsService;
    private final ModuloService moduloService;
    private final RuoloService ruoloService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param customUserDetailsService Servizio utenti.
     * @param moduloService            Servizio moduli.
     * @param ruoloService             Servizio ruoli.
     * @since 1.2.5
     */
    public GMARoleController(CustomUserDetailsService customUserDetailsService, ModuloService moduloService, RuoloService ruoloService) {
        super(moduloService,2);
        this.customUserDetailsService = customUserDetailsService;
        this.moduloService = moduloService;
        this.ruoloService = ruoloService;
    }

    /**
     * Visualizza la dashboard di configurazione dei permessi ruoli-moduli.
     *
     * @param principal Identità dell'amministratore.
     * @param model     Modello UI.
     * @return Vista "moduli/gma/role/RoleModuli" o redirect.
     * @since 1.2.5
     */
    @GetMapping({"dashboard/gma","dashboard/gma/"})
    public String dashboardDispatcher(Principal principal, Model model) {
        String email =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            model.addAttribute("ruoli", ruoloService.getAllRolesByAzienda(utente.getAzienda()));
            return "moduli/gma/role/RoleModuli";
        }
        else {
            return "redirect:/";
        }
    }

    /**
     * Aggiorna le affiliazioni (permessi) per un determinato modulo.
     * <p>
     * Riceve una lista di ID ruolo e aggiorna le associazioni nel database, garantendo
     * che i ruoli selezionati (e solo quelli) abbiano accesso al modulo specificato.
     * Implementa una logica di fallback per assicurare che il ruolo "Responsabile" mantenga sempre l'accesso.
     * </p>
     *
     * @param principal Identità amministratore.
     * @param model     Modello UI.
     * @param idModulo  ID del modulo da configurare.
     * @param roleIds   Lista degli ID dei ruoli abilitati.
     * @return Vista aggiornata con messaggio di successo/errore.
     * @since 1.2.5
     */
    @PostMapping("dashboard/gma/edit-modulo")
    public String editModulo(Principal principal, Model model, @RequestParam int idModulo, @RequestParam(required = false) List<Integer> roleIds) {
        String email =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            List<RuoloEntity> ruoli;
            if(roleIds==null || roleIds.isEmpty()) {
                ruoli = new ArrayList<>();
                ruoli.add(ruoloService.getResponsabile(utente.getAzienda())); //Fallback nel caso in cui rimuovi il responsabile da un modulo
            } else {
                try {
                    ruoli = ruoloService.getAllRolesFromIds(roleIds,utente.getAzienda().getId_azienda());
                } catch (RuoloNotFoundException re) {
                    model.addAttribute("ruoli", ruoloService.getAllRolesByAzienda(utente.getAzienda()));
                    model.addAttribute("error",true);
                    model.addAttribute("message","Uno dei ruoli selezionati non è stato trovato");
                    return "moduli/gma/role/RoleModuli";
                }
            }
            moduloService.updateModuloAffiliations(utente.getAzienda(),idModulo,ruoli);
            model.addAttribute("success",true);
            model.addAttribute("message","Assegnazione completata con successo");
            model.addAttribute("ruoli", ruoloService.getAllRolesByAzienda(utente.getAzienda()));
            return "moduli/gma/role/RoleModuli";
        }
        else {
            return "redirect:/";
        }
    }

    //Modulo non disinstallabile
    @Override
    public void disinstallaModulo(AziendaEntity azienda) {
        System.err.println("Modulo non disinstallabile");
        return;
    }
}
