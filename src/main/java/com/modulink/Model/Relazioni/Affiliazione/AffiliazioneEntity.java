package com.modulink.Model.Relazioni.Affiliazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@IdClass(AffiliazioneID.class)
@Table(name = "affiliazione", schema="modulink")
public class AffiliazioneEntity {
    @Id
    @Column(name = "ID_Ruolo")
    public int id_ruolo;

    @Id
    @Column(name = "ID_Modulo")
    public int id_modulo;

    @Id
    @Column(name = "ID_Azienda")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public int id_azienda;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Ruolo", referencedColumnName = "ID_Ruolo", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    private RuoloEntity ruolo;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Modulo", referencedColumnName = "ID_Modulo", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    private AttivazioneEntity attivazione;

    public AffiliazioneEntity(){}

    public AffiliazioneEntity(int id_ruolo, int id_modulo, int id_azienda) {
        this.id_ruolo = id_ruolo;
        this.id_modulo = id_modulo;
        this.id_azienda = id_azienda;
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

    public RuoloEntity getRuolo() {
        return ruolo;
    }

    public void setRuolo(RuoloEntity ruolo) {
        this.ruolo = ruolo;
    }

    public AttivazioneEntity getAttivazione() {
        return attivazione;
    }

    public void setAttivazione(AttivazioneEntity attivazione) {
        this.attivazione = attivazione;
    }

    public AziendaEntity getAzienda() {
        return attivazione.getAzienda();
    }

    public ModuloEntity getModulo() {
        return attivazione.getModulo();
    }
}
