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

@Controller
public class GDMController extends ModuloController {
    private final ProdottoService prodottoService;
    private final CustomUserDetailsService customUserDetailsService;

    public GDMController(ProdottoService prodottoService, CustomUserDetailsService customUserDetailsService, ModuloService moduloService) {
        super(moduloService, 6);
        this.prodottoService = prodottoService;
        this.customUserDetailsService = customUserDetailsService;
    }

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

    @PostMapping({"/dashboard/gdm/get-categories","/dashboard/gdm/get-categories/"})
    public Object getCategories(Principal principal) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) return prodottoService.findAllCategoriesByAzienda(utenteOpt.get().getAzienda());
        else return "redirect:/";
    }

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

    @Override
    public void disinstallaModulo(AziendaEntity azienda) {
        prodottoService.deleteAllByAzienda(azienda);
    }
}