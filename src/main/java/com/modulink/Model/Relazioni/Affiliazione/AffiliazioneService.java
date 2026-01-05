package com.modulink.Model.Relazioni.Affiliazione;

import com.modulink.Model.Azienda.AziendaEntity;
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

        AffiliazioneEntity affiliazione1=new AffiliazioneEntity(0,0,aziendaEntity.getId_azienda());
        AffiliazioneEntity affiliazione2=new AffiliazioneEntity(0,1,aziendaEntity.getId_azienda());

        affiliazioni.add(affiliazione1);
        affiliazioni.add(affiliazione2);
        affiliazioneRepository.saveAll(affiliazioni);
    }
}
