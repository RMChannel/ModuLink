package com.modulink.Model.Relazioni.Assegnazione;

import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@IdClass(AssegnazioneID.class)
@Table(name = "assegnazione", schema = "modulink")
public class AssegnazioneEntity {
    @Id
    @Column(name = "ID_Task")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private int id_task;

    @Id
    @Column(name = "ID_Azienda")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private int id_azienda;

    @Id
    @Column(name = "ID_Utente")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private int id_utente;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Task", referencedColumnName = "ID_Task", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TaskEntity task;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Utente", referencedColumnName = "ID_Utente", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UtenteEntity utente;

    public AssegnazioneEntity() {}

    public AssegnazioneEntity(TaskEntity task, UtenteEntity utente) {
        this.task = task;
        this.utente = utente;
        this.id_task = task.getId_task();
        this.id_azienda = task.getAzienda().getId_azienda();
        this.id_utente = utente.getId_utente();
    }

    public UtenteEntity getUtente() {
        return utente;
    }

    public void setUtente(UtenteEntity utente) {
        this.utente = utente;
        this.id_utente = utente.getId_utente();
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
        this.id_task = task.getId_task();
        this.id_azienda = task.getAzienda().getId_azienda();
    }

    public int getId_task() {
        return id_task;
    }

    public int getId_azienda() {
        return id_azienda;
    }

    public int getId_utente() {
        return id_utente;
    }

    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssegnazioneEntity that = (AssegnazioneEntity) o;
        return id_task == that.id_task && id_azienda == that.id_azienda && id_utente == that.id_utente;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id_task, id_azienda, id_utente);
    }
}
