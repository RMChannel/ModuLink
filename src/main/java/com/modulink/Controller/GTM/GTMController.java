package com.modulink.Controller.GTM;

import com.modulink.Alert;
import com.modulink.Controller.ModuloController;
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
import java.util.stream.Collectors;

@Controller
public class GTMController extends ModuloController {
    private final TaskService taskService;
    private final AssegnazioneService assegnazioneService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RuoloService ruoloService;
    private final AssociazioneService associazioneService;

    public GTMController(ModuloService moduloService, TaskService taskService, AssegnazioneService assegnazioneService, CustomUserDetailsService customUserDetailsService, RuoloService ruoloService, AssociazioneService associazioneService) {
        super(moduloService, 5);
        this.taskService = taskService;
        this.assegnazioneService = assegnazioneService;
        this.customUserDetailsService = customUserDetailsService;
        this.ruoloService = ruoloService;
        this.associazioneService = associazioneService;
    }

    @GetMapping({"/dashboard/gtm","/dashboard/gtm/"})
    public String getGTM(Principal principal, Model model) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = (UtenteEntity) model.getAttribute("utente");
            model.addAttribute("taskCreate",taskService.findByCreatore(utente));
            model.addAttribute("taskAssegnate",assegnazioneService.getTaskAssegnate(utente));
            return "moduli/gtm/GestioneTask";
        }
        else return "redirect:/";
    }

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

    @PostMapping({"/dashboard/gtm","/dashboard/gtm/"})
    public String createNewTask(Principal principal, Model model, @Valid @ModelAttribute GTMForm form) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
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

    @PostMapping({"/dashboard/gtm/edit-task","/dashboard/gtm/edit-task/"})
    public String editTask(Principal principal, Model model, @Valid @ModelAttribute GTMEditForm form, BindingResult bindingResult) {
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            TaskEntity task=taskService.findById(new TaskID(form.getIdTask(),utente.getAzienda().getId_azienda()));
            if(task==null) return "redirect:/dashboard/gtm"+Alert.error("Task non trovata");
            else if(bindingResult.hasErrors()) return "moduli/gtm/GestioneTask";
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
}