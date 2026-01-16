package com.modulink.Model.Relazioni.Partecipazione;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartecipazioneRepository extends JpaRepository<PartecipazioneEntity, PartecipazioneID> {
}