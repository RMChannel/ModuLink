package com.modulink.Controller.UserModules.GDU;

import com.modulink.Alert;
import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Email.EmailService;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneService;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;


/**
 * Controller per il modulo <strong>GDU (Gestione Dati Utenti)</strong>.
 * <p>
 * Questo componente rappresenta il pannello di controllo per la gestione del personale aziendale.
 * Implementa le funzionalità CRUD complete per gli account utente: registrazione nuovi dipendenti,
 * modifica dei profili esistenti, rimozione utenti e gestione del primo accesso (First Login).
 * Estende {@link ModuloController} (ID Modulo: 0 - modulo core) per la gestione dei permessi.
 * </p>
 *
 * @author Modulink Team
 * @version 3.0.1
 * @since 1.3.0
 */
@Controller
public class GDUController extends ModuloController {
    private final CustomUserDetailsService customUserDetailsService;
    private final RuoloService ruoloService;
    private final AssociazioneService associazioneService;
    private final String senderEmail;
    private final EmailService emailService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param moduloService            Servizio moduli.
     * @param customUserDetailsService Servizio utenti.
     * @param ruoloService             Servizio ruoli.
     * @param associazioneService      Servizio associazioni utente-ruolo.
     * @param senderEmail              Email mittente per notifiche.
     * @param emailService             Servizio invio email.
     * @since 1.3.0
     */
    public GDUController(ModuloService moduloService, CustomUserDetailsService customUserDetailsService, RuoloService ruoloService, AssociazioneService associazioneService, @Value("${spring.mail.properties.mail.smtp.from}") String senderEmail, EmailService emailService) {
        super(moduloService, 0);
        this.customUserDetailsService=customUserDetailsService;
        this.ruoloService=ruoloService;
        this.associazioneService=associazioneService;
        this.senderEmail=senderEmail;
        this.emailService=emailService;
    }

    /**
     * Visualizza la dashboard di gestione utenti.
     * <p>
     * Carica l'elenco di tutti gli utenti appartenenti all'azienda dell'utente loggato.
     * </p>
     *
     * @param principal    Identità utente.
     * @param model        Modello UI.
     * @param newUserForm  DTO per nuovo utente.
     * @param editUserForm DTO per modifica utente.
     * @return Vista "moduli/gdu/GestioneUtenti" o redirect.
     * @since 1.3.0
     */
    @GetMapping({"dashboard/gdu/","dashboard/gdu"})
    public String dashboardDispatcher(Principal principal, Model model, @ModelAttribute NewUserForm newUserForm, @ModelAttribute EditUserForm editUserForm) {
        String email =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            model.addAttribute("utenti", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
            return "moduli/gdu/GestioneUtenti";

        }else  {
            return "redirect:/";
        }
    }

    /**
     * Rimuove un utente dal sistema aziendale.
     * <p>
     * Esegue controlli di sicurezza per impedire:
     * <ul>
     *     <li>L'eliminazione di utenti appartenenti ad altre aziende (Cross-Tenant check).</li>
     *     <li>L'auto-eliminazione dell'utente loggato.</li>
     * </ul>
     * La cancellazione è transazionale e rimuove a cascata le associazioni correlate.
     *
     *
     * @param principal Identità richiedente.
     * @param model     Modello UI.
     * @param email     Email dell'utente da eliminare.
     * @param newUserForm DTO placeholder.
     * @param editUserForm DTO placeholder.
     * @return Redirect alla dashboard GDU con esito.
     * @since 1.3.0
     */
    @Transactional
    @PostMapping("dashboard/gdu/remove-user")
    public String removeUser(Principal principal, Model model, @RequestParam("email") String email, @ModelAttribute NewUserForm newUserForm, @ModelAttribute EditUserForm editUserForm) {
        String emailLogged =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(emailLogged);
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
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
            List<UtenteEntity> utenti=customUserDetailsService.getAllByAzienda(utente.getAzienda());
            utenti.remove(utenteToDelete);
            customUserDetailsService.rimuoviUtente(utenteToDelete); //Cancello l'utente e tutte le sue associazioni
            model.addAttribute("utenti", utenti);
            model.addAttribute("success",true);
            model.addAttribute("message","L'utente "+utenteToDelete.getNome()+" "+utenteToDelete.getCognome()+" è stato eliminato con successo");
            return "moduli/gdu/GestioneUtenti";
        }
        else {
            return "redirect:/";
        }
    }

    /**
     * Genera una password casuale sicura di 12 caratteri.
     *
     * @return Stringa password.
     */
    @NonNull
    private String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz@!";
        StringBuilder stringBuilder=new StringBuilder();
        Random random=new Random();
        for(int i=0;i<12;i++) stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        return stringBuilder.toString();
    }

    /**
     * Crea un nuovo account utente aziendale.
     * <p>
     * Esegue i seguenti passaggi:
     * <ol>
     *     <li>Genera una password temporanea sicura.</li>
     *     <li>Crea l'entità {@link UtenteEntity} e la associa all'azienda.</li>
     *     <li>Assegna il ruolo predefinito ("New User") per forzare il primo accesso.</li>
     *     <li>Invia una mail di benvenuto con le credenziali temporanee.</li>
     * </ol>
     *
     *
     * @param principal      Identità amministratore.
     * @param model          Modello UI.
     * @param newUserForm    DTO dati nuovo utente.
     * @param bindingResults Esito validazione.
     * @param editUserForm   DTO placeholder.
     * @return Redirect alla dashboard GDU con esito.
     * @since 1.3.0
     */
    @Transactional
    @PostMapping("dashboard/gdu/add-user")
    public String addUser(Principal principal, Model model, @Valid @ModelAttribute NewUserForm newUserForm, BindingResult bindingResults, @ModelAttribute EditUserForm editUserForm) {
        String emailLogged =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(emailLogged);
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            if(bindingResults.hasErrors()) {
                model.addAttribute("utenti", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
                model.addAttribute("newUserForm",newUserForm);
                return "moduli/gdu/GestioneUtenti";
            }
            try {
                customUserDetailsService.loadUserByUsername(newUserForm.getEmail());
                bindingResults.rejectValue("email","mail.found","La mail inserita risulta già registrata");
                model.addAttribute("newUserForm", newUserForm);
                return "moduli/gdu/GestioneUtenti";
            } catch (UsernameNotFoundException ignored) {}
            List<UtenteEntity> utenti=customUserDetailsService.getAllByAzienda(utente.getAzienda());
            AziendaEntity azienda=utente.getAzienda();
            String tempPassword=generatePassword();
            //Creo l'utente
            UtenteEntity newUser=new UtenteEntity(azienda,newUserForm.getEmail(), PasswordUtility.hashPassword(tempPassword),newUserForm.getNome(),newUserForm.getCognome(),newUserForm.getTelefono().replaceAll(" ",""),"");
            customUserDetailsService.registraUtente(newUser,azienda.getId_azienda());
            //Recupero il ruolo di default
            RuoloEntity newUserRole=ruoloService.getNewUser(azienda);
            //Creo l'associazione e unisco il ruolo all'utente
            AssociazioneEntity associazione=new AssociazioneEntity(newUser,newUserRole);
            associazioneService.save(associazione);
            newUser.addRuolo(newUserRole);
            //Scrivo l'email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(newUserForm.getEmail());
            message.setSubject("Benvenuto in "+azienda.getNome()+" "+newUserForm.getNome()+" "+newUserForm.getCognome());
            message.setText("Benvenuto "+newUserForm.getNome()+" "+newUserForm.getCognome()+" in "+azienda.getNome()+"!!!\nSei stato appena registrato alla piattaforma Modulink, per entrare utilizza questa password: "+tempPassword+"\n\nUna volta effettuato il 1°login potrai modificare la tua password con una tua personale");
            emailService.sendEmail(message);

            //Aggiungo l'utente alla lista degli utenti salvati prima così da poterli mostrare in grafica post-richiesta (Altrimenti la persistence non ha il tempo di salvare, separando le richieste si risolve il problema)
            utenti.add(newUser);
            model.addAttribute("utenti", utenti);
            //Completo la richiesta
            model.addAttribute("success",true);
            return "moduli/gdu/GestioneUtenti";
        }
        else {
            return "redirect:/";
        }
    }

    /**
     * Modifica i dati di un utente esistente.
     * <p>
     * Permette all'amministratore di aggiornare anagrafica e contatti dei dipendenti.
     * Include la possibilità di forzare un reset della password amministrativo.
     * </p>
     *
     * @param principal      Identità amministratore.
     * @param model          Modello UI.
     * @param editUserForm   DTO dati modificati.
     * @param bindingResults Esito validazione.
     * @param newUserForm    DTO placeholder.
     * @return Redirect alla dashboard GDU con esito.
     * @since 1.3.0
     */
    @PostMapping("dashboard/gdu/modify-user")
    public String editUser(Principal principal, Model model, @Valid @ModelAttribute EditUserForm editUserForm, BindingResult bindingResults, @ModelAttribute NewUserForm newUserForm) {
        String emailLogged =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(emailLogged);
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            if(bindingResults.hasErrors()) {
                model.addAttribute("utenti", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
                model.addAttribute("editUserForm",editUserForm);
                return "moduli/gdu/GestioneUtenti";
            }
            Optional<UtenteEntity> toUpdateUserOpt = customUserDetailsService.findByEmail(editUserForm.getOldmail());
            if(toUpdateUserOpt.isEmpty()) {
                model.addAttribute("error",true);
                model.addAttribute("message","L'utente indicato non è stato trovato");
                model.addAttribute("utenti", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
                return "moduli/gdu/GestioneUtenti";
            }
            UtenteEntity toUpdateUser = toUpdateUserOpt.get();
            if(toUpdateUser.getAzienda().getId_azienda()!=utente.getAzienda().getId_azienda()) {
                System.err.println("Sto stronzo sta provando ad aggiornare un cristiano non suo");
                return "redirect:/dashboard";
            }
            //Controllo i parametri password se presenti
            if(!editUserForm.getNewPassword().isEmpty()) {
                if(!editUserForm.getNewPassword().equals(editUserForm.getConfirmNewPassword())) {
                    model.addAttribute("error",true);
                    model.addAttribute("message","Le password non coincidono, controlla e riprova");
                    model.addAttribute("utenti", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
                    return "moduli/gdu/GestioneUtenti";
                }
                else if(editUserForm.getNewPassword().length()<8 || editUserForm.getNewPassword().length()>50) {
                    model.addAttribute("error",true);
                    model.addAttribute("message","La password dev'essere compresa tra gli 8 e i 50 caratteri");
                    model.addAttribute("utenti", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
                    return "moduli/gdu/GestioneUtenti";
                }
                else {
                    toUpdateUser.setHash_password(PasswordUtility.hashPassword(editUserForm.getNewPassword()));
                }
            }
            toUpdateUser.setNome(editUserForm.getNome());
            toUpdateUser.setCognome(editUserForm.getCognome());
            toUpdateUser.setTelefono(editUserForm.getTelefono().replaceAll(" ",""));
            toUpdateUser.setEmail(editUserForm.getEmail());
            customUserDetailsService.aggiornaUtente(toUpdateUser);
            model.addAttribute("success",true);
            model.addAttribute("message","L'utente "+toUpdateUser.getNome()+" "+toUpdateUser.getCognome()+" è stato aggiornato con successo");
            model.addAttribute("utenti", customUserDetailsService.getAllByAzienda(utente.getAzienda()));
            return "moduli/gdu/GestioneUtenti";
        }
        else {
            return "redirect:/";
        }
    }

    /**
     * Gestisce la procedura di primo accesso (First Login).
     * <p>
     * Imposta la password definitiva scelta dall'utente e assegna i ruoli standard,
     * completando l'attivazione dell'account.
     * </p>
     *
     * @param principal       Identità utente.
     * @param model           Modello UI.
     * @param password        Nuova password.
     * @param confirmPassword Conferma password.
     * @return Redirect alla dashboard o errore in pagina.
     * @since 1.3.0
     */
    @PostMapping("dashboard/gdu/firstlogin")
    public String registerNewUser(Principal principal, Model model, @RequestParam String password, @RequestParam String confirmPassword) {
        String emailLogged =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(emailLogged);
        if(utenteOpt.isPresent()) {
            UtenteEntity utente=utenteOpt.get();
            if(!customUserDetailsService.isThisaNewUtente(utente)) {
                return "redirect:/";
            }
            if(!password.equals(confirmPassword)) {
                model.addAttribute("error",true);
                model.addAttribute("message","Le password non coincidono, controlla e riprova");
                model.addAttribute("utente",utente);
                return "user/firstlogin";
            }
            else if(password.length()<8 || password.length()>50) {
                model.addAttribute("error",true);
                model.addAttribute("message","La password dev'essere compresa tra gli 8 e i 50 caratteri");
                model.addAttribute("utente",utente);
                return "user/firstlogin";
            }
            utente.setHash_password(PasswordUtility.hashPassword(password));

            utente.defaultRoles(ruoloService.getStandardUser(utente.getAzienda()));
            customUserDetailsService.aggiornaUtente(utente);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(utente.getEmail());
            message.setSubject("Registrazione effettuata con successo!");
            message.setText("Salve "+utente.getNome()+" "+utente.getCognome()+", ti confermiamo che il tuo account è stato registrato con successo alla piattaforma Modulink in collab. con "+utente.getAzienda().getNome()+".\n\n\nOra puoi accedere alla tua dashboard, se non visualizi alcun modulo contatta il tuo responsabile.");
            emailService.sendEmail(message);
            return "redirect:/dashboard"+ Alert.success("Registrazione effettuata correttamente, benvenuto in Modulink in collab. con "+utente.getAzienda().getNome());
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

