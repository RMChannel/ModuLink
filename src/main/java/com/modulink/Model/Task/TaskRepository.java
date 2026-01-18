package com.modulink.Model.Task;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, TaskID> {
    List<TaskEntity> findByUtenteCreatore(UtenteEntity utenteCreatore);

    @Query("SELECT t FROM TaskEntity t WHERE t.azienda = :azienda AND t.id_task = (SELECT MAX(t2.id_task) FROM TaskEntity t2 WHERE t2.azienda = :azienda)")
    TaskEntity findMaxByAzienda(@Param("azienda") AziendaEntity azienda);

    @Modifying
    @Query(value = "DELETE FROM task WHERE ID_Task = :idTask AND ID_Azienda = :idAzienda", nativeQuery = true)
    void deleteByIdNative(@Param("idTask") int idTask, @Param("idAzienda") int idAzienda);

    void deleteAllByAzienda(AziendaEntity azienda);

    @Modifying
    @Query(value = "DELETE FROM task WHERE ID_Azienda = :idAzienda", nativeQuery = true)
    void deleteAllByAziendaNative(@Param("idAzienda") int idAzienda);
}
