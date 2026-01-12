package com.modulink.Model.Relazioni.Partecipazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartecipazioneService {

    @Autowired
    private PartecipazioneRepository partecipazioneRepository;

    public List<PartecipazioneEntity> findAll() {
        return partecipazioneRepository.findAll();
    }

    public Optional<PartecipazioneEntity> findById(PartecipazioneID id) {
        return partecipazioneRepository.findById(id);
    }

    public PartecipazioneEntity save(PartecipazioneEntity partecipazione) {
        return partecipazioneRepository.save(partecipazione);
    }

    public void deleteById(PartecipazioneID id) {
        partecipazioneRepository.deleteById(id);
    }
}