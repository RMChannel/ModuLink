package com.modulink.Model.Eventi;

import java.io.Serializable;
import java.util.Objects;

public class EventoID implements Serializable {

    private int id_evento;
    private int azienda;

    public EventoID() {}

    public EventoID(int id_evento, int azienda) {
        this.id_evento = id_evento;
        this.azienda = azienda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventoID eventoID = (EventoID) o;
        return id_evento == eventoID.id_evento && azienda == eventoID.azienda;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_evento, azienda);
    }

    public int getId_evento() {
        return id_evento;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    public int getAzienda() {
        return azienda;
    }

    public void setAzienda(int azienda) {
        this.azienda = azienda;
    }
}
