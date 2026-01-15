package com.modulink.Model.Eventi;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public EventoEntity findById(EventoID eventoID) throws EventoNotFound {
        Optional<EventoEntity> evento = eventoRepository.findById(eventoID);
        if (evento.isPresent()) {
            return evento.get();
        }else  {
            throw new EventoNotFound("Evento non trovato");
        }
    }

    public void update(EventoEntity evento) {
        eventoRepository.save(evento);
    }
}
