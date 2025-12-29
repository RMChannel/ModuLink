package com.modulink.Model.Ruolo;

import com.modulink.Model.Azienda.AziendaEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RuoloService {
    private final RuoloRepository ruoloRepository;

    public RuoloService(RuoloRepository ruoloRepository) {
        this.ruoloRepository = ruoloRepository;
    }

    @Transactional
    public RuoloEntity attivazioneDefault(AziendaEntity aziendaEntity) {
        RuoloEntity ruoloResponsabile = new RuoloEntity(0,aziendaEntity,"Responsabile","#000000","Responsabile dell'azienda");
        return ruoloRepository.save(ruoloResponsabile);
    }
}
