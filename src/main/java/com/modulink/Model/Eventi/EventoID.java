package com.modulink.Model.Eventi;

import java.io.Serializable;
import java.util.Objects;

public class EventoID implements Serializable {

    private int id_evento;
    private int id_azienda;

    public EventoID() {}

    public EventoID(int id_evento, int id_azienda) {
        this.id_evento = id_evento;
        this.id_azienda = id_azienda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventoID eventoID = (EventoID) o;
        return id_evento == eventoID.id_evento && id_azienda == eventoID.id_azienda;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_evento, id_azienda);
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
}