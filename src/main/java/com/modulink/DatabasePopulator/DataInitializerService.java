package com.modulink.DatabasePopulator;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaService;
import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Eventi.EventoRepository;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneEntity;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneRepository;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneRepository;
import com.modulink.Model.Relazioni.Associazione.AssociazioneService;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneRepository;
import com.modulink.Model.Relazioni.Partecipazione.PartecipazioneEntity;
import com.modulink.Model.Relazioni.Partecipazione.PartecipazioneRepository;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloRepository;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DataInitializerService {

    private final AziendaService aziendaService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RuoloRepository ruoloRepository;
    private final AssociazioneRepository associazioneRepository;
    private final ModuloRepository moduloRepository;
    private final AttivazioneRepository attivazioneRepository;
    private final AffiliazioneRepository affiliazioneRepository;
    private final EventoRepository eventoRepository;
    private final PartecipazioneRepository partecipazioneRepository;

    public DataInitializerService(AziendaService aziendaService, CustomUserDetailsService customUserDetailsService, RuoloRepository ruoloRepository, AssociazioneRepository associazioneRepository, ModuloRepository moduloRepository, AttivazioneRepository attivazioneRepository, AffiliazioneRepository affiliazioneRepository, EventoRepository eventoRepository, PartecipazioneRepository partecipazioneRepository) {
        this.aziendaService = aziendaService;
        this.customUserDetailsService = customUserDetailsService;
        this.ruoloRepository = ruoloRepository;
        this.associazioneRepository = associazioneRepository;
        this.moduloRepository = moduloRepository;
        this.attivazioneRepository = attivazioneRepository;
        this.affiliazioneRepository = affiliazioneRepository;
        this.eventoRepository = eventoRepository;
        this.partecipazioneRepository = partecipazioneRepository;
    }

    public void addDemoAzienda() {
        AziendaEntity azienda = new AziendaEntity("Test","12345678901","Via Nazionale","Santa Maria a Vico","81028","+393205397560","");

        UtenteEntity utente = new UtenteEntity(azienda, "robbencito@gmail.com", PasswordUtility.hashPassword("ciaociao"), "Roberto", "Cito", "+393471304385", "");
        UtenteEntity u2 = new UtenteEntity(azienda, "rdiskonline@gmail.com", PasswordUtility.hashPassword("ciaociao"), "Test", "Test", "+393205397560", "");


        // Importante: riassegna sempre l'oggetto restituito dal save/registrazione
        azienda = aziendaService.registraAzienda(azienda);
        customUserDetailsService.registraUtente(utente, azienda.getId_azienda());
        customUserDetailsService.registraUtente(u2, azienda.getId_azienda());

        RuoloEntity ruoloResponsabile = new RuoloEntity(0, azienda, "Responsabile", "#000000", "Responsabile dell'azienda");
        RuoloEntity ruoloNewUser = new RuoloEntity(1,azienda,"Utente Nuovo","blue","Utente non ancora ufficialmente registrato");
        RuoloEntity ruoloStandard = new RuoloEntity(2,azienda,"Utente","grey","Utente Standard");
        ruoloRepository.save(ruoloResponsabile);
        ruoloRepository.save(ruoloNewUser);
        ruoloRepository.save(ruoloStandard);

        associazioneRepository.save(new AssociazioneEntity(utente, ruoloResponsabile));
        associazioneRepository.save(new AssociazioneEntity(u2, ruoloStandard));

        ModuloEntity Admin = new ModuloEntity(-1,"Panello Admin", "Panello di controllo di Admin","/dashboard/admin/","bi bi-code-square", false);
        ModuloEntity GDU = new ModuloEntity(0, "Gestione Utenti", "Permette la gestione di tutti gli utenti della propria azienda", "/dashboard/gdu/", "bi bi-person-lines-fill",true);
        ModuloEntity GDR = new ModuloEntity(1, "Gestione Ruoli", "Permette la gestione e l'assegnazione dei ruoli", "/dashboard/gdr/", "bi bi-award-fill",true);
        ModuloEntity GMA = new ModuloEntity(2, "Gestione Moduli", "Permette la gestione di tutti i moduli integrati nella propria azienda","/dashboard/gma/","bi bi-database-gear",true );
        ModuloEntity store = new ModuloEntity(3,"Store","Store dei moduli","/dashboard/store/","bi bi-cart-dash",true);
        ModuloEntity calendario = new ModuloEntity(4,"Calendario", "Permette di organizzare e creare eventi", "/dashboard/calendar", "bi bi-calendar",true);
        ModuloEntity GTM = new ModuloEntity(5,"Gestione Task", "Permette di gestire le tasche degli utenti", "/dashboard/gtm/", "bi bi-clipboard-data",true);
        ModuloEntity GDM = new ModuloEntity(6,"Gestione Magazzino", "Permette di gestire tutti i prodotti nel magazzino", "/dashboard/gdm/", "bi bi-box-seam",true);

        GDU = moduloRepository.save(GDU);
        GDR = moduloRepository.save(GDR);
        GMA = moduloRepository.save(GMA);
        store = moduloRepository.save(store);
        calendario = moduloRepository.save(calendario);
        GTM = moduloRepository.save(GTM);
        GDM = moduloRepository.save(GDM);
        Admin = moduloRepository.save(Admin);

        // Ora azienda e modulo sono MANAGED nella stessa transazione
        attivazioneRepository.save(new AttivazioneEntity(GDU, azienda));
        attivazioneRepository.save(new AttivazioneEntity(GDR, azienda));
        attivazioneRepository.save(new AttivazioneEntity(GMA, azienda));
        attivazioneRepository.save(new AttivazioneEntity(store, azienda));
        attivazioneRepository.save(new AttivazioneEntity(calendario, azienda));
        attivazioneRepository.save(new AttivazioneEntity(GTM, azienda));
        attivazioneRepository.save(new AttivazioneEntity(GDM, azienda));

        //testing admin
        attivazioneRepository.save(new AttivazioneEntity(Admin, azienda));


        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),GDU.getId_modulo(),azienda.getId_azienda()));
        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),GDR.getId_modulo(),azienda.getId_azienda()));
        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),GMA.getId_modulo(),azienda.getId_azienda()));
        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),store.getId_modulo(),azienda.getId_azienda()));
        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),calendario.getId_modulo(),azienda.getId_azienda()));
        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),GTM.getId_modulo(),azienda.getId_azienda()));
        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),GDM.getId_modulo(),azienda.getId_azienda()));
        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),Admin.getId_modulo(),azienda.getId_azienda()));
        // --- CREAZIONE EVENTI DI PROVA ---
        LocalDateTime now = LocalDateTime.now();

        EventoEntity evento1 = new EventoEntity(0, azienda, "Riunione Staff", "Sala Riunioni A", now.plusHours(2), now.plusHours(4));
        EventoEntity evento2 = new EventoEntity(1, azienda, "Pranzo Aziendale", "Ristorante 'Da Mario'", now.plusDays(1).withHour(13).withMinute(0), now.plusDays(1).withHour(15).withMinute(0));
        EventoEntity evento3 = new EventoEntity(2, azienda, "Corso Formazione", "Online", now.minusDays(1).withHour(10).withMinute(0), now.minusDays(1).withHour(12).withMinute(0));
        EventoEntity evento4 = new EventoEntity(3, azienda, "Brainstorming", "Ufficio", now.withHour(9).withMinute(0), now.withHour(11).withMinute(0)); // Oggi

        evento1.setCreatore(utente);
        evento2.setCreatore(utente);
        evento3.setCreatore(utente);
        evento4.setCreatore(utente);

        eventoRepository.save(evento1);
        eventoRepository.save(evento2);
        eventoRepository.save(evento3);
        eventoRepository.save(evento4);

        // Associa eventi all'utente
        partecipazioneRepository.save(new PartecipazioneEntity(utente.getId_utente(), evento1.getId_evento(), azienda.getId_azienda()));
        partecipazioneRepository.save(new PartecipazioneEntity(utente.getId_utente(), evento2.getId_evento(), azienda.getId_azienda()));
        partecipazioneRepository.save(new PartecipazioneEntity(utente.getId_utente(), evento3.getId_evento(), azienda.getId_azienda()));
        partecipazioneRepository.save(new PartecipazioneEntity(utente.getId_utente(), evento4.getId_evento(), azienda.getId_azienda()));
    }

    public void addModulinkAzienda() {
        AziendaEntity modulink = new AziendaEntity("Modulink","1111111111","Via Nazionale","Santa Maria a Vico","81028","+393471304385","");
        UtenteEntity u3 = new UtenteEntity(modulink,"r.cito@studenti.unisa.it",PasswordUtility.hashPassword("ciaociao"),"Roberto","Cito","+393471304385","");
        modulink = aziendaService.registraAzienda(modulink);
        customUserDetailsService.registraUtente(u3, modulink.getId_azienda());
        RuoloEntity ruoloResponsabile2 = new RuoloEntity(0, modulink, "Responsabile", "#000000", "Responsabile dell'azienda");
        RuoloEntity ruoloNewUser2 = new RuoloEntity(1,modulink,"Utente Nuovo","blue","Utente non ancora ufficialmente registrato");
        RuoloEntity ruoloStandard2 = new RuoloEntity(2,modulink,"Utente","grey","Utente Standard");
        ruoloRepository.save(ruoloResponsabile2);
        ruoloRepository.save(ruoloNewUser2);
        ruoloRepository.save(ruoloStandard2);
        associazioneRepository.save(new AssociazioneEntity(u3, ruoloResponsabile2));
        ModuloEntity Support = new ModuloEntity(7,"Supporto", "Modulo dedicato al supporto tecnico", "/dashboard/support/", "bi bi-life-preserver",false);
        Support = moduloRepository.save(Support);
        attivazioneRepository.save(new AttivazioneEntity(Support, modulink));
        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile2.getId_ruolo(),Support.getId_modulo(),modulink.getId_azienda()));
    }

    @Transactional // Qui la transazione funzionerà correttamente!
    public void runInitialization() {
        addDemoAzienda();
        addModulinkAzienda();
    }
}