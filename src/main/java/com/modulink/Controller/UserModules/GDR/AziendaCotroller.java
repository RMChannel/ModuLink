package com.modulink.Controller.UserModules.GDR;

import com.modulink.Controller.ModuloController;
import com.modulink.Controller.Register.RegisterAziendaForm;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaService;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard/gdr")
public class AziendaCotroller extends ModuloController {
    private final CustomUserDetailsService customUserDetailsService;
    private final ModuloService moduloService;
    private final AziendaService aziendaService;

    public AziendaCotroller(CustomUserDetailsService customUserDetailsService, ModuloService moduloService, AziendaService aziendaService) {
        super(moduloService, 3);
        this.customUserDetailsService = customUserDetailsService;
        this.moduloService = moduloService;
        this.aziendaService = aziendaService;
    }

    @GetMapping("/")
    public String DatiAzienda(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }
        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            AziendaEntity azienda = utente.getAzienda();

            RegisterAziendaForm form = new RegisterAziendaForm();
            form.setNomeAzienda(azienda.getNome());
            form.setPiva(azienda.getPiva());
            form.setIndirizzo(azienda.getIndirizzo());
            form.setCitta(azienda.getCitta());
            form.setCap(azienda.getCap());
            form.setTelefono(azienda.getTelefono());
            
            model.addAttribute("registerAziendaForm", form);
            model.addAttribute("currentLogoPath", azienda.getLogo());
            return "moduli/gdr/editAzienda";
        } else {
            return "redirect:/";
        }
    }


    @PostMapping("/edit")
    public String edit(Principal principal, Model model, @Valid @ModelAttribute("registerAziendaForm") RegisterAziendaForm form, BindingResult result) throws IOException {
        if (principal == null) {
            return "redirect:/";
        }
        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);

        if (isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente = utenteOpt.get();
            AziendaEntity azienda = utente.getAzienda();

            // Check PIVA Uniqueness (if changed)
            if (!azienda.getPiva().equals(form.getPiva())) {
                AziendaEntity existing = aziendaService.getAziendaByPIVA(form.getPiva());
                if (existing != null && existing.getId_azienda() != azienda.getId_azienda()) {
                     result.rejectValue("piva", "piva.found", "La partita IVA inserita risulta già registrata");
                }
            }

            // Check Telefono Uniqueness (if changed)
            String normalizedPhone = form.getTelefono().replaceAll(" ", "");
            form.setTelefono(normalizedPhone); 
            
            if (!azienda.getTelefono().equals(normalizedPhone)) {
                if (aziendaService.findByTelefono(normalizedPhone)) {
                     result.rejectValue("telefono", "telefono.found", "Il telefono inserito risulta già registrato da un'altra azienda");
                }
            }

            if (result.hasErrors()) {
                model.addAttribute("currentLogoPath", azienda.getLogo());
                return "moduli/gdr/editAzienda";
            }

            // Update Fields
            azienda.setNome(form.getNomeAzienda());
            azienda.setPiva(form.getPiva());
            azienda.setIndirizzo(form.getIndirizzo());
            azienda.setCitta(form.getCitta());
            azienda.setCap(form.getCap());
            azienda.setTelefono(form.getTelefono());

            // Handle Logo
            MultipartFile file = form.getLogo();
            if (file != null && !file.isEmpty()) {
                String logodir = "azienda-logos/";
                Path uploadPath = Paths.get(logodir);
                if (!uploadPath.toFile().exists()) uploadPath.toFile().mkdirs();
                
                String filename = form.getPiva() + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                
                if (Files.exists(filePath)) Files.delete(filePath);
                Files.write(filePath, file.getBytes());
                
                azienda.setLogo(logodir + filename);
            }

            aziendaService.updateAzienda(azienda);
            
            model.addAttribute("success", true);
            model.addAttribute("message", "Dati aziendali aggiornati con successo");
            model.addAttribute("currentLogoPath", azienda.getLogo());
            
            return "moduli/gdr/editAzienda";
            
        } else {
            return "redirect:/";
        }
    }
}