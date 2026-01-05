package com.modulink.Model.Relazioni.Attivazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttivazioneService {
    private final AttivazioneRepository attivazioneRepository;
    private final ModuloRepository moduloRepository;

    public AttivazioneService(AttivazioneRepository attivazioneRepository, ModuloRepository moduloRepository) {
        this.attivazioneRepository=attivazioneRepository;
        this.moduloRepository=moduloRepository;
    }

    @Transactional
    public void attivazioneDefault(AziendaEntity aziendaEntity) {
        List<AttivazioneEntity> attivazioni=new ArrayList<>();

        AttivazioneEntity attivazione1=new AttivazioneEntity(moduloRepository.getReferenceById(0),aziendaEntity);
        AttivazioneEntity attivazione2=new AttivazioneEntity(moduloRepository.getReferenceById(1),aziendaEntity);

        attivazioni.add(attivazione1);
        attivazioni.add(attivazione2);
        attivazioneRepository.saveAll(attivazioni);
    }
}
