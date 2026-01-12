package com.modulink.Model.Relazioni.Partecipazione;

import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartecipazioneRepository extends JpaRepository<PartecipazioneEntity, PartecipazioneID> {
    public List<PartecipazioneEntity> findByUtente(UtenteEntity utente);


}