package com.modulink.DatabasePopulator;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaService;
import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Eventi.EventoRepository;
import com.modulink.Model.Eventi.EventoService;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import com.modulink.Model.Prodotto.ProdottoEntity;
import com.modulink.Model.Prodotto.ProdottoService;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneService;
import com.modulink.Model.Relazioni.Pertinenza.PertinenzaEntity;
import com.modulink.Model.Relazioni.Pertinenza.PertinenzaRepository;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneRepository;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneRepository;
import com.modulink.Model.Relazioni.Partecipazione.PartecipazioneEntity;
import com.modulink.Model.Relazioni.Partecipazione.PartecipazioneRepository;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloRepository;
import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Task.TaskService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DataInitializerService {

    private final AziendaService aziendaService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RuoloRepository ruoloRepository;
    private final AssociazioneRepository associazioneRepository;
    private final ModuloRepository moduloRepository;
    private final AttivazioneRepository attivazioneRepository;
    private final PertinenzaRepository pertinenzaRepository;
    private final EventoService eventoService;
    private final PartecipazioneRepository partecipazioneRepository;
    private final AttivazioneService attivazioneService;
    private final TaskService taskService;
    private final ProdottoService prodottoService;

    public DataInitializerService(AziendaService aziendaService, CustomUserDetailsService customUserDetailsService, RuoloRepository ruoloRepository, AssociazioneRepository associazioneRepository, ModuloRepository moduloRepository, AttivazioneRepository attivazioneRepository, PertinenzaRepository pertinenzaRepository, EventoService eventoService, PartecipazioneRepository partecipazioneRepository, AttivazioneService attivazioneService, TaskService taskService, ProdottoService prodottoService) {
        this.aziendaService = aziendaService;
        this.customUserDetailsService = customUserDetailsService;
        this.ruoloRepository = ruoloRepository;
        this.associazioneRepository = associazioneRepository;
        this.moduloRepository = moduloRepository;
        this.eventoService = eventoService;
        this.attivazioneRepository = attivazioneRepository;
        this.pertinenzaRepository = pertinenzaRepository;
        this.partecipazioneRepository = partecipazioneRepository;
        this.attivazioneService = attivazioneService;
        this.taskService = taskService;
        this.prodottoService = prodottoService;
    }

    public void addDemoAzienda() {
        // Recupera i moduli necessari
        ModuloEntity modCalendario = moduloRepository.findById(4).orElse(null);
        ModuloEntity modGTM = moduloRepository.findById(5).orElse(null);
        ModuloEntity modGDM = moduloRepository.findById(6).orElse(null);

        // --- Azienda 1: Tech Solutions ---
        AziendaEntity techSolutions = new AziendaEntity("Tech Solutions", "01234567891", "Via Innovazione 10", "Milano", "20100", "+39021234567", "");
        techSolutions = aziendaService.registraAzienda(techSolutions);

        // Utenti
        UtenteEntity tsAdmin = new UtenteEntity(techSolutions, "admin@techsolutions.com", PasswordUtility.hashPassword("password"), "Marco", "Rossi", "+393330000001", "");
        UtenteEntity tsUser = new UtenteEntity(techSolutions, "user@techsolutions.com", PasswordUtility.hashPassword("password"), "Luca", "Bianchi", "+393330000002", "");
        customUserDetailsService.registraUtente(tsAdmin, techSolutions.getId_azienda());
        customUserDetailsService.registraUtente(tsUser, techSolutions.getId_azienda());

        // Ruoli
        RuoloEntity ruoloAdminTS = new RuoloEntity(0, techSolutions, "Amministratore", "#FF0000", "Amministratore di sistema");
        RuoloEntity ruoloDipendenteTS = new RuoloEntity(1, techSolutions, "Dipendente", "#00FF00", "Dipendente standard");
        ruoloRepository.save(ruoloAdminTS);
        ruoloRepository.save(ruoloDipendenteTS);

        // Associazione Ruoli
        associazioneRepository.save(new AssociazioneEntity(tsAdmin, ruoloAdminTS));
        associazioneRepository.save(new AssociazioneEntity(tsUser, ruoloDipendenteTS));

        // Attivazione Moduli
        // Attiviamo moduli base + Task + Magazzino + Calendario
        attivazioneRepository.save(new AttivazioneEntity(modGTM, techSolutions));
        attivazioneRepository.save(new AttivazioneEntity(modGDM, techSolutions));
        attivazioneRepository.save(new AttivazioneEntity(modCalendario, techSolutions));
        // Attiviamo default (GDU, GRU, etc se necessario, attivazioneService.attivazioneDefault lo fa per alcuni)
        attivazioneService.attivazioneDefault(techSolutions);

        // Pertinenze (Permessi)
        // Admin ha accesso a tutto
        pertinenzaRepository.save(new PertinenzaEntity(ruoloAdminTS.getId_ruolo(), modGTM.getId_modulo(), techSolutions.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloAdminTS.getId_ruolo(), modGDM.getId_modulo(), techSolutions.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloAdminTS.getId_ruolo(), modCalendario.getId_modulo(), techSolutions.getId_azienda()));
        // Utente ha accesso a Task e Calendario
        pertinenzaRepository.save(new PertinenzaEntity(ruoloDipendenteTS.getId_ruolo(), modGTM.getId_modulo(), techSolutions.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloDipendenteTS.getId_ruolo(), modCalendario.getId_modulo(), techSolutions.getId_azienda()));

        // Prodotti
        ProdottoEntity p1 = new ProdottoEntity(techSolutions, "Laptop Pro", 10, 1200.00, "Laptop ad alte prestazioni", "Elettronica");
        ProdottoEntity p2 = new ProdottoEntity(techSolutions, "Mouse Wireless", 50, 25.50, "Mouse ergonomico", "Accessori");
        prodottoService.save(p1);
        prodottoService.save(p2);

        // Task
        TaskEntity t1 = new TaskEntity(techSolutions, tsAdmin, "Aggiornare Server", 3, LocalDate.now().plusDays(5), LocalDate.now(), null);
        TaskEntity t2 = new TaskEntity(techSolutions, tsAdmin, "Scrivere Documentazione", 2, LocalDate.now().plusDays(10), LocalDate.now(), null);
        taskService.save(t1);
        taskService.save(t2);

        // Eventi
        EventoEntity e1 = new EventoEntity(techSolutions, "Riunione Settimanale", "Sala Riunioni A", LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), LocalDateTime.now().plusDays(1).withHour(11).withMinute(0));
        e1.setCreatore(tsAdmin);
        eventoService.create(e1);

        EventoEntity e2 = new EventoEntity(techSolutions, "Webinar Cloud", "Online", LocalDateTime.now().plusDays(3).withHour(15).withMinute(0), LocalDateTime.now().plusDays(3).withHour(16).withMinute(30));
        e2.setCreatore(tsAdmin);
        eventoService.create(e2);


        // --- Azienda 2: Green Energy ---
        AziendaEntity greenEnergy = new AziendaEntity("Green Energy", "98765432109", "Viale della Natura 5", "Roma", "00100", "+39061234567", "");
        greenEnergy = aziendaService.registraAzienda(greenEnergy);

        // Utenti
        UtenteEntity geManager = new UtenteEntity(greenEnergy, "manager@greenenergy.com", PasswordUtility.hashPassword("password"), "Giulia", "Verdi", "+393330000003", "");
        UtenteEntity geTech = new UtenteEntity(greenEnergy, "tech@greenenergy.com", PasswordUtility.hashPassword("password"), "Paolo", "Neri", "+393330000004", "");
        customUserDetailsService.registraUtente(geManager, greenEnergy.getId_azienda());
        customUserDetailsService.registraUtente(geTech, greenEnergy.getId_azienda());

        // Ruoli
        RuoloEntity ruoloManagerGE = new RuoloEntity(0, greenEnergy, "Manager", "#0000FF", "Gestore aziendale");
        RuoloEntity ruoloTecnicoGE = new RuoloEntity(1, greenEnergy, "Tecnico", "#FFFF00", "Tecnico operativo");
        ruoloRepository.save(ruoloManagerGE);
        ruoloRepository.save(ruoloTecnicoGE);

        // Associazione Ruoli
        associazioneRepository.save(new AssociazioneEntity(geManager, ruoloManagerGE));
        associazioneRepository.save(new AssociazioneEntity(geTech, ruoloTecnicoGE));

        // Attivazione Moduli (Solo Task e Magazzino)
        attivazioneRepository.save(new AttivazioneEntity(modGTM, greenEnergy));
        attivazioneRepository.save(new AttivazioneEntity(modGDM, greenEnergy));
        attivazioneService.attivazioneDefault(greenEnergy);

        // Pertinenze
        pertinenzaRepository.save(new PertinenzaEntity(ruoloManagerGE.getId_ruolo(), modGTM.getId_modulo(), greenEnergy.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloManagerGE.getId_ruolo(), modGDM.getId_modulo(), greenEnergy.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloTecnicoGE.getId_ruolo(), modGDM.getId_modulo(), greenEnergy.getId_azienda())); // Tecnico vede solo magazzino

        // Prodotti
        ProdottoEntity p3 = new ProdottoEntity(greenEnergy, "Pannello Solare X1", 20, 500.00, "Pannello fotovoltaico 300W", "Energia");
        ProdottoEntity p4 = new ProdottoEntity(greenEnergy, "Inverter Hybrid", 5, 1500.00, "Inverter ibrido 5kW", "Elettronica");
        prodottoService.save(p3);
        prodottoService.save(p4);

        // Task
        TaskEntity t3 = new TaskEntity(greenEnergy, geManager, "Installazione Impianto Rossi", 3, LocalDate.now().plusDays(2), LocalDate.now(), null);
        taskService.save(t3);

        // Eventi (Nessun modulo calendario attivo, quindi niente eventi o magari eventi di sistema se possibile, ma evitiamo se modulo non c'è)
        // Se il modulo non è attivo, l'accesso sarebbe bloccato da UI, ma a livello DB si potrebbero creare.
        // Per coerenza non ne creo se non ho attivato il modulo.
    }

    public void addModulinkAzienda() {


        AziendaEntity modulink = new AziendaEntity("ModuLink","1111111111","Via Nazionale","Santa Maria a Vico","81028","+393471304385","azienda-logos/11111111111_1769189367649_logo.png");
        UtenteEntity u1 = new UtenteEntity(modulink,"r.cito@studenti.unisa.it",PasswordUtility.hashPassword("ciaociao"),"Roberto","Cito","+393471304385","");
        UtenteEntity u2 = new UtenteEntity(modulink,"a.buzi@studenti.unisa.it",PasswordUtility.hashPassword("ciaociao"),"Arjel","Buzi","+393755676630","");
        UtenteEntity u3 = new UtenteEntity(modulink,"d.carpentieri8@studenti.unisa.it",PasswordUtility.hashPassword("ciaociao"),"Daniele","Carpentieri","+393290228070","");
        UtenteEntity u4 = new UtenteEntity(modulink,"a.chikviladze@studenti.unisa.it",PasswordUtility.hashPassword("ciaociao"),"Aleksandre","Chikviladze","+393285630871","");

        modulink = aziendaService.registraAzienda(modulink);

        customUserDetailsService.registraUtente(u1, modulink.getId_azienda());
        customUserDetailsService.registraUtente(u2, modulink.getId_azienda());
        customUserDetailsService.registraUtente(u3, modulink.getId_azienda());
        customUserDetailsService.registraUtente(u4, modulink.getId_azienda());


        RuoloEntity ruoloResponsabile2 = new RuoloEntity(0, modulink, "Responsabile", "#000000", "Responsabile dell'azienda");
        RuoloEntity ruoloNewUser2 = new RuoloEntity(1,modulink,"Utente Nuovo","blue","Utente non ancora ufficialmente registrato");
        RuoloEntity ruoloStandard2 = new RuoloEntity(2,modulink,"Utente","grey","Utente Standard");

        ruoloRepository.save(ruoloResponsabile2);
        ruoloRepository.save(ruoloNewUser2);
        ruoloRepository.save(ruoloStandard2);

        associazioneRepository.save(new AssociazioneEntity(u1, ruoloResponsabile2));
        associazioneRepository.save(new AssociazioneEntity(u2, ruoloResponsabile2));
        associazioneRepository.save(new AssociazioneEntity(u3, ruoloResponsabile2));
        associazioneRepository.save(new AssociazioneEntity(u4, ruoloResponsabile2));

        ModuloEntity News = new ModuloEntity(-3,"News","Modulo dedicato alla gestione delle news","/dashboard/news/","bi bi-newspaper",false);
        ModuloEntity Support = new ModuloEntity(-2,"Supporto", "Modulo dedicato al supporto tecnico", "/dashboard/support/", "bi bi-life-preserver",false);
        ModuloEntity Admin = new ModuloEntity(-1,"Panello Admin", "Panello di controllo di Admin","/dashboard/admin/","bi bi-code-square", false);
        ModuloEntity GDU = new ModuloEntity(0, "Gestione Utenti", "Permette la gestione di tutti gli utenti della propria azienda", "/dashboard/gdu/", "bi bi-person-lines-fill",true);
        ModuloEntity GRU = new ModuloEntity(1, "Gestione Ruoli", "Permette la gestione e l'assegnazione dei ruoli", "/dashboard/gru/", "bi bi-award-fill",true);
        ModuloEntity GMA = new ModuloEntity(2, "Gestione Moduli", "Permette la gestione di tutti i moduli integrati nella propria azienda","/dashboard/gma/","bi bi-database-gear",true );
        ModuloEntity GDR = new ModuloEntity(3, "Gestione Responsabile", "permette al responsabile di gestire la propria azienda","/dashboard/gdr/","bi bi-building-gear",true);
        ModuloEntity store = new ModuloEntity(9999,"Store","Store dei moduli","/dashboard/store/","bi bi-cart-dash",true);
        ModuloEntity calendario = new ModuloEntity(4,"Calendario", "Permette di organizzare e creare eventi", "/dashboard/calendar", "bi bi-calendar",true);
        ModuloEntity GTM = new ModuloEntity(5,"Gestione Task", "Permette di gestire le tasche degli utenti", "/dashboard/gtm/", "bi bi-clipboard-data",true);
        ModuloEntity GDM = new ModuloEntity(6,"Gestione Magazzino", "Permette di gestire tutti i prodotti nel magazzino", "/dashboard/gdm/", "bi bi-box-seam",true);

        Admin = moduloRepository.save(Admin);
        Support = moduloRepository.save(Support);
        News = moduloRepository.save(News);
        moduloRepository.save(GDR);
        moduloRepository.save(GDU);
        moduloRepository.save(GRU);
        moduloRepository.save(GMA);
        moduloRepository.save(store);
        moduloRepository.save(calendario);
        moduloRepository.save(GTM);
        moduloRepository.save(GDM);

        attivazioneRepository.save(new AttivazioneEntity(Admin,modulink));
        attivazioneRepository.save(new AttivazioneEntity(Support, modulink));
        attivazioneRepository.save(new AttivazioneEntity(News, modulink));
        attivazioneService.attivazioneDefault(modulink);

        pertinenzaRepository.save(new PertinenzaEntity(ruoloResponsabile2.getId_ruolo(),Support.getId_modulo(),modulink.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloResponsabile2.getId_ruolo(),Admin.getId_modulo(),modulink.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloResponsabile2.getId_ruolo(),News.getId_modulo(),modulink.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloResponsabile2.getId_ruolo(),9999,modulink.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloResponsabile2.getId_ruolo(),0,modulink.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloResponsabile2.getId_ruolo(),1,modulink.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloResponsabile2.getId_ruolo(),2,modulink.getId_azienda()));
        pertinenzaRepository.save(new PertinenzaEntity(ruoloResponsabile2.getId_ruolo(),3,modulink.getId_azienda()));

    }

    @Transactional // Qui la transazione funzionerà correttamente!
    public void runInitialization() {
        addModulinkAzienda();
        addDemoAzienda();
    }
}