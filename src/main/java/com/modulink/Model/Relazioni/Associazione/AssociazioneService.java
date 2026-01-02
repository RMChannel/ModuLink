package com.modulink.Model.Relazioni.Associazione;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssociazioneService {
    private final AssociazioneRepository associazioneRepository;

    public AssociazioneService(AssociazioneRepository associazioneRepository) {
        this.associazioneRepository = associazioneRepository;
    }

    @Transactional
    public void save(AssociazioneEntity associazioneEntity) {
        associazioneRepository.save(associazioneEntity);
    }
}
