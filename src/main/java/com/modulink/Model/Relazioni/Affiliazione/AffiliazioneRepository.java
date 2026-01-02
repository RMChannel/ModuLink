package com.modulink.Model.Relazioni.Affiliazione;

import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AffiliazioneRepository extends JpaRepository<AffiliazioneEntity, AffiliazioneID> {

    @Query("SELECT a.attivazione.modulo FROM AffiliazioneEntity a WHERE a.ruolo = :ruolo")
    List<ModuloEntity> findModuliByRuolo(@Param("ruolo") RuoloEntity ruolo);

    @Query("SELECT a.attivazione.modulo FROM AffiliazioneEntity a WHERE a.id_ruolo = :idRuolo AND a.id_azienda = :idAzienda")
    List<ModuloEntity> findModuliByRuoloId(@Param("idRuolo") int idRuolo, @Param("idAzienda") int idAzienda);
}
