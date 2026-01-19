package com.modulink.Controller.UserModules.GDR;

import com.modulink.Controller.ModuloController;
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

/**
 * Controller per il modulo <strong>GDR (Gestione Del Responsabile)</strong>.
 * <p>
 * Questo controller permette agli utenti autorizzati (es. amministratori aziendali) di visualizzare
 * e modificare i dati anagrafici della propria azienda. Include la gestione del caricamento del logo
 * e la verifica dei vincoli di unicità sui dati sensibili (P.IVA, Telefono).
 * </p>
 * <p>
 * Estende {@link ModuloController} (ID Modulo: 3) per l'integrazione con il sistema di gestione moduli.
 * </p>
 *
 * @author Modulink Team
 * @version 1.8.3
 * @since 1.2.0
 */
@Controller
@RequestMapping("/dashboard/gdr")
public class AziendaCotroller extends ModuloController {
    private final CustomUserDetailsService customUserDetailsService;
    private final ModuloService moduloService;
    private final AziendaService aziendaService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param customUserDetailsService Servizio recupero utenti.
     * @param moduloService            Servizio moduli.
     * @param aziendaService           Servizio gestione aziende.
     * @since 1.2.0
     */
    public AziendaCotroller(CustomUserDetailsService customUserDetailsService, ModuloService moduloService, AziendaService aziendaService) {
        super(moduloService, 3);
        this.customUserDetailsService = customUserDetailsService;
        this.moduloService = moduloService;
        this.aziendaService = aziendaService;
    }

    /**
     * Prepara la vista per la modifica dei dati aziendali.
     * <p>
     * Recupera i dati attuali dell'azienda associata all'utente loggato e popola il form {@link EditAziendaForm}.
     * </p>
     *
     * @param principal Identità dell'utente loggato.
     * @param model     Modello UI.
     * @return Vista "moduli/gdr/editAzienda" o redirect se non autorizzato.
     * @since 1.2.0
     */
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

            EditAziendaForm form = new EditAziendaForm();
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


    /**
     * Processa la richiesta di aggiornamento dei dati aziendali.
     * <p>
     * Esegue validazioni critiche:
     * <ul>
     *     <li>Unicità della P.IVA rispetto ad altre aziende nel sistema.</li>
     *     <li>Unicità del numero di telefono (normalizzato).</li>
     *     <li>Gestione del logo: cancellazione vecchio file e salvataggio nuovo upload.</li>
     * </ul>
     *
     *
     * @param principal Identità dell'utente.
     * @param model     Modello UI.
     * @param form      DTO con i nuovi dati aziendali.
     * @param result    Risultati validazione form.
     * @return Redirect alla stessa pagina con messaggio di esito.
     * @throws IOException In caso di errori I/O sul filesystem.
     * @since 1.2.0
     */
    @PostMapping("/edit")
    public String edit(Principal principal, Model model, @Valid @ModelAttribute("registerAziendaForm") EditAziendaForm form, BindingResult result) throws IOException {
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
            if (form.isDeleteFoto()){
                if(azienda.getLogo()!=null && !azienda.getLogo().isEmpty()) {
                    Files.deleteIfExists(Paths.get(azienda.getLogo()));
                }
                azienda.setLogo("");
            }

            MultipartFile file = form.getLogo();
            if (file != null && !file.isEmpty()) {
                String logodir = "azienda-logos/";
                Path uploadPath = Paths.get(logodir);
                if (!uploadPath.toFile().exists()) uploadPath.toFile().mkdirs();
                
                String filename = form.getPiva() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                
                // If we are replacing an existing logo, delete the old one
                if (!form.isDeleteFoto() && azienda.getLogo() != null && !azienda.getLogo().isEmpty()) {
                    Files.deleteIfExists(Paths.get(azienda.getLogo()));
                }

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

    /**
     * Inibisce la disinstallazione del modulo GDR.
     * Essendo il modulo che gestisce l'identità aziendale, non può essere rimosso.
     *
     * @param azienda Azienda target.
     * @since 1.2.0
     */
    //Modulo non disinstallabile
    @Override
    public void disinstallaModulo(AziendaEntity azienda) {
        System.err.println("Modulo non disinstallabile");
        return;
    }
}