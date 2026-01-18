package com.modulink.Model.Relazioni.Pertinenza;

import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PertinenzaRepository extends JpaRepository<PertinenzaEntity, PertinenzaID> {

    @Query("SELECT a.attivazione.modulo FROM PertinenzaEntity a WHERE a.ruolo = :ruolo")
    List<ModuloEntity> findModuliByRuolo(@Param("ruolo") RuoloEntity ruolo);

    @Query("SELECT a.attivazione.modulo FROM PertinenzaEntity a WHERE a.id_ruolo = :idRuolo AND a.id_azienda = :idAzienda")
    List<ModuloEntity> findModuliByRuoloId(@Param("idRuolo") int idRuolo, @Param("idAzienda") int idAzienda);

    List<PertinenzaEntity> findAllByAttivazione(AttivazioneEntity attivazione);

    void removeByAttivazione(AttivazioneEntity attivazione);
}
