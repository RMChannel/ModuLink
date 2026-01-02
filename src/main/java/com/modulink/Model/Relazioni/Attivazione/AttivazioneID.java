package com.modulink.Model.Relazioni.Attivazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;

import java.util.Objects;

public class AttivazioneID {
    private ModuloEntity modulo;
    private AziendaEntity azienda;

    public AttivazioneID() {}

    public AttivazioneID(ModuloEntity modulo, AziendaEntity azienda) {
        this.modulo = modulo;
        this.azienda = azienda;
    }

    public ModuloEntity getModulo() {
        return modulo;
    }

    public void setModulo(ModuloEntity modulo) {
        this.modulo = modulo;
    }

    public AziendaEntity getAzienda() {
        return azienda;
    }

    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AttivazioneID that = (AttivazioneID) o;
        return Objects.equals(modulo, that.modulo) && Objects.equals(azienda, that.azienda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modulo, azienda);
    }
}
