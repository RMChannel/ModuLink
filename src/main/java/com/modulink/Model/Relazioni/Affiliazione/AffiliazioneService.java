package com.modulink.Model.Relazioni.Affiliazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AffiliazioneService {
    private final AffiliazioneRepository affiliazioneRepository;

    public AffiliazioneService(AffiliazioneRepository affiliazioneRepository) {
        this.affiliazioneRepository = affiliazioneRepository;
    }

    @Transactional
    public void attivazioneDefault(AziendaEntity aziendaEntity) {
        List<AffiliazioneEntity> affiliazioni=new ArrayList<>();

        affiliazioni.add(new AffiliazioneEntity(0,0,aziendaEntity.getId_azienda()));
        affiliazioni.add(new AffiliazioneEntity(0,1,aziendaEntity.getId_azienda()));
        affiliazioni.add(new AffiliazioneEntity(0,2,aziendaEntity.getId_azienda()));
        affiliazioni.add(new AffiliazioneEntity(0,3,aziendaEntity.getId_azienda()));

        affiliazioneRepository.saveAll(affiliazioni);
    }

    public List<AffiliazioneEntity> findAllByAttivazione(AttivazioneEntity attivazione) {
        return affiliazioneRepository.findAllByAttivazione(attivazione);
    }
}
