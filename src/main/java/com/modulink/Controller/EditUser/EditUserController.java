package com.modulink.Controller.EditUser;

import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

/**
 * Controller per la gestione della modifica del profilo utente.
 * <p>
 * Fornisce le funzionalità per permettere agli utenti autenticati di aggiornare i propri dati anagrafici,
 * cambiare la password e gestire l'immagine del profilo (caricamento/rimozione).
 * Implementa logiche di validazione di sicurezza per prevenire modifiche non autorizzate (es. cambio email non consentito).
 * </p>
 *
 * @author Modulink Team
 * @version 2.2.0
 * @since 1.2.0
 */
@Controller
public class EditUserController {
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param customUserDetailsService Servizio per la gestione degli utenti.
     * @since 1.2.0
     */
    public EditUserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService=customUserDetailsService;
    }

    /**
     * Prepara e visualizza la pagina di modifica del profilo.
     * <p>
     * Popola il form con i dati attuali dell'utente loggato recuperati dal Model (popolato dall'interceptor globale).
     * </p>
     *
     * @param principal    L'utente loggato.
     * @param model        Il modello UI contenente l'utente corrente.
     * @param editUserForm Il DTO da popolare per il binding del form.
     * @return Il nome della vista "user/edit-profile" o un redirect se non autenticato.
     * @since 1.2.0
     */
    @GetMapping({"/dashboard/edit-user","/dashboard/edit-user/"})
    public String getEditUserPage(Principal principal, Model model, @ModelAttribute EditUserForm editUserForm) {
        if (principal == null) return "redirect:/";
        else {
            UtenteEntity utente = (UtenteEntity) model.getAttribute("utente");
            editUserForm.setNome(utente.getNome());
            editUserForm.setCognome(utente.getCognome());
            editUserForm.setTelefono(utente.getTelefono());
            editUserForm.setEmail(utente.getEmail());
            model.addAttribute("editUserForm",editUserForm);
            return "user/edit-profile";
        }
    }

    /**
     * Processa la richiesta di aggiornamento dei dati utente.
     * <p>
     * Esegue diverse operazioni critiche:
     * <ul>
     *     <li>Validazione dei campi obbligatori e del formato (telefono, lunghezza stringhe).</li>
     *     <li>Verifica della corrispondenza delle password se viene richiesto un cambio password.</li>
     *     <li>Controllo di integrità sull'email (non modificabile).</li>
     *     <li>Gestione del filesystem per l'upload o la rimozione dell'immagine di profilo.</li>
     *     <li>Aggiornamento persistente dell'entità utente.</li>
     * </ul>
     *
     *
     * @param principal     Utente loggato.
     * @param model         Modello UI.
     * @param editUserForm  DTO con i nuovi dati inviati.
     * @param bindingResult Risultati della validazione standard.
     * @return Ricarica la pagina con messaggio di successo o errore.
     * @throws IOException In caso di errori I/O durante la gestione del file immagine.
     * @since 1.2.0
     */
    @PostMapping("/dashboard/update-user")
    public String updateUser(Principal principal, Model model, @Valid @ModelAttribute EditUserForm editUserForm, BindingResult bindingResult) throws IOException {
        if(principal == null) return "redirect:/";
        if(!editUserForm.getPassword().isEmpty()) {
            if(editUserForm.getPassword().length()<8 || editUserForm.getPassword().length()>50) bindingResult.rejectValue("password","password.length","La password deve essere compresa tra 8 e 50 caratteri.");
            if(editUserForm.getConfirmPassword().length()<8 || editUserForm.getPassword().length()>50) bindingResult.rejectValue("confirmPassword","confirmPassword.length","La password deve essere compresa tra 8 e 50 caratteri.");
            if(!editUserForm.getPassword().equals(editUserForm.getConfirmPassword())) bindingResult.rejectValue("confirmPassword","confirmPassword.notequal","Le password non corrispondono.");
        }
        else if(bindingResult.hasErrors()) return "user/edit-profile";
        UtenteEntity utente = (UtenteEntity) model.getAttribute("utente");
        if(!editUserForm.getEmail().equals(utente.getEmail())) {
            bindingResult.rejectValue("email","email.notequal","L'email inserita non corrisponde all'email registrata.");
            return "user/edit-profile";
        }
        else if(editUserForm.isRemoveImageFlag()) {
            if(utente.getPath_immagine_profilo()!=null && !utente.getPath_immagine_profilo().isEmpty()) Files.delete(Paths.get(utente.getPath_immagine_profilo()));
            utente.setPath_immagine_profilo(null);
        }
        else if(editUserForm.getImmagineProfilo().getBytes().length!=0) { //Salva o modifica l'immagine di profilo dell'utente
            String filename="";
            if(utente.getPath_immagine_profilo()!=null && !utente.getPath_immagine_profilo().isEmpty()) Files.delete(Paths.get(utente.getPath_immagine_profilo()));
            String logodir="user-logos/";
            Path uploadPath = Paths.get(logodir);
            if(!uploadPath.toFile().exists()) uploadPath.toFile().mkdirs();
            filename=editUserForm.getEmail()+editUserForm.getImmagineProfilo().getOriginalFilename();
            Path filePath=uploadPath.resolve(filename);
            if(Files.exists(filePath)) Files.delete(filePath);
            Files.write(filePath,editUserForm.getImmagineProfilo().getBytes());
            filename=logodir+filename;
            utente.setPath_immagine_profilo(filename);
        }
        utente.setNome(editUserForm.getNome());
        utente.setCognome(editUserForm.getCognome());
        utente.setTelefono(editUserForm.getTelefono().replaceAll(" ",""));
        if(!editUserForm.getPassword().isEmpty()) utente.setHash_password(PasswordUtility.hashPassword(editUserForm.getPassword()));
        customUserDetailsService.aggiornaUtente(utente);
        model.addAttribute("utente",utente);
        model.addAttribute("success",true);
        model.addAttribute("message","Utente aggiornato con successo");
        return "user/edit-profile";
    }
}
