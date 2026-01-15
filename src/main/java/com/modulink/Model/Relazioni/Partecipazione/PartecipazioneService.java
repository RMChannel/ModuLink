package com.modulink.Model.Relazioni.Partecipazione;

import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Eventi.EventoRepository;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PartecipazioneService {
    private final PartecipazioneRepository partecipazioneRepository;
    private final EventoRepository eventoRepository;
    public PartecipazioneService(PartecipazioneRepository partecipazioneRepository,EventoRepository eventoRepository) {
        this.partecipazioneRepository = partecipazioneRepository;
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public void Invita(EventoEntity eventoEntity, UtenteEntity utenteEntity){
        PartecipazioneEntity partecipazioneEntity = new PartecipazioneEntity(utenteEntity.getId_utente(), eventoEntity.getId_evento(), utenteEntity.getAzienda().getId_azienda());
        partecipazioneRepository.save(partecipazioneEntity);
    }

    public List<UtenteEntity> getUtenteEntitiesByEvento(EventoEntity eventoEntity){
        List<UtenteEntity> utenteEntities = new ArrayList<>();
        partecipazioneRepository.getPartecipazioneEntitiesByEvento(eventoEntity).forEach(c->{
            utenteEntities.add(c.getUtente());
        });
        return utenteEntities;
    }

    public void RimuoviInvito(EventoEntity eventoEntity, UtenteEntity utenteEntity){
        partecipazioneRepository.removeByUtenteAndEvento(utenteEntity, eventoEntity);
    }

}