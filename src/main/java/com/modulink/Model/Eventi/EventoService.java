package com.modulink.Model.Eventi;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventoService {
    EventoRepository eventoRepository;
    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public void create(EventoEntity eventoEntity){
        eventoRepository.save(eventoEntity);
    }

    @Transactional
    public List<EventoEntity> findAllByUtente(UtenteEntity utente) {
        return eventoRepository.findAllByUtente(utente);
    }

    @Transactional
    public List<EventoEntity> findByAzienda(AziendaEntity aziendaEntity) {
        return eventoRepository.findByAzienda(aziendaEntity);
    }

    @Transactional
    public List<EventoEntity> findAllByAziendaAndNotUtente(UtenteEntity utenteEntity) {

    }
}
