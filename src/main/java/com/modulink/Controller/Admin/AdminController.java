package com.modulink.Controller.Admin;

import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaService;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard/admin")
public class AdminController extends ModuloController {

    private final CustomUserDetailsService customUserDetailsService;
    private final AziendaService aziendaService;

    public AdminController(ModuloService moduloService, CustomUserDetailsService customUserDetailsService, AziendaService aziendaService) {
        super(moduloService, -1);
        this.customUserDetailsService = customUserDetailsService;
        this.aziendaService = aziendaService;
    }

    @GetMapping({"/", ""})
    public String index(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();
        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(email);
        
        if (!isAccessibleModulo(currentUserOpt)) {
            return "redirect:/dashboard";
        }

        List<AziendaEntity> aziende = aziendaService.getAllAziende();
        // Prevent admin from deleting their own company (if applicable) or the main admin company
        currentUserOpt.ifPresent(utenteEntity -> {
            if(utenteEntity.getAzienda() != null) {
                 aziende.removeIf(a -> a.getId_azienda() == utenteEntity.getAzienda().getId_azienda());
            }
        });

        model.addAttribute("aziende", aziende);
        return "admin/adminPage";
    }

    @PostMapping("/azienda/save")
    public String saveAzienda(@ModelAttribute AziendaEntity formAzienda, RedirectAttributes redirectAttributes) {
        try {
            if (formAzienda.getId_azienda() == 0) {
                // New Azienda
                aziendaService.registraAzienda(formAzienda);
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "Azienda creata con successo.");
            } else {
                // Update Azienda
                Optional<AziendaEntity> existingOpt = aziendaService.getAziendaById(formAzienda.getId_azienda());
                if (existingOpt.isPresent()) {
                    AziendaEntity existing = existingOpt.get();
                    existing.setNome(formAzienda.getNome());
                    existing.setPiva(formAzienda.getPiva());
                    existing.setIndirizzo(formAzienda.getIndirizzo());
                    existing.setCitta(formAzienda.getCitta());
                    existing.setCap(formAzienda.getCap());
                    existing.setTelefono(formAzienda.getTelefono());
                    existing.setLogo(formAzienda.getLogo());
                    
                    aziendaService.updateAzienda(existing);
                    redirectAttributes.addFlashAttribute("success", true);
                    redirectAttributes.addFlashAttribute("message", "Azienda aggiornata con successo.");
                } else {
                    redirectAttributes.addFlashAttribute("error", true);
                    redirectAttributes.addFlashAttribute("message", "Azienda non trovata.");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Errore durante il salvataggio: " + e.getMessage());
        }
        return "redirect:/dashboard/admin/";
    }

    @PostMapping("/azienda/delete")
    public String deleteAzienda(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        try {
            aziendaService.deleteAzienda(id);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Azienda eliminata con successo.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Errore durante l'eliminazione dell'azienda: " + e.getMessage());
        }
        return "redirect:/dashboard/admin/";
    }
}