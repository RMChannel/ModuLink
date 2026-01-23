package com.modulink.Model.Relazioni.Assegnazione;

import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA per la gestione delle operazioni CRUD e delle query personalizzate sull'entità {@link AssegnazioneEntity}.
 * <p>
 * Fornisce metodi per trovare assegnazioni per utente, eliminare assegnazioni specifiche o basate su task e azienda,
 * e inserire nuove assegnazioni.
 * </p>
 *
 * @author Modulink Team
 * @version 2.1.0
 * @since 1.0.5
 */
public interface AssegnazioneRepository extends JpaRepository<AssegnazioneEntity, AssegnazioneID> {

    /**
     * Trova tutte le assegnazioni associate a uno specifico utente.
     *
     * @param utente L'entità {@link UtenteEntity} per la quale cercare le assegnazioni.
     * @return Una lista di {@link AssegnazioneEntity} trovate.
     */
    List<AssegnazioneEntity> findByUtente(UtenteEntity utente);

    /**
     * Elimina tutte le assegnazioni relative a uno specifico Task all'interno di un'Azienda.
     *
     * @param idTask    L'ID del Task.
     * @param idAzienda L'ID dell'Azienda.
     * @since 1.0.5
     */
    @Modifying
    @Query(value = "DELETE FROM assegnazione WHERE ID_Task = :idTask AND ID_Azienda = :idAzienda", nativeQuery = true)
    void deleteByTaskId(@Param("idTask") int idTask, @Param("idAzienda") int idAzienda);

    /**
     * Elimina una specifica assegnazione identificata da Task, Azienda e Utente.
     *
     * @param idTask    L'ID del Task.
     * @param idAzienda L'ID dell'Azienda.
     * @param idUtente  L'ID dell'Utente.
     */
    @Modifying
    @Query(value = "DELETE FROM assegnazione WHERE ID_Task = :idTask AND ID_Azienda = :idAzienda AND ID_Utente = :idUtente", nativeQuery = true)
    void deleteByIds(@Param("idTask") int idTask, @Param("idAzienda") int idAzienda, @Param("idUtente") int idUtente);

    /**
     * Inserisce una nuova assegnazione nel database utilizzando una query nativa.
     *
     * @param idTask    L'ID del Task da assegnare.
     * @param idAzienda L'ID dell'Azienda.
     * @param idUtente  L'ID dell'Utente a cui assegnare il Task.
     */
    @Modifying
    @Query(value = "INSERT INTO assegnazione (ID_Task, ID_Azienda, ID_Utente) VALUES (:idTask, :idAzienda, :idUtente)", nativeQuery = true)
    void insertAssegnazione(@Param("idTask") int idTask, @Param("idAzienda") int idAzienda, @Param("idUtente") int idUtente);

    /**
     * Elimina tutte le assegnazioni associate a una specifica Azienda.
     *
     * @param idAzienda L'ID dell'Azienda per la quale rimuovere tutte le assegnazioni.
     */
    @Modifying
    @Query(value = "DELETE FROM assegnazione WHERE ID_Azienda = :idAzienda", nativeQuery = true)
    void deleteByIdAzienda(@Param("idAzienda") int idAzienda);
}
