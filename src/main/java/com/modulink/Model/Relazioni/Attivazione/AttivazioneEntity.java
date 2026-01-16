package com.modulink.Model.Relazioni.Attivazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@IdClass(AttivazioneID.class)
@Table(name = "attivazione", schema = "modulink")
public class AttivazioneEntity {
    @Id
    @ManyToOne
    @JoinColumn(
            name = "id_modulo",
            foreignKey = @ForeignKey(name = "fk_modulo_azienda")
    )
    private ModuloEntity modulo;

    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(
            name = "id_azienda",
            foreignKey = @ForeignKey(name = "fk_azienda_modulo")
    )
    private AziendaEntity azienda;

    public AttivazioneEntity() {}

    public AttivazioneEntity(ModuloEntity modulo, AziendaEntity azienda) {
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
}
