package com.modulink;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaRepository;
import com.modulink.Model.Azienda.AziendaService;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneRepository;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloRepository;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UserRepository;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabasePopulator {
    @Bean
    CommandLineRunner initDatabase(AziendaService aziendaService, CustomUserDetailsService customUserDetailsService, RuoloRepository ruoloRepository, AssociazioneRepository associazioneRepository) {
        return args -> {
            AziendaEntity aziendaEntity=new AziendaEntity("Test","12345678901","Via Nazionale","Santa Maria a Vico","81028","+393471304385","");
            UtenteEntity utenteEntity=new UtenteEntity(aziendaEntity,"robbencito@gmail.com", PasswordUtility.hashPassword("ciaociao"),"Roberto","Cito","+393471304385","");

            aziendaEntity=aziendaService.registraAzienda(aziendaEntity);
            customUserDetailsService.registraUtente(utenteEntity,aziendaEntity.getId_azienda());

            RuoloEntity ruoloResponsabile = new RuoloEntity(0,aziendaEntity,"Responsabile","#000000","Responsabile dell'azienda");
            AssociazioneEntity associazioneEntity = new AssociazioneEntity(utenteEntity,ruoloResponsabile);

            ruoloRepository.save(ruoloResponsabile);
            associazioneRepository.save(associazioneEntity);

            System.out.println("Database populato con successo");
        };
    }
}
