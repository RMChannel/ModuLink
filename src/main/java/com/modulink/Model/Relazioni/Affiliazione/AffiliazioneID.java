package com.modulink.Model.Relazioni.Affiliazione;

import java.io.Serializable;
import java.util.Objects;

public class AffiliazioneID implements Serializable {
    private int id_ruolo;
    private int id_modulo;
    private int id_azienda;

    public AffiliazioneID(){}

    public AffiliazioneID(int id_ruolo, int id_modulo, int id_azienda) {
        this.id_ruolo = id_ruolo;
        this.id_modulo = id_modulo;
        this.id_azienda = id_azienda;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AffiliazioneID that = (AffiliazioneID) o;
        return id_ruolo == that.id_ruolo && id_modulo == that.id_modulo && id_azienda == that.id_azienda;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_ruolo, id_modulo, id_azienda);
    }

    public int getId_ruolo() {
        return id_ruolo;
    }

    public void setId_ruolo(int id_ruolo) {
        this.id_ruolo = id_ruolo;
    }

    public int getId_modulo() {
        return id_modulo;
    }

    public void setId_modulo(int id_modulo) {
        this.id_modulo = id_modulo;
    }

    public int getId_azienda() {
        return id_azienda;
    }

    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }
}
