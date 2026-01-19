package com.modulink.Model.Relazioni.Partecipazione;

import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository per la gestione delle operazioni CRUD sulle partecipazioni agli eventi.
 *
 * @author Modulink Team
 * @version 2.0.0
 * @since 1.2.5
 */
@Repository
public interface PartecipazioneRepository extends JpaRepository<PartecipazioneEntity, PartecipazioneID> {
    /**
     * Recupera tutte le partecipazioni per uno specifico evento.
     *
     * @param evento L'evento di riferimento.
     * @return Lista di partecipazioni.
     * @since 1.2.5
     */
    public List<PartecipazioneEntity> getPartecipazioneEntitiesByEvento(EventoEntity evento);

    /**
     * Rimuove la partecipazione di un utente a un evento specifico (Annulla iscrizione).
     *
     * @param utente L'utente che annulla la partecipazione.
     * @param evento L'evento da cui disiscriversi.
     * @since 1.2.5
     */
    @Transactional
    void removeByUtenteAndEvento(UtenteEntity utente, EventoEntity evento);
}