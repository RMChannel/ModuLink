package com.modulink.DatabasePopulator;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaService;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneEntity;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneRepository;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneRepository;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneRepository;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloRepository;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataInitializerService {

    private final AziendaService aziendaService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RuoloRepository ruoloRepository;
    private final AssociazioneRepository associazioneRepository;
    private final ModuloRepository moduloRepository;
    private final AttivazioneRepository attivazioneRepository;
    private final AffiliazioneRepository affiliazioneRepository;

    public DataInitializerService(AziendaService aziendaService, CustomUserDetailsService customUserDetailsService, RuoloRepository ruoloRepository, AssociazioneRepository associazioneRepository, ModuloRepository moduloRepository, AttivazioneRepository attivazioneRepository, AffiliazioneRepository affiliazioneRepository) {
        this.aziendaService = aziendaService;
        this.customUserDetailsService = customUserDetailsService;
        this.ruoloRepository = ruoloRepository;
        this.associazioneRepository = associazioneRepository;
        this.moduloRepository = moduloRepository;
        this.attivazioneRepository = attivazioneRepository;
        this.affiliazioneRepository = affiliazioneRepository;
    }

    @Transactional // Qui la transazione funzionerà correttamente!
    public void runInitialization() {
        AziendaEntity azienda = new AziendaEntity("Test","12345678901","Via Nazionale","Santa Maria a Vico","81028","+393205397560","");
        UtenteEntity utente = new UtenteEntity(azienda, "robbencito@gmail.com", PasswordUtility.hashPassword("ciaociao"), "Roberto", "Cito", "+393471304385", "");

        // Importante: riassegna sempre l'oggetto restituito dal save/registrazione
        azienda = aziendaService.registraAzienda(azienda);
        customUserDetailsService.registraUtente(utente, azienda.getId_azienda());

        RuoloEntity ruoloResponsabile = new RuoloEntity(0, azienda, "Responsabile", "#000000", "Responsabile dell'azienda");
        RuoloEntity ruoloNewUser = new RuoloEntity(1,azienda,"Utente Nuovo","blue","Utente non ancora ufficialmente registrato");
        RuoloEntity ruoloStandard = new RuoloEntity(2,azienda,"Utente","grey","Utente Standard");
        ruoloRepository.save(ruoloResponsabile);
        ruoloRepository.save(ruoloNewUser);
        ruoloRepository.save(ruoloStandard);

        AssociazioneEntity associazione = new AssociazioneEntity(utente, ruoloResponsabile);
        associazioneRepository.save(associazione);

        ModuloEntity GDU = new ModuloEntity(0, "Gestione Utenti", "Permette la gestione di tutti gli utenti della propria azienda", "/dashboard/gdu/", "bi bi-person-lines-fill");
        ModuloEntity GDR = new ModuloEntity(1, "Gestione Ruoli", "Permette la gestione e l'assegnazione dei ruoli", "/dashboard/gdr/", "bi bi-award-fill");
        ModuloEntity GDM = new ModuloEntity(2, "Gestione Moduli", "Permette la gestione di tutti i moduli integrati nella propria azienda","/dashboard/gdm/","bi bi-database-gear");
        ModuloEntity store = new ModuloEntity(3,"Store","Store dei moduli","/dashboard/store/","bi bi-cart-dash");
        ModuloEntity calendario = new ModuloEntity(4,"Calendario", "Permette di organizzare e creare eventi", "/dashboard/calendario/", "bi bi-calendar");

        GDU = moduloRepository.save(GDU);
        GDR = moduloRepository.save(GDR);
        GDM = moduloRepository.save(GDM);
        store = moduloRepository.save(store);
        calendario = moduloRepository.save(calendario);

        // Ora azienda e modulo sono MANAGED nella stessa transazione
        attivazioneRepository.save(new AttivazioneEntity(GDU, azienda));
        attivazioneRepository.save(new AttivazioneEntity(GDR, azienda));
        attivazioneRepository.save(new AttivazioneEntity(GDM, azienda));
        attivazioneRepository.save(new AttivazioneEntity(store, azienda));


        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),GDU.getId_modulo(),azienda.getId_azienda()));
        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),GDR.getId_modulo(),azienda.getId_azienda()));
        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),GDM.getId_modulo(),azienda.getId_azienda()));
        affiliazioneRepository.save(new AffiliazioneEntity(ruoloResponsabile.getId_ruolo(),store.getId_modulo(),azienda.getId_azienda()));
    }
}