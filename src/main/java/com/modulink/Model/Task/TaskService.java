package com.modulink.Model.Task;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneService;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.modulink.Model.Task.TaskID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final AssegnazioneService assegnazioneService;

    public TaskService(TaskRepository taskRepository, AssegnazioneService assegnazioneService) {
        this.taskRepository = taskRepository;
        this.assegnazioneService = assegnazioneService;
    }

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

    @Transactional
    public void delete(int idTask, int idAzienda) {
        // Elimina prima le assegnazioni con query nativa SQL
        assegnazioneService.deleteByTaskId(idTask, idAzienda);

        // Poi elimina il task con query nativa SQL
        taskRepository.deleteByIdNative(idTask, idAzienda);
    }

    public List<TaskEntity> findByCreatore(UtenteEntity utente) {
        return taskRepository.findByUtenteCreatore(utente);
    }

    public TaskEntity findById(TaskID id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Transactional
    public void classicUpdate(TaskEntity taskEntity) {
        taskRepository.save(taskEntity);
    }

    @Transactional
    public void deleteAllByAzienda(AziendaEntity azienda) {
        taskRepository.deleteAllByAzienda(azienda);
        taskRepository.flush();
    }
}
