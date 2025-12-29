package com.modulink.DatabasePopulator;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaService;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
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

    public DataInitializerService(AziendaService aziendaService, CustomUserDetailsService customUserDetailsService, RuoloRepository ruoloRepository, AssociazioneRepository associazioneRepository, ModuloRepository moduloRepository, AttivazioneRepository attivazioneRepository) {
        this.aziendaService = aziendaService;
        this.customUserDetailsService = customUserDetailsService;
        this.ruoloRepository = ruoloRepository;
        this.associazioneRepository = associazioneRepository;
        this.moduloRepository = moduloRepository;
        this.attivazioneRepository = attivazioneRepository;
    }

    @Transactional // Qui la transazione funzionerà correttamente!
    public void runInitialization() {
        AziendaEntity azienda = new AziendaEntity("Test","12345678901","Via Nazionale","Santa Maria a Vico","81028","+393205397560","");
        UtenteEntity utente = new UtenteEntity(azienda, "robbencito@gmail.com", PasswordUtility.hashPassword("ciaociao"), "Roberto", "Cito", "+393471304385", "");

        // Importante: riassegna sempre l'oggetto restituito dal save/registrazione
        azienda = aziendaService.registraAzienda(azienda);
        customUserDetailsService.registraUtente(utente, azienda.getId_azienda());

        RuoloEntity ruoloResponsabile = new RuoloEntity(0, azienda, "Responsabile", "#000000", "Responsabile dell'azienda");
        ruoloRepository.save(ruoloResponsabile);

        AssociazioneEntity associazione = new AssociazioneEntity(utente, ruoloResponsabile);
        associazioneRepository.save(associazione);

        ModuloEntity modulo = new ModuloEntity(0, "Gestione Utenti", "Permette la gestione di tutti gli utenti della propria azienda", "/manange-users", "");
        modulo = moduloRepository.save(modulo);

        // Ora azienda e modulo sono MANAGED nella stessa transazione
        AttivazioneEntity attivazione = new AttivazioneEntity(modulo, azienda);
        attivazioneRepository.save(attivazione);
    }
}