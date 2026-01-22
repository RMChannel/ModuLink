package com.modulink.Model.Relazioni.Attivazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttivazioneRepository extends JpaRepository<AttivazioneEntity,AttivazioneID> {

    @Query("SELECT a.modulo FROM AttivazioneEntity a WHERE a.azienda = :azienda")
    public List<ModuloEntity> findModuliByAzienda(@Param("azienda") AziendaEntity azienda);

    @Query("SELECT m FROM ModuloEntity m WHERE m NOT IN (SELECT a.modulo FROM AttivazioneEntity a WHERE a.azienda = :azienda) AND m.Visible = true")
    public List<ModuloEntity> getAllNotPurchased(@Param("azienda") AziendaEntity azienda);

    @Query("SELECT m FROM ModuloEntity m WHERE m IN (SELECT a.modulo FROM AttivazioneEntity a WHERE a.azienda = :azienda) AND m.Visible = true")
    public List<ModuloEntity> getAllPurchased(@Param("azienda") AziendaEntity azienda);

    boolean existsByAziendaAndModulo(AziendaEntity azienda, ModuloEntity modulo);
}
