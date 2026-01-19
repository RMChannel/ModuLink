package com.modulink.Controller.AdminModules.News;

import com.modulink.Alert;
import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.News.NewsEntity;
import com.modulink.Model.News.NewsService;
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
import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller per la gestione del modulo di pubblicazione News.
 * <p>
 * Questo controller permette agli amministratori di gestire le comunicazioni verso gli utenti
 * (creazione e cancellazione news) e agli utenti finali di visualizzarle.
 * Estende {@link ModuloController} per l'integrazione con il sistema di gestione moduli.
 * </p>
 *
 * @author Modulink Team
 * @version 1.5.4
 * @since 1.1.0
 */
@Controller
public class NewsController extends ModuloController {
    private final NewsService newsService;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Costruttore con iniezione delle dipendenze.
     *
     * @param moduloService            Servizio base gestione moduli.
     * @param newsService              Servizio specifico per entità News.
     * @param customUserDetailsService Servizio recupero utenti.
     * @since 1.1.0
     */
    public NewsController(ModuloService moduloService, NewsService newsService, CustomUserDetailsService customUserDetailsService) {
        super(moduloService, -3);
        this.newsService = newsService;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Gestisce la visualizzazione pubblica delle news nella homepage.
     *
     * @param model     Modello UI.
     * @param principal Utente loggato.
     * @return Vista pubblica delle news.
     * @since 1.1.0
     */
    @GetMapping("/news")
    public String news(Model model, Principal principal) {
        model.addAttribute("news",newsService.findAll());
        return "homepage/news";
    }

    /**
     * Gestisce l'accesso al pannello di amministrazione delle news.
     * Verifica i permessi di accesso al modulo prima di mostrare la pagina di gestione.
     *
     * @param model     Modello UI.
     * @param principal Utente amministratore.
     * @return Vista di amministrazione news o redirect.
     * @since 1.1.0
     */
    @GetMapping({"/dashboard/news","/dashboard/news/"})
    public String getNews(Model model, Principal principal) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            model.addAttribute("news",newsService.findAll());
            model.addAttribute("newsForm", new NewsForm());
            return "admin/news/newsPage";
        }
        else return "redirect:/";
    }

    /**
     * Processa la creazione di una nuova news.
     * <p>
     * Esegue la validazione dei dati (inclusa la verifica che la data non sia nel passato)
     * e salva la news nel database.
     * </p>
     *
     * @param model         Modello UI.
     * @param principal     Utente amministratore.
     * @param newsForm      DTO con i dati della news.
     * @param bindingResult Risultati della validazione.
     * @return Redirect con messaggio di successo o ricaricamento pagina in caso di errore.
     * @since 1.1.0
     */
    @PostMapping({"/dashboard/news","/dashboard/news/"})
    public String addNews(Model model, Principal principal, @Valid @ModelAttribute("newsForm") NewsForm newsForm, BindingResult bindingResult) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            if(newsForm.getData().isBefore(LocalDate.now().minusDays(1))) bindingResult.rejectValue("data","datanelpassato.error","La data inserita è nel passato");
            if(bindingResult.hasErrors()) {
                model.addAttribute("news", newsService.findAll());
                return "admin/news/newsPage";
            }
            NewsEntity news=new NewsEntity(newsForm.getTitolo(),newsForm.getTesto(),newsForm.getData());
            newsService.save(news);
            return "redirect:/dashboard/news"+ Alert.success("News aggiunta con successo");
        }
        else return "redirect:/";
    }

    /**
     * Gestisce la cancellazione di una news specifica.
     *
     * @param principal Utente amministratore.
     * @param model     Modello UI.
     * @param idNews    ID della news da eliminare.
     * @return Redirect con messaggio di successo.
     * @since 1.1.0
     */
    @PostMapping({"/dashboard/remove-news","/dashboard/remove-news/"})
    public String removeNews(Principal principal, Model model, @RequestParam int idNews) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            newsService.delete(idNews);
            return "redirect:/dashboard/news"+ Alert.success("News cancellata con successo");
        }
        else return "redirect:/";
    }

    /**
     * Inibisce la disinstallazione del modulo News essendo un componente di sistema.
     *
     * @param azienda Azienda target.
     * @since 1.1.0
     */
    //Modulo non disinstallabile
    @Override
    public void disinstallaModulo(AziendaEntity azienda) {
        System.err.println("Modulo non disinstallabile");
        return;
    }
}
