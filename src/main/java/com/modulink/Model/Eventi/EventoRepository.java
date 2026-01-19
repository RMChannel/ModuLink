package com.modulink.Model.Eventi;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EventoRepository extends JpaRepository<EventoEntity, EventoID> {
    List<EventoEntity> findByAzienda(AziendaEntity azienda);

    @Query("SELECT p.evento FROM PartecipazioneEntity p WHERE p.utente = :utente")
    List<EventoEntity> findAllByUtente(@Param("utente") UtenteEntity utente);

    @Query("SELECT COALESCE(MAX(e.id_evento), 0) FROM EventoEntity e WHERE e.azienda = :azienda")
    int findMaxIdByAzienda(@Param("azienda") AziendaEntity azienda);
}