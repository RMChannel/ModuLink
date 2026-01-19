package com.modulink.Model.Eventi;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="evento", schema="modulink")
@IdClass(EventoID.class)
public class EventoEntity {

    @Id
    @Column(name="id_evento", nullable = false)
    private int id_evento;


    @Id
    @ManyToOne
    @JoinColumn(name="id_azienda", referencedColumnName = "id_azienda", nullable = false, foreignKey = @ForeignKey(name = "FK_Evento_Azienda"))
    private AziendaEntity azienda;

    @Column(name="nome", nullable = false, length = 200)
    private String nome;


    @Column(name="luogo", length = 300)
    private String luogo;

    @ManyToOne
    private UtenteEntity creatore;

    @Column(name="data_ora_inizio", nullable = false)
    private LocalDateTime data_ora_inizio;

    @Column(name="data_fine")
    private LocalDateTime data_fine;

    public EventoEntity() {}

    public EventoEntity(int id_evento, AziendaEntity azienda, String nome, String luogo, LocalDateTime data_ora_inizio, LocalDateTime data_fine) {
        this.id_evento = id_evento;
        this.azienda = azienda;
        this.nome = nome;
        this.luogo = luogo;
        this.data_ora_inizio = data_ora_inizio;
        this.data_fine = data_fine;
    }

    public int getId_evento() {
        return id_evento;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    public AziendaEntity getAzienda() {
        return azienda;
    }

    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public LocalDateTime getData_ora_inizio() {
        return data_ora_inizio;
    }

    public void setData_ora_inizio(LocalDateTime data_ora_inizio) {
        this.data_ora_inizio = data_ora_inizio;
    }

    public LocalDateTime getData_fine() {
        return data_fine;
    }

    public void setData_fine(LocalDateTime data_fine) {
        this.data_fine = data_fine;
    }

    public UtenteEntity getCreatore() {
        return creatore;
    }

    public void setCreatore(UtenteEntity creatore) {
        this.creatore = creatore;
    }
}