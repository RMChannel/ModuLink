package com.modulink.Model.Relazioni.Partecipazione;

import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PartecipazioneService {
    private final PartecipazioneRepository partecipazioneRepository;
    public PartecipazioneService(PartecipazioneRepository partecipazioneRepository) {
        this.partecipazioneRepository = partecipazioneRepository;
    }

    @Transactional
    public void Invita(EventoEntity eventoEntity, UtenteEntity utenteEntity){
        PartecipazioneEntity partecipazioneEntity = new PartecipazioneEntity(eventoEntity.getId_evento(),utenteEntity.getId_utente(),utenteEntity.getAzienda().getId_azienda());
        partecipazioneRepository.save(partecipazioneEntity);
    }
}