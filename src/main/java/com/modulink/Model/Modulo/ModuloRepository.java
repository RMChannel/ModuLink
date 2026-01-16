package com.modulink.Model.Modulo;

import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuloRepository extends JpaRepository<ModuloEntity, Integer> {

    @Query("SELECT DISTINCT aff.attivazione.modulo FROM PertinenzaEntity aff " +
           "JOIN AssociazioneEntity ass ON aff.ruolo = ass.ruolo " +
           "WHERE ass.utente = :utente")
    List<ModuloEntity> findModuliByUtente(@Param("utente") UtenteEntity utente);

    @Query("SELECT m FROM ModuloEntity m WHERE :url LIKE CONCAT(m.url_modulo, '%') ORDER BY LENGTH(m.url_modulo) DESC")
    List<ModuloEntity> findMatchingModuli(@Param("url") String url);

    @Query("SELECT DISTINCT aff.attivazione.modulo FROM PertinenzaEntity aff " +
            "JOIN AssociazioneEntity ass ON aff.ruolo = ass.ruolo " +
            "WHERE ass.utente = :utente AND aff.id_modulo= :id")
    List<ModuloEntity> isModuloAccessible(@Param("id") int id, @Param("utente") UtenteEntity utente);
}
