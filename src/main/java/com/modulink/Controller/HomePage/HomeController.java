package com.modulink.Controller.HomePage;


import com.modulink.Controller.AdminModules.Support.SupportForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * Controller per la gestione delle pagine pubbliche (Homepage) del portale ModuLink.
 * <p>
 * Questo controller gestisce l'accesso alle risorse non protette come la landing page,
 * le pagine informative (Privacy, Termini, Affiliati) e il form di supporto pubblico.
 * Implementa una logica di reindirizzamento per gli utenti già autenticati,
 * prevenendo l'accesso alle pagine di benvenuto se una sessione è già attiva.
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.0
 * @since 1.0.0
 */
@Controller
public class HomeController {

    /**
     * Gestisce la root del sito ("/", "/home").
     * <p>
     * Se l'utente è autenticato (Principal != null), viene reindirizzato alla dashboard personale.
     * Altrimenti, visualizza la landing page pubblica.
     * </p>
     *
     * @param model     Modello UI.
     * @param principal Utente autenticato (opzionale).
     * @return Vista "index" o redirect "/dashboard".
     * @since 1.0.0
     */
    @GetMapping({"/","/home","/home/"})
    public String homepage(Model model, Principal principal) {
        if(principal != null) {
            return "redirect:/dashboard";
        }
        else {
            return "index";
        }
    }

    /**
     * Visualizza la pagina "Contattaci".
     * <p>
     * Accessibile solo se non autenticati. Gli utenti loggati vengono reindirizzati alla home.
     * </p>
     *
     * @param model     Modello UI.
     * @param principal Utente autenticato.
     * @return Vista "homepage/contactus" o redirect.
     * @since 1.0.0
     */
    @GetMapping("/contactus")
    public String contactUs(Model model, Principal principal) {
        if(principal != null) {
            return "redirect:/home";
        }
        else {
            return "homepage/contactus";
        }
    }

    /**
     * Visualizza la pagina della Privacy Policy.
     *
     * @param model     Modello UI.
     * @param principal Utente autenticato.
     * @return Vista "homepage/privacy" o redirect.
     * @since 1.0.0
     */
    @GetMapping("/privacy")
    public String privacy(Model model, Principal principal) {
        if(principal != null) {
            return "redirect:/home";
        }
        else {
            return "homepage/privacy";
        }
    }

    /**
     * Visualizza la pagina dei Termini e Condizioni.
     *
     * @param model     Modello UI.
     * @param principal Utente autenticato.
     * @return Vista "homepage/termini" o redirect.
     * @since 1.0.0
     */
    @GetMapping("/termini")
    public String termini(Model model, Principal principal) {
        if(principal != null) {
            return "redirect:/home";
        }
        else {
            return "homepage/termini";
        }
    }

    /**
     * Visualizza la pagina di Supporto pubblico.
     * <p>
     * Inizializza un nuovo {@link SupportForm} vuoto nel modello per il binding del form di contatto.
     * </p>
     *
     * @param model     Modello UI.
     * @param principal Utente autenticato.
     * @return Vista "homepage/supporto".
     * @since 1.1.0
     */
    @GetMapping("/supporto")
    public String supporto(Model model, Principal principal) {
        model.addAttribute("support", new SupportForm());
        return "homepage/supporto";
    }

    /**
     * Visualizza la pagina descrittiva dei pacchetti commerciali.
     *
     * @param model     Modello UI.
     * @param principal Utente autenticato.
     * @return Vista "homepage/pacchetti".
     * @since 1.0.0
     */
    @GetMapping("/pacchetti")
    public String pacchetti(Model model, Principal principal) {
        return "homepage/pacchetti";
    }

    /**
     * Visualizza il manuale utente online.
     *
     * @param model     Modello UI.
     * @param principal Utente autenticato.
     * @return Vista "homepage/manuale".
     * @since 1.0.0
     */
    @GetMapping("/manuale")
    public String manuale(Model model, Principal principal) {
        return "homepage/manuale";
    }

}