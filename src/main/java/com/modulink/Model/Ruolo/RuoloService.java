package com.modulink.Model.Ruolo;

import com.modulink.Model.Azienda.AziendaEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuoloService {
    private final RuoloRepository ruoloRepository;

    public RuoloService(RuoloRepository ruoloRepository) {
        this.ruoloRepository = ruoloRepository;
    }

    @Transactional
    public RuoloEntity attivazioneDefault(AziendaEntity aziendaEntity) {
        List<RuoloEntity> ruoli=new ArrayList<>();
        RuoloEntity ruoloResponsabile = new RuoloEntity(0,aziendaEntity,"Responsabile","#000000","Responsabile dell'azienda");
        ruoli.add(ruoloResponsabile);
        ruoli.add(new RuoloEntity(1,aziendaEntity,"Utente Nuovo","blue","Utente non ancora ufficialmente registrato"));
        ruoli.add(new RuoloEntity(2,aziendaEntity,"Utente","grey","Utente Standard"));
        ruoloRepository.saveAll(ruoli);
        return ruoloResponsabile;
    }

    public RuoloEntity getResponsabile(AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(0, azienda.getId_azienda())).orElseThrow();
    }

    public RuoloEntity getNewUser(AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(1, azienda.getId_azienda())).orElseThrow();
    }

    public RuoloEntity getStandardUser(AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(2, azienda.getId_azienda())).orElseThrow();
    }
}
