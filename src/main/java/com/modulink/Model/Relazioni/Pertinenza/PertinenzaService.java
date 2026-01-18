package com.modulink.Model.Relazioni.Pertinenza;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PertinenzaService {
    private final PertinenzaRepository pertinenzaRepository;

    public PertinenzaService(PertinenzaRepository pertinenzaRepository) {
        this.pertinenzaRepository = pertinenzaRepository;
    }

    @Transactional
    public void attivazioneDefault(AziendaEntity aziendaEntity) {
        List<PertinenzaEntity> affiliazioni=new ArrayList<>();

        affiliazioni.add(new PertinenzaEntity(0,0,aziendaEntity.getId_azienda()));
        affiliazioni.add(new PertinenzaEntity(0,1,aziendaEntity.getId_azienda()));
        affiliazioni.add(new PertinenzaEntity(0,2,aziendaEntity.getId_azienda()));
        affiliazioni.add(new PertinenzaEntity(0,3,aziendaEntity.getId_azienda()));
        affiliazioni.add(new PertinenzaEntity(0,9999,aziendaEntity.getId_azienda()));

        pertinenzaRepository.saveAll(affiliazioni);
    }

    public List<PertinenzaEntity> findAllByAttivazione(AttivazioneEntity attivazione) {
        return pertinenzaRepository.findAllByAttivazione(attivazione);
    }
}
