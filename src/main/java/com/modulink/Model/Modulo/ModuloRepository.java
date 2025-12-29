package com.modulink.Model.Modulo;

import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuloRepository extends JpaRepository<ModuloEntity, Integer> {

    @Query("SELECT DISTINCT aff.attivazione.modulo FROM AffiliazioneEntity aff " +
           "JOIN AssociazioneEntity ass ON aff.ruolo = ass.ruolo " +
           "WHERE ass.utente = :utente")
    List<ModuloEntity> findModuliByUtente(@Param("utente") UtenteEntity utente);
}
