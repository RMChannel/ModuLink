package com.modulink.Model.Relazioni.Partecipazione;

import java.io.Serializable;
import java.util.Objects;

public class PartecipazioneID implements Serializable {
    private int id_utente;
    private int id_evento;
    private int id_azienda;

    public PartecipazioneID() {
    }

    public PartecipazioneID(int id_utente, int id_evento, int id_azienda) {
        this.id_utente = id_utente;
        this.id_evento = id_evento;
        this.id_azienda = id_azienda;
    }

    public int getId_utente() {
        return id_utente;
    }

    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    public int getId_evento() {
        return id_evento;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    public int getId_azienda() {
        return id_azienda;
    }

    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartecipazioneID that = (PartecipazioneID) o;
        return id_utente == that.id_utente && id_evento == that.id_evento && id_azienda == that.id_azienda;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_utente, id_evento, id_azienda);
    }

    @Override
    public String toString() {
        return "PartecipazioneID{" +
                "id_utente=" + id_utente +
                ", id_evento=" + id_evento +
                ", id_azienda=" + id_azienda +
                '}';
    }
}