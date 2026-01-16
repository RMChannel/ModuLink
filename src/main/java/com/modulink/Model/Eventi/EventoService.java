package com.modulink.Model.Eventi;
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
    public List<EventoEntity> findAll() {
        return eventoRepository.findAll();
    }
}
