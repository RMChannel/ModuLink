package com.modulink.Model.Eventi;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia DAO per l'accesso ai dati dell'entità {@link EventoEntity}.
 * <p>
 * Estende {@link JpaRepository} tipizzato con la chiave composta {@link EventoID}.
 * Fornisce query custom JPQL per la gestione di partecipazioni e lookup specifici per tenant (Azienda).
 * </p>
 *
 * @see EventoEntity
 * @see EventoID
 * @author Modulink Team
 * @version 1.5.0
 * @since 1.2.0
 */
@Repository
public interface EventoRepository extends JpaRepository<EventoEntity, EventoID> {

    /**
     * Recupera tutti gli eventi associati a una specifica azienda.
     * <p>
     * Metodo derivato dalla naming convention di Spring Data JPA.
     * Esegue una query filtrata sulla colonna Foreign Key verso {@link AziendaEntity}.
     * </p>
     *
     * @param azienda L'entità azienda target.
     * @return Lista di eventi appartenenti all'azienda.
     * @since 1.2.0
     */
    @Transactional
    List<EventoEntity> findByAzienda(AziendaEntity azienda);

    /**
     * Recupera tutti gli eventi rilevanti per un determinato utente, sia come creatore che come partecipante.
     * <p>
     * Utilizza una query JPQL custom con {@code LEFT JOIN} esplicita sull'entità {@code PartecipazioneEntity}
     * per includere eventi a cui l'utente partecipa, unendo i risultati (in OR logico) con gli eventi creati dall'utente stesso.
     * La clausola {@code DISTINCT} rimuove duplicati nel caso un utente sia sia creatore che partecipante.
     * </p>
     *
     * @param utente L'utente per cui cercare gli eventi.
     * @return Lista di eventi correlati all'utente.
     * @since 1.4.0
     */
    @Query("SELECT DISTINCT e FROM EventoEntity e LEFT JOIN PartecipazioneEntity p ON p.evento = e WHERE p.utente = :utente OR e.creatore = :utente")
    @Transactional
    List<EventoEntity> findAllByUtente(@Param("utente") UtenteEntity utente);

    /**
     * Recupera l'entità evento con l'identificativo locale {@code id_evento} più alto per una data azienda.
     * <p>
     * Utilizzato per determinare il prossimo ID disponibile nella sequenza locale.
     * La query annidata seleziona il MAX(id_evento) e la query esterna recupera l'intera entità corrispondente.
     * </p>
     *
     * @param azienda L'azienda contesto della query.
     * @return L'entità {@link EventoEntity} con ID massimo, oppure {@code null} se nessun evento esiste per l'azienda.
     * @since 1.2.0
     */
    @Query("SELECT e FROM EventoEntity e WHERE e.azienda = :azienda AND e.id_evento = (SELECT MAX(e2.id_evento) FROM EventoEntity e2 WHERE e2.azienda = :azienda)")
    EventoEntity findMaxIdByAzienda(@Param("azienda") AziendaEntity azienda);

    /**
     * Elimina massivamente tutti gli eventi di un'azienda.
     * <p>
     * Utile per operazioni di pulizia o dismissione tenant.
     * </p>
     *
     * @param azienda L'azienda i cui eventi devono essere rimossi.
     * @since 1.2.0
     */
    void deleteAllByAzienda(AziendaEntity azienda);
}