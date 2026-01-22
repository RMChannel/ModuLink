package com.modulink.Model.Relazioni.Assegnazione;

import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssegnazioneRepository extends JpaRepository<AssegnazioneEntity, AssegnazioneID> {
    List<AssegnazioneEntity> findByUtente(UtenteEntity utente);

    @Modifying
    @Query(value = "DELETE FROM assegnazione WHERE ID_Task = :idTask AND ID_Azienda = :idAzienda", nativeQuery = true)
    void deleteByTaskId(@Param("idTask") int idTask, @Param("idAzienda") int idAzienda);

    @Modifying
    @Query(value = "DELETE FROM assegnazione WHERE ID_Task = :idTask AND ID_Azienda = :idAzienda AND ID_Utente = :idUtente", nativeQuery = true)
    void deleteByIds(@Param("idTask") int idTask, @Param("idAzienda") int idAzienda, @Param("idUtente") int idUtente);

    @Modifying
    @Query(value = "INSERT INTO assegnazione (ID_Task, ID_Azienda, ID_Utente) VALUES (:idTask, :idAzienda, :idUtente)", nativeQuery = true)
    void insertAssegnazione(@Param("idTask") int idTask, @Param("idAzienda") int idAzienda, @Param("idUtente") int idUtente);

    @Modifying
    @Query(value = "DELETE FROM assegnazione WHERE ID_Azienda = :idAzienda", nativeQuery = true)
    void deleteByIdAzienda(@Param("idAzienda") int idAzienda);
}
