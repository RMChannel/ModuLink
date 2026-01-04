package com.modulink.Controller.GDU;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;
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
import org.springframework.mail.javamail.JavaMailSenderImpl;
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


@Controller
public class GDUController {
    private final ModuloService moduloService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RuoloService ruoloService;
    private final AssociazioneService associazioneService;
    private final JavaMailSenderImpl mailSender;
    private final String senderEmail;

    public GDUController(ModuloService moduloService, CustomUserDetailsService customUserDetailsService, RuoloService ruoloService, AssociazioneService associazioneService, JavaMailSenderImpl mailSender, @Value("${spring.mail.username}") String senderEmail) {
        this.moduloService=moduloService;
        this.customUserDetailsService=customUserDetailsService;
        this.ruoloService=ruoloService;
        this.associazioneService=associazioneService;
        this.mailSender=mailSender;
        this.senderEmail=senderEmail;
    }

    private boolean isAccessibleModulo(Optional<UtenteEntity> user) {
        if(user.isEmpty()) return false;
        UtenteEntity utente=user.get();
        return moduloService.isAccessibleModulo(0, utente);
    }

    @GetMapping("dashboard/gdu/")
    public String dashboardDispatcher(Principal principal, Model model, @ModelAttribute NewUserForm newUserForm) {
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

    @Transactional
    @PostMapping("dashboard/gdu/remove-user")
    public String removeUser(Principal principal, Model model, @RequestParam("email") String email, @ModelAttribute NewUserForm newUserForm) {
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

    @NonNull
    private String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz@!";
        StringBuilder stringBuilder=new StringBuilder();
        Random random=new Random();
        for(int i=0;i<12;i++) stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        return stringBuilder.toString();
    }

    @Transactional
    @PostMapping("dashboard/gdu/add-user")
    public String addUser(Principal principal, Model model, @Valid @ModelAttribute NewUserForm newUserForm, BindingResult bindingResults) {
        String emailLogged =  principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(emailLogged);
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            List<ModuloEntity> moduli = moduloService.findModuliByUtente(utente);
            model.addAttribute("moduli", moduli != null ? moduli : List.of());
            model.addAttribute("utente", utente);
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
            UtenteEntity newUser=new UtenteEntity(azienda,newUserForm.getEmail(), PasswordUtility.hashPassword(tempPassword),newUserForm.getNome(),newUserForm.getCognome(),newUserForm.getTelefono().replaceAll(" ",""),"");
            customUserDetailsService.registraUtente(newUser,azienda.getId_azienda());
            RuoloEntity newUserRole=ruoloService.getNewUser(azienda);
            AssociazioneEntity associazione=new AssociazioneEntity(newUser,newUserRole);
            associazioneService.save(associazione);
            newUser.addRuolo(newUserRole);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(newUserForm.getEmail());
            message.setSubject("Benvenuto in "+azienda.getNome()+" "+newUserForm.getNome()+" "+newUserForm.getCognome());
            message.setText("Benvenuto "+newUserForm.getNome()+" "+newUserForm.getCognome()+" in "+azienda.getNome()+"!!!\nSei stato appena registrato alla piattaforma Modulink, per entrare utilizza questa password: "+tempPassword+"\n\nUna volta effettuato il 1°login potrai modificare la tua password con una tua personale");
            mailSender.send(message);
            utenti.add(newUser);
            model.addAttribute("utenti", utenti);
            model.addAttribute("success",true);
            return "moduli/gdu/GestioneUtenti";
        }
        else {
            return "redirect:/";
        }
    }
}
