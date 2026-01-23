package com.modulink.Controller.UserModules.GRU;

import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UserNotFoundException;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

/**
 * Controller per il modulo <strong>GRU (Gestione Ruoli Utenti)</strong>.
 * <p>
 * Questo modulo permette di definire la struttura organizzativa dell'azienda attraverso la creazione
 * e gestione di ruoli personalizzati. Fornisce le funzionalità per creare, modificare ed eliminare ruoli,
 * nonché per assegnare massivamente utenti a specifici ruoli.
 * Estende {@link ModuloController} (ID Modulo: 1) per la gestione dei permessi di accesso.
 * </p>
 *
 * @author Modulink Team
 * @version 2.1.0
 * @since 1.2.0
 */
@Controller
public class GRUController extends ModuloController {
    private final ModuloService moduloService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RuoloService ruoloService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param moduloService            Servizio gestione moduli.
     * @param customUserDetailsService Servizio utenti.
     * @param ruoloService             Servizio gestione ruoli.
     * @since 1.2.0
     */
    public GRUController(ModuloService moduloService, CustomUserDetailsService customUserDetailsService, RuoloService ruoloService) {
        super(moduloService, 1);
        this.moduloService = moduloService;
        this.customUserDetailsService = customUserDetailsService;
        this.ruoloService = ruoloService;
    }

    /**
     * Visualizza la dashboard di gestione ruoli.
     * <p>
     * Carica l'elenco dei ruoli esistenti e la lista completa degli utenti per facilitare le operazioni di assegnazione.
     * </p>
     *
     * @param principal Identità utente loggato.
     * @param model     Modello UI.
     * @return Vista "moduli/gru/GestioneRuoli" o redirect.
     * @since 1.2.0
     */
    @GetMapping({"dashboard/gru/","dashboard/gru"})
    public String dashboardDispatcher(Principal principal, Model model) {
        String email =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            model.addAttribute("ruoli", ruoloService.getAllRolesByAzienda(utente.getAzienda()));
            
            // For assigning users
            model.addAttribute("allUsers", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
            
            // Form for new role
            if (!model.containsAttribute("newRoleForm")) {
                model.addAttribute("newRoleForm", new NewRoleForm());
            }
            
            return "moduli/gru/GestioneRuoli";
        }
        else {
            return "redirect:/";
        }
    }

    /**
     * Crea un nuovo ruolo aziendale personalizzato.
     *
     * @param newRoleForm   DTO con i dati del nuovo ruolo (nome, colore, descrizione).
     * @param bindingResult Esito validazione.
     * @param principal     Identità utente.
     * @param model         Modello UI.
     * @return Redirect alla dashboard con esito.
     * @since 1.2.0
     */
    @PostMapping("dashboard/gru/add-role")
    public String addRole(@ModelAttribute("newRoleForm") @Valid NewRoleForm newRoleForm, BindingResult bindingResult, Principal principal, Model model) {
        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            List<RuoloEntity> ruoli=ruoloService.getAllRolesByAzienda(utente.getAzienda());
            model.addAttribute("allUsers", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
            if (bindingResult.hasErrors()) {
                model.addAttribute("newRoleForm",newRoleForm);
                model.addAttribute("ruoli", ruoli);
                return "moduli/gru/GestioneRuoli";
            }
            else {
                RuoloEntity newRole=new RuoloEntity(utente.getAzienda(), newRoleForm.getNome(), newRoleForm.getColore(), newRoleForm.getDescrizione());
                ruoli.add(ruoloService.createRole(utenteOpt.get().getAzienda(), newRole));
                model.addAttribute("ruoli", ruoli);
                model.addAttribute("success",true);
                model.addAttribute("message","Il ruolo "+newRoleForm.getNome()+" è stato creato correttamente.");
                return "moduli/gru/GestioneRuoli";
            }
        }
        else {
            return "redirect:/";
        }
    }

    /**
     * Modifica le proprietà di un ruolo esistente.
     *
     * @param idRuolo     ID del ruolo da modificare.
     * @param nome        Nuovo nome.
     * @param colore      Nuovo colore (esadecimale).
     * @param descrizione Nuova descrizione.
     * @param principal   Identità utente.
     * @param model       Modello UI.
     * @param newRoleForm DTO placeholder.
     * @return Redirect alla dashboard con esito.
     * @since 1.2.0
     */
    @PostMapping("dashboard/gru/modify-role")
    public String modifyRole(@RequestParam int idRuolo, @RequestParam String nome, @RequestParam String colore, @RequestParam String descrizione, Principal principal, Model model, @ModelAttribute("newRoleForm") NewRoleForm newRoleForm) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            List<RuoloEntity> ruoli=ruoloService.getAllRolesByAzienda(utente.getAzienda());
            model.addAttribute("allUsers", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
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
            return "moduli/gru/GestioneRuoli";
        }
        else {
            return "redirect:/";
        }
    }

    /**
     * Elimina un ruolo personalizzato.
     * <p>
     * Non permette l'eliminazione dei ruoli di sistema (Default).
     * </p>
     *
     * @param idRuolo     ID del ruolo.
     * @param principal   Identità utente.
     * @param model       Modello UI.
     * @param newRoleForm DTO placeholder.
     * @return Redirect con esito operazione.
     * @since 1.2.0
     */
    @PostMapping("dashboard/gru/delete-role")
    public String deleteRole(@RequestParam int idRuolo, Principal principal, Model model, @ModelAttribute("newRoleForm") NewRoleForm newRoleForm) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            List<RuoloEntity> ruoli=ruoloService.getAllRolesByAzienda(utente.getAzienda());
            model.addAttribute("allUsers", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
            RuoloEntity ruolo=ruoloService.getRoleById(idRuolo,utente.getAzienda());
            try {
                ruoloService.deleteRole(utenteOpt.get().getAzienda(), idRuolo);
                ruoli.remove(ruolo);
                model.addAttribute("ruoli",ruoli);
            } catch (IllegalArgumentException e) {
                model.addAttribute("ruoli",ruoli);
                model.addAttribute("error",true);
                model.addAttribute("message","Non puoi eliminare un ruolo di default");
                return "moduli/gru/GestioneRuoli";
            }
            model.addAttribute("success",true);
            model.addAttribute("message","Il ruolo è stato eliminato con successo");
            return "moduli/gru/GestioneRuoli";
        }
        else {
            return "redirect:/";
        }
    }

    /**
     * Assegna un insieme di utenti a uno specifico ruolo.
     * <p>
     * Aggiorna le associazioni esistenti, sovrascrivendo la lista degli utenti appartenenti al ruolo.
     * </p>
     *
     * @param idRuolo     ID del ruolo target.
     * @param userIds     Lista degli ID degli utenti da assegnare.
     * @param principal   Identità utente.
     * @param model       Modello UI.
     * @param newRoleForm DTO placeholder.
     * @return Redirect con esito.
     * @since 1.2.5
     */
    @PostMapping("dashboard/gru/assign-role")
    public String assignRole(@RequestParam int idRuolo, @RequestParam(required = false) List<Integer> userIds, Principal principal, Model model, @ModelAttribute("newRoleForm") NewRoleForm newRoleForm) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            model.addAttribute("ruoli",ruoloService.getAllRolesByAzienda(utente.getAzienda()));
            List<UtenteEntity> usersToAssign;
            if (userIds == null || userIds.isEmpty()) {
                usersToAssign = Collections.emptyList();
            } else {
                try {
                    usersToAssign=customUserDetailsService.getAllUsersFromIDs(userIds,utente.getAzienda().getId_azienda());
                } catch (UserNotFoundException ue) {
                    model.addAttribute("allUsers", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
                    model.addAttribute("error",true);
                    model.addAttribute("message","Uno degli utenti selezionati non è stato trovato");
                    return "moduli/gru/GestioneRuoli";
                }
            }
            ruoloService.updateRoleAssociations(utente.getAzienda(), idRuolo, usersToAssign);
            model.addAttribute("success",true);
            model.addAttribute("message","Assegnazione completata con successo");
            model.addAttribute("allUsers", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
            return "moduli/gru/GestioneRuoli";
        }
        else {
            return "redirect:/dashboard/gru";
        }
    }

    /**
     * Il modulo GRU è un componente core del sistema di gestione utenti e non può essere disinstallato.
     *
     * @param azienda Azienda target.
     */
    //Modulo non disinstallabile
    @Override
    public void disinstallaModulo(AziendaEntity azienda) {
        System.err.println("Modulo non disinstallabile");
        return;
    }
}