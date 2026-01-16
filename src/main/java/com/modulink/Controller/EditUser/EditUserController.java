package com.modulink.Controller.EditUser;

import com.modulink.Model.Utente.CustomUserDetailsService;
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

@Controller
public class EditUserController {
    private final CustomUserDetailsService customUserDetailsService;

    public EditUserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService=customUserDetailsService;
    }

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

    @PostMapping("/dashboard/update-user")
    public String updateUser(Principal principal, Model model, @Valid @ModelAttribute EditUserForm editUserForm, BindingResult bindingResult) throws IOException {
        if(principal == null) return "redirect:/";
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
        customUserDetailsService.aggiornaUtente(utente);
        model.addAttribute("utente",utente);
        model.addAttribute("success",true);
        model.addAttribute("message","Utente aggiornato con successo");
        return "user/edit-profile";
    }
}
