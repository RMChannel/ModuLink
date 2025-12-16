package com.modulink.Model.Modulo;

import java.util.Objects;

public class ModuloID {
    private int id_modulo;
    private int id_azienda;

    public ModuloID(){}

    public ModuloID(int id_modulo, int id_azienda) {
        this.id_modulo = id_modulo;
        this.id_azienda = id_azienda;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ModuloID moduloID = (ModuloID) o;
        return id_modulo == moduloID.id_modulo && id_azienda == moduloID.id_azienda;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_modulo, id_azienda);
    }
}
