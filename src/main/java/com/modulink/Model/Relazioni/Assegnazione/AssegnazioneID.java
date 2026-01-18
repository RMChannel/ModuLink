package com.modulink.Model.Relazioni.Assegnazione;

import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Utente.UtenteEntity;

import java.io.Serializable;
import java.util.Objects;

public class AssegnazioneID implements Serializable {
    private int id_task;
    private int id_azienda;
    private int id_utente;

    public AssegnazioneID() {}

    public AssegnazioneID(int id_task, int id_azienda, int id_utente) {
        this.id_task = id_task;
        this.id_azienda = id_azienda;
        this.id_utente = id_utente;
    }

    public int getId_task() {
        return id_task;
    }

    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    public int getId_azienda() {
        return id_azienda;
    }

    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    public int getId_utente() {
        return id_utente;
    }

    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AssegnazioneID that = (AssegnazioneID) o;
        return id_task == that.id_task && id_azienda == that.id_azienda && id_utente == that.id_utente;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_task, id_azienda, id_utente);
    }
}
