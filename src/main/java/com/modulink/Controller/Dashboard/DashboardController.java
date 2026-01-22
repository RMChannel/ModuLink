package com.modulink.Controller.Dashboard;

import com.modulink.Model.Eventi.EventoRepository;
import com.modulink.Model.Prodotto.ProdottoRepository;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneRepository;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneRepository;
import com.modulink.Model.Ruolo.RuoloRepository;
import com.modulink.Model.News.NewsRepository;
import com.modulink.Model.SupportForm.SupportFormRepository;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UserRepository;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class DashboardController {
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final RuoloRepository ruoloRepository;
    private final ProdottoRepository prodottoRepository;
    private final EventoRepository eventoRepository;
    private final AssegnazioneRepository assegnazioneRepository;
    private final AttivazioneRepository attivazioneRepository;
    private final NewsRepository newsRepository;
    private final SupportFormRepository supportFormRepository;


    public DashboardController(CustomUserDetailsService customUserDetailsService,
                               UserRepository userRepository,
                               RuoloRepository ruoloRepository,
                               ProdottoRepository prodottoRepository,
                               EventoRepository eventoRepository,
                               AssegnazioneRepository assegnazioneRepository,
                               AttivazioneRepository attivazioneRepository,
                               NewsRepository newsRepository,
                               SupportFormRepository supportFormRepository) {
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
        this.ruoloRepository = ruoloRepository;
        this.prodottoRepository = prodottoRepository;
        this.eventoRepository = eventoRepository;
        this.assegnazioneRepository = assegnazioneRepository;
        this.attivazioneRepository = attivazioneRepository;
        this.newsRepository = newsRepository;
        this.supportFormRepository = supportFormRepository;
    }

    @GetMapping({"/dashboard", "/dashboard/"})
    public String dashboardDispatcher(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }
        String email = principal.getName();
        Optional<UtenteEntity> utenteOpt = customUserDetailsService.findByEmail(email);
        if (utenteOpt.isPresent()) {
            UtenteEntity utente = utenteOpt.get();
            if (customUserDetailsService.isThisaNewUtente(utente)) {
                return "user/firstlogin";
            } else {
                // Preparazione statistiche per i moduli
                Map<Integer, Object> stats = new HashMap<>();

                // GDU (0) - Gestione Utenti: numero totale utenti azienda
                stats.put(0, userRepository.getAllByAziendaIs(utente.getAzienda()).size());

                // GRU (1) - Gestione Ruoli: numero totale ruoli azienda
                stats.put(1, ruoloRepository.findAllByAzienda(utente.getAzienda()).size());

                // GMA (2) - Gestione Moduli: numero moduli attivi
                stats.put(2, attivazioneRepository.findModuliByAzienda(utente.getAzienda()).size());

                // Calendario (4): eventi di oggi
                long eventiOggi = eventoRepository.findAllByUtente(utente).stream()
                        .filter(e -> e.getData_fine().toLocalDate().equals(LocalDate.now()))
                        .count();
                stats.put(4, eventiOggi);

                // GTM (5) - Gestione Task: task assegnati non completati
                long taskPendenti = assegnazioneRepository.findByUtente(utente).stream()
                        .filter(a -> a.getTask().getDataCompletamento() == null)
                        .count();
                stats.put(5, taskPendenti);

                // GDM (6) - Gestione Magazzino: numero totale prodotti
                stats.put(6, prodottoRepository.findAllByAzienda(utente.getAzienda()).size());

                // Store (9999): moduli disponibili non acquistati
                stats.put(9999, attivazioneRepository.getAllNotPurchased(utente.getAzienda()).size());

                // News (-3): news recenti (ultimi 7 giorni)
                long newsRecenti = newsRepository.findAll().stream()
                        .filter(n -> n.getData().isAfter(LocalDate.now().minusDays(7)))
                        .count();
                stats.put(-3, newsRecenti);

                // Support (-2): messaggi inviati dall'utente
                long messaggiSupporto = supportFormRepository.findAll().stream()
                        .filter(s -> s.getEmail().equals(utente.getEmail()))
                        .count();
                stats.put(-2, messaggiSupporto);

                model.addAttribute("stats", stats);
                return "user/dashboard";
            }
        } else {
            return "redirect:/logout";
        }
    }
}
