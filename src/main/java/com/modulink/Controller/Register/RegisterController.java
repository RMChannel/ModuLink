package com.modulink.Controller.Register;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaService;
import com.modulink.Model.Relazioni.Pertinenza.PertinenzaService;
import com.modulink.Model.Relazioni.Associazione.AssociazioneService;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneService;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import com.modulink.Model.Ruolo.RuoloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/**
 * Controller Spring MVC che gestisce il flusso di registrazione (Onboarding) di una nuova Azienda e del suo Responsabile.
 * <p>
 * Il processo è strutturato come un <strong>Wizard in due passaggi</strong>:
 * <ol>
 * <li>Registrazione dati Azienda (Anagrafica, Logo, P.IVA, Contatti).</li>
 * <li>Registrazione dati Utente Responsabile (Credenziali, Profilo).</li>
 * </ol>
 * <p>
 * La classe utilizza l'annotazione {@link SessionAttributes} per mantenere persistente l'oggetto
 * {@link RegisterAziendaForm} tra le varie richieste HTTP finché l'intero processo non viene completato.
 *
 * @author Modulink Team
 * @version 1.2
 */
@Controller
@SessionAttributes("registerAziendaForm")
public class RegisterController {
    private final CustomUserDetailsService userDetailsService;
    private final AziendaService aziendaService;
    private final RuoloService ruoloService;
    private final AssociazioneService associazioneService;
    private final AttivazioneService attivazioneService;
    private final PertinenzaService pertinenzaService;
    private final List<String> supportedImageTypes = Arrays.asList("image/jpeg", "image/png");

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param userDetailsService     Servizio per la gestione degli utenti e autenticazione.
     * @param aziendaService         Servizio per la logica di business relativa alle aziende.
     * @param ruoloService        Repository per la persistenza dei ruoli.
     * @param associazioneService Repository per collegare utenti e ruoli.
     */
    public RegisterController(CustomUserDetailsService userDetailsService, AziendaService aziendaService, RuoloService ruoloService, AssociazioneService associazioneService, AttivazioneService attivazioneService, PertinenzaService pertinenzaService) {
        this.userDetailsService = userDetailsService;
        this.aziendaService = aziendaService;
        this.ruoloService = ruoloService;
        this.associazioneService = associazioneService;
        this.attivazioneService = attivazioneService;
        this.pertinenzaService = pertinenzaService;
    }

    /**
     * Inizializza il processo di registrazione (Step 1).
     * <p>
     * Se l'utente è già autenticato, viene reindirizzato alla home.
     * Altrimenti, inizializza il form per i dati aziendali.
     *
     * @param model     Il modello per la vista.
     * @param principal L'utente attualmente loggato (se presente).
     * @return Il nome della vista Thymeleaf per la registrazione azienda.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model, Principal principal) {
        if(principal != null) {return "redirect:/home";}
        model.addAttribute("registerAziendaForm", new RegisterAziendaForm());
        return "register/RegistraAzienda";
    }

    /**
     * Elabora i dati dell'Azienda inviati (Step 1 -> Step 2).
     * <p>
     * Esegue le seguenti operazioni:
     * <ul>
     * <li>Validazione formale dei dati (JSR-380).</li>
     * <li>Verifica univocità della Partita IVA nel database.</li>
     * <li><strong>Normalizzazione del numero di telefono</strong> (rimozione spazi bianchi) e verifica della sua univocità nel sistema.</li>
     * <li>Conversione temporanea del file Logo in array di byte per conservarlo in sessione.</li>
     * </ul>
     * Se tutto è corretto, prepara il model per il secondo step (Registrazione Utente).
     *
     * @param model               Il modello per la vista.
     * @param principal           L'utente loggato.
     * @param registerAziendaForm Il DTO popolato con i dati dell'azienda.
     * @param bindingResult       Risultato della validazione.
     * @return La vista successiva (RegistraUtente) o la stessa in caso di errori.
     * @throws IOException Se si verifica un errore nella lettura del file logo.
     */
    @PostMapping("/register")
    public String registerAzienda(Model model, Principal principal, @Valid @ModelAttribute("registerAziendaForm") RegisterAziendaForm registerAziendaForm, BindingResult bindingResult) throws IOException {
        if (principal != null) {return "redirect:/home";}
        else {
            if(bindingResult.hasErrors()) {
                model.addAttribute("registerAziendaForm", registerAziendaForm);
                return "register/RegistraAzienda";
            }
            else if(aziendaService.getAziendaByPIVA(registerAziendaForm.getPiva())!=null) {
                bindingResult.rejectValue("piva","piva.found","La partita IVA inserita risulta già registrata");
                model.addAttribute("registerAziendaForm", registerAziendaForm);
                return "register/RegistraAzienda";
            }
            else {
                // Normalizza il telefono rimuovendo gli spazi e verifica univocità
                registerAziendaForm.setTelefono(registerAziendaForm.getTelefono().replaceAll(" ",""));
                if(aziendaService.findByTelefono(registerAziendaForm.getTelefono())) {
                    bindingResult.rejectValue("telefono","telefono.found","Il telefono inserito risulta già registrato da un'altra azienda");
                    model.addAttribute("registerAziendaForm", registerAziendaForm);
                    return "register/RegistraAzienda";
                }
                MultipartFile file = registerAziendaForm.getLogo();
                if(file!=null && !file.isEmpty()) {
                    registerAziendaForm.setLogoBytes(file.getBytes());
                    registerAziendaForm.setLogoFileName(file.getOriginalFilename());
                }
                model.addAttribute("registerUtenteForm", new RegisterResponsabileForm());
                model.addAttribute("firstAccess", true);
                return "register/RegistraUtente";
            }
        }
    }

    /**
     * Gestisce i tentativi di accesso diretto allo Step 2 via GET.
     * <p>
     * Reindirizza sempre all'inizio del processo per garantire che i dati aziendali siano presenti.
     *
     * @return Redirect alla pagina di registrazione principale.
     */
    @GetMapping("/register-utente")
    public String redirectToRegisterUtente() {
        return "redirect:/register";
    }

    /**
     * Finalizza la registrazione completa (Azienda + Responsabile).
     * <p>
     * Metodo annotato come {@link Transactional}: garantisce che il salvataggio di Azienda, Utente,
     * Ruolo e Associazione avvenga in modo atomico (tutto o niente).
     * <p>
     * Flusso operativo:
     * <ol>
     * <li>Verifica congruenza password e univocità email.</li>
     * <li>Salvataggio fisico del logo Azienda su filesystem.</li>
     * <li>Persistenza {@link AziendaEntity}.</li>
     * <li>Salvataggio fisico foto profilo Utente su filesystem.</li>
     * <li>Persistenza {@link UtenteEntity} (con password hashata).</li>
     * <li>Creazione e assegnazione automatica del ruolo "Responsabile".</li>
     * <li>Pulizia della sessione (rimozione attributi temporanei).</li>
     * </ol>
     *
     * @param registerAziendaForm DTO Azienda recuperato dalla sessione.
     * @param model               Il modello.
     * @param principal           L'utente loggato.
     * @param registerUtenteForm  DTO Utente inviato dal form.
     * @param bindingResult       Risultati validazione utente.
     * @param sessionStatus       Oggetto per segnare la sessione come completata.
     * @return Redirect alla pagina di login in caso di successo.
     * @throws IOException Se fallisce la scrittura dei file su disco.
     */
    @PostMapping("/register-utente")
    @Transactional
    public String registerUtente(@ModelAttribute("registerAziendaForm") RegisterAziendaForm registerAziendaForm, Model model, Principal principal, @Valid @ModelAttribute("registerUtenteForm") RegisterResponsabileForm registerUtenteForm, BindingResult bindingResult, SessionStatus sessionStatus) throws IOException {
        if (principal != null) {return "redirect:/home";}
        else if(registerAziendaForm == null) {
            return "redirect:/register";
        }
        else {
            if (registerUtenteForm.getImmagineProfilo() != null && !registerUtenteForm.getImmagineProfilo().isEmpty()) {
                if (!supportedImageTypes.contains(registerUtenteForm.getImmagineProfilo().getContentType())) {
                    bindingResult.rejectValue("immagineProfilo", "error.immagineProfilo", "Il file caricato deve essere un'immagine (JPEG o PNG)");
                    model.addAttribute("registerUtenteForm", registerUtenteForm);
                    return "register/RegistraUtente";
                }
                if (registerUtenteForm.getImmagineProfilo().getSize() > 12 * 1024 * 1024) {
                    bindingResult.rejectValue("immagineProfilo", "size.exceeded", "La dimensione dell’immagine supera 12MB");
                    model.addAttribute("registerUtenteForm", registerUtenteForm);
                    return "register/RegistraUtente";
                }
            }
            if(bindingResult.hasErrors()) {
                model.addAttribute("registerUtenteForm", registerUtenteForm);
                return "register/RegistraUtente";
            }
            else if (!registerUtenteForm.getPassword().equals(registerUtenteForm.getConfermaPassword())) {
                bindingResult.rejectValue("confermaPassword", "error.password", "Le password non corrispondono");
                model.addAttribute("registerUtenteForm", registerUtenteForm);
                return "register/RegistraUtente";
            }
            else {
                // Normalizza il telefono rimuovendo gli spazi
                registerUtenteForm.setTelefonoutente(registerUtenteForm.getTelefonoutente().replaceAll(" ",""));
                try {
                    userDetailsService.loadUserByUsername(registerUtenteForm.getEmail());
                    bindingResult.rejectValue("email","mail.found","La mail inserita risulta già registrata");
                    model.addAttribute("registerUtenteForm", registerUtenteForm);
                    return "register/RegistraUtente";
                } catch (UsernameNotFoundException ignored) {}
                String filename="";
                if(registerAziendaForm.getLogoBytes()!=null) { //Salva il logo dell'azienda
                    String logodir="azienda-logos/";
                    Path uploadPath = Paths.get(logodir);
                    if(!uploadPath.toFile().exists()) uploadPath.toFile().mkdirs();
                    filename=registerAziendaForm.getPiva()+registerAziendaForm.getLogoFileName();
                    Path filePath=uploadPath.resolve(filename);
                    if(Files.exists(filePath)) Files.delete(filePath);
                    Files.write(filePath,registerAziendaForm.getLogoBytes());
                    filename=logodir+filename;
                }
                //Salva l'azienda
                AziendaEntity aziendaEntity = new AziendaEntity(registerAziendaForm.getNomeAzienda(),registerAziendaForm.getPiva(),registerAziendaForm.getIndirizzo(),registerAziendaForm.getCitta(),registerAziendaForm.getCap(),registerAziendaForm.getTelefono(),filename);
                aziendaEntity=aziendaService.registraAzienda(aziendaEntity);

                filename="";
                if(registerUtenteForm.getImmagineProfilo().getBytes().length!=0) { //Salva il logo del responsabile
                    String logodir="user-logos/";
                    Path uploadPath = Paths.get(logodir);
                    if(!uploadPath.toFile().exists()) uploadPath.toFile().mkdirs();
                    filename=registerUtenteForm.getEmail()+registerUtenteForm.getImmagineProfilo().getOriginalFilename();
                    Path filePath=uploadPath.resolve(filename);
                    if(Files.exists(filePath)) Files.delete(filePath);
                    Files.write(filePath,registerUtenteForm.getImmagineProfilo().getBytes());
                    filename=logodir+filename;
                }
                //Salvo il responsabile
                UtenteEntity utenteEntity = new UtenteEntity(aziendaEntity,registerUtenteForm.getEmail(), PasswordUtility.hashPassword(registerUtenteForm.getPassword()),registerUtenteForm.getNome(),registerUtenteForm.getCognome(),registerUtenteForm.getTelefonoutente(),filename);
                userDetailsService.registraUtente(utenteEntity,aziendaEntity.getId_azienda());

                //Creo i ruoli default e mi prendo il ruolo del responsabile
                RuoloEntity ruoloEntity= ruoloService.attivazioneDefault(aziendaEntity);

                //Creo l'associazione tra Utente e Ruolo Responsabile
                AssociazioneEntity associazioneResponsabile = new AssociazioneEntity(utenteEntity,ruoloEntity);
                associazioneService.save(associazioneResponsabile);

                //Attivo tutti i moduli di default
                attivazioneService.attivazioneDefault(aziendaEntity);

                //Affilio tutti i moduli di default per il Responsabile
                pertinenzaService.attivazioneDefault(aziendaEntity);

                //La sessione viene svuotata visto che l'azienda è stata registrata correttamente e anche il responsabile
                sessionStatus.setComplete();
                return "redirect:/login?success&message=Registrazione effettuata con successo!! Ora effettua il Login";
            }
        }
    }
}