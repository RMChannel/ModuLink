package com.modulink.Model.Eventi;
import com.modulink.Model.Azienda.AziendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventoRepository extends JpaRepository<EventoEntity, EventoID> {
    List<EventoEntity> findByAzienda(AziendaEntity azienda);

}