package com.modulink.Model.Relazioni.Partecipazione;

import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "partecipazione", schema = "modulink")
@IdClass(PartecipazioneID.class)
public class PartecipazioneEntity {

    @Id
    @Column(name = "ID_Utente")
    private int id_utente;

    @Id
    @Column(name = "id_evento")
    private int id_evento;

    @Id
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "ID_Azienda")
    private int id_azienda;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Utente", referencedColumnName = "ID_Utente", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UtenteEntity utente;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "id_evento", referencedColumnName = "id_evento", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "id_azienda", insertable = false, updatable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private EventoEntity evento;

    public PartecipazioneEntity() {
    }

    public PartecipazioneEntity(int id_utente, int id_evento, int id_azienda) {
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

    public UtenteEntity getUtente() {
        return utente;
    }

    public void setUtente(UtenteEntity utente) {
        this.utente = utente;
    }

    public EventoEntity getEvento() {
        return evento;
    }

    public void setEvento(EventoEntity evento) {
        this.evento = evento;
    }
}
