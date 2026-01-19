package com.modulink.Model.Task;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository JPA per la gestione dell'entità {@link TaskEntity}.
 * <p>
 * Questa interfaccia fornisce metodi per eseguire operazioni CRUD e query personalizzate
 * sulla tabella "task", inclusa la ricerca per utente creatore, il recupero dell'ultimo task
 * inserito per un'azienda e operazioni di cancellazione native.
 * </p>
 *
 * @author Modulink Team
 * @version 1.4.2
 */
public interface TaskRepository extends JpaRepository<TaskEntity, TaskID> {

    /**
     * Trova tutti i task creati da uno specifico utente.
     *
     * @param utenteCreatore L'entità dell'utente che ha creato i task.
     * @return Una lista di task creati dall'utente specificato.
     */
    List<TaskEntity> findByUtenteCreatore(UtenteEntity utenteCreatore);

    /**
     * Recupera il task con l'ID più alto associato a una specifica azienda.
     * <p>
     * Questo metodo è utile per determinare l'ultimo task inserito per un'azienda.
     * Utilizza una query JPQL personalizzata.
     * </p>
     *
     * @param azienda L'entità dell'azienda per cui cercare il task.
     * @return Il task con l'ID massimo per l'azienda data, o null se non presente.
     */
    @Query("SELECT t FROM TaskEntity t WHERE t.azienda = :azienda AND t.id_task = (SELECT MAX(t2.id_task) FROM TaskEntity t2 WHERE t2.azienda = :azienda)")
    TaskEntity findMaxByAzienda(@Param("azienda") AziendaEntity azienda);

    /**
     * Elimina un task specifico dal database utilizzando una query nativa SQL.
     * <p>
     * Questo metodo esegue una cancellazione fisica del record basata sulla chiave primaria composta.
     * </p>
     *
     * @param idTask    L'ID del task da eliminare.
     * @param idAzienda L'ID dell'azienda associata al task.
     */
    @Modifying
    @Query(value = "DELETE FROM task WHERE ID_Task = :idTask AND ID_Azienda = :idAzienda", nativeQuery = true)
    void deleteByIdNative(@Param("idTask") int idTask, @Param("idAzienda") int idAzienda);

    /**
     * Elimina tutti i task di un'azienda specifica utilizzando una query nativa SQL.
     *
     * @param idAzienda L'ID dell'azienda di cui eliminare i task.
     */
    @Modifying
    @Query(value = "DELETE FROM task WHERE ID_Azienda = :idAzienda", nativeQuery = true)
    void deleteAllByAziendaNative(@Param("idAzienda") int idAzienda);
}
