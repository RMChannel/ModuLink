package com.modulink.Model.Task;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.modulink.Model.Task.TaskID;

/**
 * Service per la gestione della logica di business relativa ai Task.
 * <p>
 * Questa classe fornisce metodi per creare, aggiornare, eliminare e recuperare task,
 * gestendo anche le relazioni complesse come le assegnazioni agli utenti e la generazione
 * degli identificativi incrementali per azienda. Interagisce con {@link TaskRepository}
 * e {@link AssegnazioneService}.
 * </p>
 *
 * @author Modulink Team
 * @version 1.5.7
 */
@Service
public class
TaskService {
    private final TaskRepository taskRepository;
    private final AssegnazioneService assegnazioneService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param taskRepository      Il repository per le operazioni sui dati dei task.
     * @param assegnazioneService Il service per gestire le assegnazioni dei task agli utenti.
     */
    public TaskService(TaskRepository taskRepository, AssegnazioneService assegnazioneService) {
        this.taskRepository = taskRepository;
        this.assegnazioneService = assegnazioneService;
    }

    /**
     * Salva un nuovo task nel database.
     * <p>
     * Questo metodo calcola automaticamente il prossimo ID task disponibile per l'azienda
     * specificata (logica di auto-incremento per azienda). Inoltre, gestisce il salvataggio
     * separato delle assegnazioni associate al task.
     * </p>
     *
     * @param taskEntity L'entità task da salvare.
     */
    @Transactional
    public void save(TaskEntity taskEntity) {
        TaskEntity tmax=taskRepository.findMaxByAzienda(taskEntity.getAzienda());
        if(tmax==null) taskEntity.setId_task(0);
        else taskEntity.setId_task(tmax.getId_task()+1);

        // Salva le assegnazioni temporaneamente
        List<com.modulink.Model.Relazioni.Assegnazione.AssegnazioneEntity> assegnazioniTemp = taskEntity.getAssegnazioni();
        taskEntity.setAssegnazioni(null);

        // Salva il task
        taskRepository.save(taskEntity);

        // Ora salva le assegnazioni manualmente usando solo gli ID
        if (assegnazioniTemp != null && !assegnazioniTemp.isEmpty()) {
            for (com.modulink.Model.Relazioni.Assegnazione.AssegnazioneEntity ass : assegnazioniTemp) {
                assegnazioneService.save(
                    taskEntity.getId_task(),
                    taskEntity.getAzienda().getId_azienda(),
                    ass.getUtente().getId_utente()
                );
            }
        }
    }

    /**
     * Aggiorna un task esistente e le relative assegnazioni.
     * <p>
     * Questo metodo aggiorna i dettagli del task (titolo, priorità, scadenze, ecc.)
     * ed elimina tutte le assegnazioni precedenti per sostituirle con quelle nuove fornite.
     * </p>
     *
     * @param taskId         L'ID del task da aggiornare.
     * @param updateData     Oggetto contenente i nuovi dati del task.
     * @param newAssignments La nuova lista di assegnazioni per il task.
     */
    @Transactional
    public void update(int taskId, TaskEntity updateData, List<com.modulink.Model.Relazioni.Assegnazione.AssegnazioneEntity> newAssignments) {
        TaskEntity managedTask = taskRepository.findById(new TaskID(taskId, updateData.getAzienda().getId_azienda())).orElse(null);
        if (managedTask != null) {
            managedTask.setTitolo(updateData.getTitolo());
            managedTask.setPriorita(updateData.getPriorita());
            managedTask.setScadenza(updateData.getScadenza());
            managedTask.setCompletato(updateData.getDataCompletamento());

            // Elimina tutte le assegnazioni esistenti
            assegnazioneService.deleteByTaskId(managedTask.getId_task(), managedTask.getAzienda().getId_azienda());

            // Salva il task aggiornato
            taskRepository.save(managedTask);
            taskRepository.flush();

            // Crea le nuove assegnazioni usando solo gli ID
            for (com.modulink.Model.Relazioni.Assegnazione.AssegnazioneEntity newOne : newAssignments) {
                assegnazioneService.save(
                    managedTask.getId_task(),
                    managedTask.getAzienda().getId_azienda(),
                    newOne.getUtente().getId_utente()
                );
            }
        }
    }

    /**
     * Elimina un task e tutte le sue assegnazioni correlate.
     * <p>
     * L'eliminazione avviene prima sulle assegnazioni e poi sul task stesso utilizzando
     * query native per garantire la pulizia completa dei dati.
     * </p>
     *
     * @param idTask    L'ID del task da eliminare.
     * @param idAzienda L'ID dell'azienda associata al task.
     */
    @Transactional
    public void delete(int idTask, int idAzienda) {
        // Elimina prima le assegnazioni con query nativa SQL
        assegnazioneService.deleteByTaskId(idTask, idAzienda);

        // Poi elimina il task con query nativa SQL
        taskRepository.deleteByIdNative(idTask, idAzienda);
    }

    /**
     * Trova tutti i task creati da un determinato utente.
     *
     * @param utente L'utente creatore dei task.
     * @return Una lista di task creati dall'utente.
     */
    public List<TaskEntity> findByCreatore(UtenteEntity utente) {
        return taskRepository.findByUtenteCreatore(utente);
    }

    /**
     * Cerca un task tramite la sua chiave primaria composta.
     *
     * @param id La chiave primaria composta {@link TaskID}.
     * @return Il task trovato o null se non esiste.
     */
    public TaskEntity findById(TaskID id) {
        return taskRepository.findById(id).orElse(null);
    }

    /**
     * Esegue un aggiornamento standard dell'entità task.
     * <p>
     * A differenza del metodo {@code update}, questo metodo utilizza il salvataggio
     * standard di JPA senza gestire manualmente le assegnazioni.
     * </p>
     *
     * @param taskEntity L'entità task da aggiornare.
     */
    @Transactional
    public void classicUpdate(TaskEntity taskEntity) {
        taskRepository.save(taskEntity);
    }

    /**
     * Elimina tutti i task e le relative assegnazioni per un'intera azienda.
     * <p>
     * Utilizza query native per prestazioni ottimali ed evitare il caricamento in memoria
     * di grandi quantità di dati.
     * </p>
     *
     * @param azienda L'azienda di cui cancellare tutti i task.
     */
    @Transactional
    public void deleteAllByAzienda(AziendaEntity azienda) {
        // Elimina tutte le assegnazioni dell'azienda
        assegnazioneService.deleteAllByAzienda(azienda.getId_azienda());
        
        // Elimina tutti i task dell'azienda usando query nativa per evitare caricamento in memoria
        taskRepository.deleteAllByAziendaNative(azienda.getId_azienda());
    }
}
