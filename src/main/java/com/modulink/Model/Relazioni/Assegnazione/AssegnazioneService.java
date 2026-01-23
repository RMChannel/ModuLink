package com.modulink.Model.Relazioni.Assegnazione;

import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service per la gestione della logica di business relativa alle assegnazioni dei Task agli Utenti.
 * <p>
 * Fornisce metodi transazionali per salvare, eliminare e recuperare le assegnazioni,
 * interagendo con {@link AssegnazioneRepository}.
 * </p>
 *
 * @author Modulink Team
 * @version 1.3.2
 * @since 1.1.0
 */
@Service
public class AssegnazioneService {
    private final AssegnazioneRepository assegnazioneRepository;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param assegnazioneRepository Il repository per l'accesso ai dati delle assegnazioni.
     */
    public AssegnazioneService(AssegnazioneRepository assegnazioneRepository) {
        this.assegnazioneRepository = assegnazioneRepository;
    }

    /**
     * Salva una nuova assegnazione nel database.
     *
     * @param idTask    L'ID del Task.
     * @param idAzienda L'ID dell'Azienda.
     * @param idUtente  L'ID dell'Utente.
     */
    @Transactional
    public void save(int idTask, int idAzienda, int idUtente) {
        assegnazioneRepository.insertAssegnazione(idTask, idAzienda, idUtente);
    }

    /**
     * Elimina un'assegnazione specifica basata sugli ID forniti.
     *
     * @param idTask    L'ID del Task.
     * @param idAzienda L'ID dell'Azienda.
     * @param idUtente  L'ID dell'Utente.
     */
    @Transactional
    public void deleteByIds(int idTask, int idAzienda, int idUtente) {
        assegnazioneRepository.deleteByIds(idTask, idAzienda, idUtente);
    }

    /**
     * Elimina tutte le assegnazioni relative a un determinato Task e Azienda.
     *
     * @param idTask    L'ID del Task.
     * @param idAzienda L'ID dell'Azienda.
     */
    @Transactional
    public void deleteByTaskId(int idTask, int idAzienda) {
        assegnazioneRepository.deleteByTaskId(idTask, idAzienda);
    }

    /**
     * Recupera la lista dei Task assegnati a uno specifico Utente.
     *
     * @param utente L'entità {@link UtenteEntity} di cui recuperare i task assegnati.
     * @return Una lista di {@link TaskEntity} assegnati all'utente.
     */
    @Transactional
    public List<TaskEntity> getTaskAssegnate(UtenteEntity utente) {
        List<AssegnazioneEntity> assegnazioni = assegnazioneRepository.findByUtente(utente);
        return assegnazioni.stream().map(AssegnazioneEntity::getTask).toList();
    }

    /**
     * Elimina tutte le assegnazioni collegate a una specifica Azienda.
     *
     * @param idAzienda L'ID dell'Azienda da cui rimuovere le assegnazioni.
     */
    @Transactional
    public void deleteAllByAzienda(int idAzienda) {
        assegnazioneRepository.deleteByIdAzienda(idAzienda);
    }
}
