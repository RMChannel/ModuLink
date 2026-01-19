package com.modulink.Controller.UserModules.GDM;

import com.modulink.Alert;
import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Prodotto.ProdottoEntity;
import com.modulink.Model.Prodotto.ProdottoID;
import com.modulink.Model.Prodotto.ProdottoNotFoundException;
import com.modulink.Model.Prodotto.ProdottoService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller per il modulo <strong>GDM (Gestione Magazzino)</strong>.
 * <p>
 * Questo componente gestisce tutte le operazioni relative all'inventario prodotti all'interno del contesto multi-tenant.
 * Fornisce funzionalità per il caricamento iniziale dei prodotti, l'aggiornamento delle giacenze (acquisti/vendite)
 * e la categorizzazione degli articoli.
 * </p>
 * <p>
 * Estende {@link ModuloController} (ID Modulo: 6) per garantire che solo le aziende con il modulo attivo
 * e gli utenti con i permessi necessari possano accedere alle risorse.
 * </p>
 *
 * @author Modulink Team
 * @version 2.4.0
 * @since 1.3.0
 */
@Controller
public class GDMController extends ModuloController {
    private final ProdottoService prodottoService;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param prodottoService          Servizio per la manipolazione dell'entità Prodotto.
     * @param customUserDetailsService Servizio per il recupero dell'utente loggato.
     * @param moduloService            Servizio base per la gestione dei moduli.
     * @since 1.3.0
     */
    public GDMController(ProdottoService prodottoService, CustomUserDetailsService customUserDetailsService, ModuloService moduloService) {
        super(moduloService, 6);
        this.prodottoService = prodottoService;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Visualizza la pagina principale di gestione prodotti.
     * <p>
     * Carica l'elenco completo dei prodotti appartenenti esclusivamente all'azienda dell'utente corrente.
     * </p>
     *
     * @param principal Identità dell'utente.
     * @param model     Modello UI per il passaggio dei prodotti alla vista.
     * @return Nome della vista "moduli/gdm/GestioneProdotti" o redirect.
     * @since 1.3.0
     */
    @GetMapping({"/dashboard/gdm","/dashboard/gdm/"})
    public String dashboardDispatcher(Principal principal, Model model) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            UtenteEntity utente=utenteOpt.get();
            List<ProdottoEntity> prodotti=prodottoService.findAllByAzienda(utente.getAzienda());
            model.addAttribute("prodotti",prodotti);
            return "moduli/gdm/GestioneProdotti";
        }
        else return "redirect:/";
    }

    /**
     * Recupera l'elenco univoco delle categorie prodotti utilizzate dall'azienda.
     *
     * @param principal Identità dell'utente.
     * @return Lista di stringhe rappresentanti le categorie o redirect.
     * @since 1.3.5
     */
    @PostMapping({"/dashboard/gdm/get-categories","/dashboard/gdm/get-categories/"})
    public Object getCategories(Principal principal) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) return prodottoService.findAllCategoriesByAzienda(utenteOpt.get().getAzienda());
        else return "redirect:/";
    }

    /**
     * Aggiunge un nuovo prodotto al magazzino aziendale.
     * <p>
     * Esegue validazioni di integrità sui campi numerici (quantità e prezzo devono essere positivi)
     * prima di persistere l'entità.
     * </p>
     *
     * @param principal       Identità dell'utente.
     * @param model           Modello UI.
     * @param newProdottoForm DTO con i dati del nuovo prodotto.
     * @param bindingResult   Risultati della validazione.
     * @return Redirect alla dashboard GDM con feedback di successo o errore.
     * @since 1.3.0
     */
    @PostMapping({"/dashboard/gdm/add-product","/dashboard/gdm/add-product/"})
    public String addProduct(Principal principal, Model model, @Valid @ModelAttribute("newProdottoForm") NewProdottoForm newProdottoForm, BindingResult bindingResult) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            if(bindingResult.hasErrors()) {
                model.addAttribute("newProdottoForm",newProdottoForm);
                return "moduli/gdm/GestioneProdotti";
            }
            if(newProdottoForm.getQuantita()!=null && newProdottoForm.getQuantita()<0) bindingResult.rejectValue("quantita","quantità.negative","La quantità deve essere positiva");
            else if(newProdottoForm.getPrezzo()!=null && newProdottoForm.getPrezzo()<0) bindingResult.rejectValue("prezzo","prezzo.negative","Il prezzo deve essere positivo");
            if(bindingResult.hasErrors()) {
                model.addAttribute("newProdottoForm",newProdottoForm);
                return "moduli/gdm/GestioneProdotti";
            }
            UtenteEntity utente=utenteOpt.get();
            ProdottoEntity p=new ProdottoEntity(utente.getAzienda(),newProdottoForm.getNome(),newProdottoForm.getQuantita(),newProdottoForm.getPrezzo(),newProdottoForm.getDescrizione(),newProdottoForm.getCategoria());
            prodottoService.save(p);
            return "redirect:/dashboard/gdm"+ Alert.success("Prodotto aggiunto con successo");
        }
        else return "redirect:/";
    }

    /**
     * Rimuove un prodotto dal magazzino.
     *
     * @param principal   Identità dell'utente.
     * @param model       Modello UI.
     * @param id_prodotto Identificativo del prodotto da eliminare.
     * @return Redirect alla dashboard GDM.
     * @since 1.3.0
     */
    @PostMapping({"/dashboard/gdm/remove-product","/dashboard/gdm/remove-product/"})
    public String removeProduct(Principal principal, Model model, @RequestParam("id_prodotto") int id_prodotto) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            try {
                prodottoService.delete(id_prodotto,utenteOpt.get().getAzienda());
            } catch (ProdottoNotFoundException e) {
                return "redirect:/dashboard/gdm"+Alert.error("Prodotto non trovato");
            }
            return "redirect:/dashboard/gdm"+Alert.success("Prodotto cancellato con successo");
        }
        else return "redirect:/";
    }

    /**
     * Aggiorna i dati anagrafici e tecnici di un prodotto esistente.
     *
     * @param principal          Identità dell'utente.
     * @param model              Modello UI.
     * @param updateProdottoForm DTO con i dati aggiornati del prodotto.
     * @param bindingResult      Risultati della validazione.
     * @return Redirect alla dashboard GDM.
     * @since 1.3.0
     */
    @PostMapping({"/dashboard/gdm/update-product","/dashboard/gdm/update-product/"})
    public String updateProduct(Principal principal, Model model, @Valid @ModelAttribute("updateProdottoForm") UpdateProdottoForm updateProdottoForm, BindingResult bindingResult) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            if(bindingResult.hasErrors()) {
                model.addAttribute("updateProdottoForm",updateProdottoForm);
                return "moduli/gdm/GestioneProdotti";
            }
            if(updateProdottoForm.getQuantita() == null || updateProdottoForm.getQuantita() < 0) bindingResult.rejectValue("quantita","quantità.negative","La quantità deve essere positiva");
            if(updateProdottoForm.getPrezzo()==null || updateProdottoForm.getPrezzo()<0) bindingResult.rejectValue("prezzo","prezzo.negative","Il prezzo deve essere positivo");
            if(bindingResult.hasErrors()) {
                model.addAttribute("updateProdottoForm",updateProdottoForm);
                return "moduli/gdm/GestioneProdotti";
            }
            try {
                ProdottoEntity prodotto=prodottoService.findById(new ProdottoID(updateProdottoForm.getIdProdotto(),utenteOpt.get().getAzienda().getId_azienda()));
                prodotto.setNome(updateProdottoForm.getNome());
                prodotto.setQuantita((int) updateProdottoForm.getQuantita());
                prodotto.setPrezzo((double) updateProdottoForm.getPrezzo());
                prodotto.setDescrizione(updateProdottoForm.getDescrizione());
                prodotto.setCategoria(updateProdottoForm.getCategoria());
                prodottoService.updateProdotto(prodotto);
                return "redirect:/dashboard/gdm"+Alert.success("Prodotto modificato con successo");
            } catch (ProdottoNotFoundException e) {
                return "redirect:/dashboard/gdm"+Alert.error("Prodotto non trovato");
            }
        }
        else return "redirect:/";
    }

    /**
     * Incrementa la giacenza di un prodotto (operazione di carico/acquisto).
     *
     * @param principal  Identità dell'utente.
     * @param idProdotto Identificativo del prodotto.
     * @param quantita   Numero di unità da aggiungere.
     * @return Redirect alla dashboard GDM con riepilogo valore acquisto.
     * @since 1.4.0
     */
    @PostMapping({"/dashboard/gdm/buy","/dashboard/gdm/buy/"})
    public String acquista(Principal principal, @RequestParam int idProdotto, @RequestParam int quantita) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            if(quantita<1) return "redirect:/dashboard/gdm"+Alert.error("La quantita di acquisto non può essere minore di 1");
            try {
                ProdottoEntity prodotto=prodottoService.findById(new ProdottoID(idProdotto,utenteOpt.get().getAzienda().getId_azienda()));
                prodotto.setQuantita(prodotto.getQuantita()+quantita);
                prodottoService.updateProdotto(prodotto);
                return "redirect:/dashboard/gdm"+Alert.success("Acquisto effettuato con successo, acquistato: "+quantita*prodotto.getPrezzo()+"€");
            } catch (ProdottoNotFoundException e) {
                return "redirect:/dashboard/gdm"+Alert.error("Prodotto non trovato");
            }
        }
        else return "redirect:/";
    }

    /**
     * Decrementa la giacenza di un prodotto (operazione di scarico/vendita).
     * <p>
     * Verifica preventivamente che la quantità disponibile sia sufficiente per coprire la vendita.
     * </p>
     *
     * @param principal  Identità dell'utente.
     * @param idProdotto Identificativo del prodotto.
     * @param quantita   Numero di unità da rimuovere.
     * @return Redirect alla dashboard GDM con riepilogo valore vendita.
     * @since 1.4.0
     */
    @PostMapping({"/dashboard/gdm/sell","/dashboard/gdm/sell/"})
    public String vendita(Principal principal, @RequestParam int idProdotto, @RequestParam int quantita) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            if(quantita<1) return "redirect:/dashboard/gdm"+Alert.error("La quantita di acquisto non può essere minore di 1");
            try {
                ProdottoEntity prodotto=prodottoService.findById(new ProdottoID(idProdotto,utenteOpt.get().getAzienda().getId_azienda()));
                if(prodotto.getQuantita()<quantita) return "redirect:/dashboard/gdm"+Alert.error("Non hai abbastanza prodotti per effettuare questa vendita");
                prodotto.setQuantita(prodotto.getQuantita()-quantita);
                prodottoService.updateProdotto(prodotto);
                return "redirect:/dashboard/gdm"+Alert.success("Vendita effettuata con successa, venduto: "+quantita*prodotto.getPrezzo()+"€");
            } catch (ProdottoNotFoundException e) {
                return "redirect:/dashboard/gdm"+Alert.error("Prodotto non trovato");
            }
        }
        else return "redirect:/";
    }

    /**
     * Gestisce la pulizia dei dati in caso di disinstallazione del modulo GDM.
     * Rimuove tutti i record della tabella Prodotti associati all'azienda specificata.
     *
     * @param azienda L'azienda che disinstalla il modulo.
     * @since 1.3.0
     */
    @Override
    public void disinstallaModulo(AziendaEntity azienda) {
        prodottoService.deleteAllByAzienda(azienda);
    }
}