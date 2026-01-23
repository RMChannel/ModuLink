package com.modulink.Controller.UserModules.GTM;

import com.modulink.Alert;
import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneEntity;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneService;
import com.modulink.Model.Relazioni.Associazione.AssociazioneService;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloService;
import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Task.TaskID;
import com.modulink.Model.Task.TaskService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UserNotFoundException;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Controller per il modulo <strong>GTM (Gestione Task Manager)</strong>.
 * <p>
 * Questo controller gestisce l'intero ciclo di vita delle attività operative (Task):
 * creazione, assegnazione a singoli utenti o interi ruoli, modifica, completamento e cancellazione.
 * Integra un sistema di assegnazione flessibile basato su {@link GTMMessage} che permette di
 * indirizzare i task in modo polimorfico.
 * </p>
 * <p>
 * Estende {@link ModuloController} (ID Modulo: 5) per la verifica dei permessi.
 * </p>
 *
 * @author Modulink Team
 * @version 3.2.1
 * @since 1.3.0
 */
@Controller
public class GTMController extends ModuloController {
    private final TaskService taskService;
    private final AssegnazioneService assegnazioneService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RuoloService ruoloService;
    private final AssociazioneService associazioneService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param moduloService            Servizio moduli.
     * @param taskService              Servizio gestione task.
     * @param assegnazioneService      Servizio assegnazione task.
     * @param customUserDetailsService Servizio utenti.
     * @param ruoloService             Servizio ruoli.
     * @param associazioneService      Servizio associazione utente-ruolo.
     * @since 1.3.0
     */
    public GTMController(ModuloService moduloService, TaskService taskService, AssegnazioneService assegnazioneService, CustomUserDetailsService customUserDetailsService, RuoloService ruoloService, AssociazioneService associazioneService) {
        super(moduloService, 5);
        this.taskService = taskService;
        this.assegnazioneService = assegnazioneService;
        this.customUserDetailsService = customUserDetailsService;
        this.ruoloService = ruoloService;
        this.associazioneService = associazioneService;
    }

    /**
     * Visualizza la dashboard di gestione Task.
     * <p>
     * Recupera e mostra sia i task creati dall'utente corrente (per monitoraggio),
     * sia i task assegnati all'utente (da completare).
     * </p>
     *
     * @param principal Identità utente.
     * @param model     Modello UI.
     * @return Vista "moduli/gtm/GestioneTask" o redirect.
     * @since 1.3.0
     */
    @GetMapping({"/dashboard/gtm","/dashboard/gtm/"})
    public String getGTM(Principal principal, Model model) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = (UtenteEntity) model.getAttribute("utente");
            model.addAttribute("taskCreate",taskService.findByCreatore(utente));
            model.addAttribute("taskAssegnate",assegnazioneService.getTaskAssegnate(utente));
            model.addAttribute("form", new GTMForm());
            model.addAttribute("editForm", new GTMEditForm());
            return "moduli/gtm/GestioneTask";
        }
        else return "redirect:/";
    }

    /**
     * API per recuperare la lista di possibili assegnatari (Utenti e Ruoli).
     * <p>
     * Utilizzato dal frontend per popolare il selettore di assegnazione.
     * Restituisce una lista eterogenea di oggetti {@link GTMMessage} normalizzati.
     * </p>
     *
     * @param principal Identità utente.
     * @return JSON List di GTMMessage o BadRequest.
     * @since 1.3.5
     */
    @PostMapping("/dashboard/gtm/getusers")
    public Object getUsers(Principal principal) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            List<GTMMessage> messages = new ArrayList<>();
            List<UtenteEntity> utenti = customUserDetailsService.getAllByAzienda(utente.getAzienda());
            for(UtenteEntity u : utenti) messages.add(new GTMMessage(u.getEmail(),"utente",u.getId_utente(),u.getAzienda().getId_azienda(),"#4287f5"));
            List<RuoloEntity> ruoli = ruoloService.getAllRolesByAzienda(utente.getAzienda());
            for (RuoloEntity r : ruoli) messages.add(new GTMMessage(r.getNome(),"ruolo",r.getId_ruolo(),r.getAzienda().getId_azienda(),r.getColore()));
            return ResponseEntity.ok(messages);
        }
        else return ResponseEntity.badRequest().build();
    }

    /**
     * Crea un nuovo Task.
     * <p>
     * Implementa la logica di esplosione delle assegnazioni: se un task è assegnato a un Ruolo,
     * il sistema risolve tutti gli utenti appartenenti a quel ruolo e crea un'assegnazione per ciascuno.
     * Esegue validazioni sulle date (scadenza futura).
     * </p>
     *
     * @param principal     Identità creatore.
     * @param model         Modello UI.
     * @param form          DTO dati task.
     * @param bindingResult Esito validazione.
     * @return Redirect alla dashboard GTM con esito.
     * @since 1.3.0
     */
    @PostMapping({"/dashboard/gtm","/dashboard/gtm/"})
    public String createNewTask(Principal principal, Model model, @Valid @ModelAttribute("form") GTMForm form, BindingResult bindingResult) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            if(form.getScadenza() != null && form.getScadenza().isBefore(LocalDate.now().minusDays(1))) bindingResult.rejectValue("scadenza","datanelpassato.error","La data di scadenza non può essere nel passato");
            if(form.getScadenza() != null) {
                if(form.getScadenza().isBefore(LocalDate.now())) bindingResult.rejectValue("scadenza","scadenzanelpassato.error","La scadenza non può essere prima di oggi");
            }
            if(form.getPriorita()<0 || form.getPriorita()>5) bindingResult.rejectValue("priorita","prioritanonvalida.error","La priorità deve essere compresa tra 0 e 5");
            if(bindingResult.hasErrors()) {
                model.addAttribute("taskCreate",taskService.findByCreatore(utente));
                model.addAttribute("taskAssegnate",assegnazioneService.getTaskAssegnate(utente));
                model.addAttribute("editForm", new GTMEditForm());
                return "moduli/gtm/GestioneTask";
            }
            TaskEntity task=new TaskEntity(utente.getAzienda(),utente,form.getTitolo(),form.getPriorita(),form.getScadenza(), LocalDate.now(),null);
            Set<AssegnazioneEntity> assegnazioniSet=new HashSet<>();
            if(form.getMessaggi()!=null) {
                for(GTMMessage msg:form.getMessaggi()) {
                    if(msg.getType().equals("utente")) {
                        try {
                            assegnazioniSet.add(new AssegnazioneEntity(task, customUserDetailsService.getByID(msg.getId(),msg.getAzienda())));
                        } catch (UserNotFoundException e) {
                            return "redirect:/dashboard/gtm"+Alert.error("Utente non trovato");
                        }
                    }
                    else if(msg.getType().equals("ruolo")) {
                        List<UtenteEntity> utentiFromRole=associazioneService.getAllByRole(ruoloService.getRoleById(msg.getId(),utente.getAzienda()));
                        for(UtenteEntity u:utentiFromRole) assegnazioniSet.add(new AssegnazioneEntity(task,u));
                    }
                    else {
                        return "redirect:/dashboard/gtm"+Alert.error("Messaggio non valido");
                    }
                }
            }
            task.setAssegnazioni(new ArrayList<>(assegnazioniSet));
            taskService.save(task);
            return "redirect:/dashboard/gtm"+Alert.success("Task creata con successo");
        }
        else return "redirect:/";
    }

    /**
     * Cancella un Task esistente.
     * <p>
     * Permesso solo all'utente che ha creato il task (Owner).
     * </p>
     *
     * @param principal Identità utente.
     * @param model     Modello UI.
     * @param idTask    ID del task.
     * @return Redirect con esito.
     * @since 1.3.0
     */
    @PostMapping({"/dashboard/gtm/delete-task","/dashboard/gtm/delete-task/"})
    public String removeTask(Principal principal, Model model, @RequestParam int idTask) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            TaskEntity task=taskService.findById(new TaskID(idTask,utente.getAzienda().getId_azienda()));
            if (task==null) return "redirect:/dashboard/gtm"+Alert.error("Task non trovata");
            else if(!task.getUtenteCreatore().equals(utente)) return "redirect:/dashboard/gtm"+Alert.error("Non sei autorizzato a cancellare questa task");
            else {
                taskService.delete(idTask, utente.getAzienda().getId_azienda());
                return "redirect:/dashboard/gtm"+Alert.success("Task cancellata con successo");
            }
        }
        else return "redirect:/";
    }

    /**
     * Marca un task come completato.
     * <p>
     * Imposta la data di completamento al momento attuale.
     * </p>
     *
     * @param principal Identità utente.
     * @param model     Modello UI.
     * @param idTask    ID del task.
     * @return Redirect con esito.
     * @since 1.3.0
     */
    @PostMapping({"/dashboard/gtm/set-as-completato","/dashboard/gtm/set-as-completato/"})
    public String setAsCompletato(Principal principal, Model model, @RequestParam int idTask) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            TaskEntity task=taskService.findById(new TaskID(idTask,utente.getAzienda().getId_azienda()));
            task.setCompletato();
            taskService.classicUpdate(task);
            return "redirect:/dashboard/gtm"+Alert.success("Task completata con successo");
        }
        else return "redirect:/";
    }

    /**
     * Modifica i dettagli di un Task esistente.
     * <p>
     * Permette di cambiare titolo, priorità, scadenza, stato di completamento e assegnatari.
     * Aggiorna le relazioni di assegnazione nel database.
     * </p>
     *
     * @param principal     Identità creatore.
     * @param model         Modello UI.
     * @param form          DTO modifiche.
     * @param bindingResult Esito validazione.
     * @return Redirect con esito.
     * @since 1.3.0
     */
    @PostMapping({"/dashboard/gtm/edit-task","/dashboard/gtm/edit-task/"})
    public String editTask(Principal principal, Model model, @Valid @ModelAttribute("editForm") GTMEditForm form, BindingResult bindingResult) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            TaskEntity task=taskService.findById(new TaskID(form.getIdTask(),utente.getAzienda().getId_azienda()));
            if(task==null) return "redirect:/dashboard/gtm"+Alert.error("Task non trovata");
            if(form.getScadenza() != null && form.getScadenza().isBefore(LocalDate.now().minusDays(1)))
                bindingResult.rejectValue("scadenza","datanelpassato.error","La data di scadenza non può essere nel passato");
            if(form.getPriorita()<0 || form.getPriorita()>5) bindingResult.rejectValue("priorita","prioritanonvalida.error","La priorità deve essere compresa tra 0 e 5");
            if(bindingResult.hasErrors()) {
                model.addAttribute("taskCreate",taskService.findByCreatore(utente));
                model.addAttribute("taskAssegnate",assegnazioneService.getTaskAssegnate(utente));
                model.addAttribute("form", new GTMForm());
                return "moduli/gtm/GestioneTask";
            }
            else if(!task.getUtenteCreatore().equals(utente)) return "redirect:/dashboard/gtm"+Alert.error("Non sei autorizzato a modificare questa task");
            task.setTitolo(form.getTitolo());
            task.setPriorita(form.getPriorita());
            task.setScadenza(form.getScadenza());
            if(form.isCompletato()) {
                if(!task.isCompletato()) task.setCompletato();
            }
            else {
                if(task.isCompletato()) task.setCompletato(null);
            }
            Set<AssegnazioneEntity> assegnazioniSet=new HashSet<>();
            if(form.getMessaggi()!=null) {
                for(GTMMessage msg:form.getMessaggi()) {
                    if(msg.getType().equals("utente")) {
                        try {
                            assegnazioniSet.add(new AssegnazioneEntity(task, customUserDetailsService.getByID(msg.getId(),utente.getAzienda().getId_azienda())));
                        } catch (UserNotFoundException e) {
                            return "redirect:/dashboard/gtm"+Alert.error("Utente non trovato");
                        }
                    }
                    else if(msg.getType().equals("ruolo")) {
                        List<UtenteEntity> utentiFromRole=associazioneService.getAllByRole(ruoloService.getRoleById(msg.getId(),utente.getAzienda()));
                        for(UtenteEntity u:utentiFromRole) assegnazioniSet.add(new AssegnazioneEntity(task,u));
                    }
                    else {
                        return "redirect:/dashboard/gtm"+Alert.error("Messaggio non valido");
                    }
                }
            }
            List<AssegnazioneEntity> newAssignments = new ArrayList<>(assegnazioniSet);
            taskService.update(task.getId_task(), task, newAssignments);
            return "redirect:/dashboard/gtm"+Alert.success("Task modificata con successo");
        }
        else return "redirect:/";
    }

    /**
     * Disinstalla il modulo GTM rimuovendo tutti i task associati all'azienda.
     *
     * @param azienda Azienda target.
     * @since 1.3.0
     */
    @Override
    public void disinstallaModulo(AziendaEntity azienda) {
        taskService.deleteAllByAzienda(azienda);
    }
}