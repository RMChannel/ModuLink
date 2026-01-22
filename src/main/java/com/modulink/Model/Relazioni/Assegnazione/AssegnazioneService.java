package com.modulink.Model.Relazioni.Assegnazione;

import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssegnazioneService {
    private final AssegnazioneRepository assegnazioneRepository;

    public AssegnazioneService(AssegnazioneRepository assegnazioneRepository) {
        this.assegnazioneRepository = assegnazioneRepository;
    }

    @Transactional
    public void save(int idTask, int idAzienda, int idUtente) {
        assegnazioneRepository.insertAssegnazione(idTask, idAzienda, idUtente);
    }

    @Transactional
    public void deleteByIds(int idTask, int idAzienda, int idUtente) {
        assegnazioneRepository.deleteByIds(idTask, idAzienda, idUtente);
    }

    @Transactional
    public void deleteByTaskId(int idTask, int idAzienda) {
        assegnazioneRepository.deleteByTaskId(idTask, idAzienda);
    }

    @Transactional
    public List<TaskEntity> getTaskAssegnate(UtenteEntity utente) {
        List<AssegnazioneEntity> assegnazioni = assegnazioneRepository.findByUtente(utente);
        return assegnazioni.stream().map(AssegnazioneEntity::getTask).toList();
    }

    @Transactional
    public void deleteAllByAzienda(int idAzienda) {
        assegnazioneRepository.deleteByIdAzienda(idAzienda);
    }
}
