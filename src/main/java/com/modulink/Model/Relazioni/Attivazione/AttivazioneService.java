package com.modulink.Model.Relazioni.Attivazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        AttivazioneEntity attivazione=new AttivazioneEntity(moduloRepository.getReferenceById(0),aziendaEntity);
        attivazioneRepository.save(attivazione);
    }
}
