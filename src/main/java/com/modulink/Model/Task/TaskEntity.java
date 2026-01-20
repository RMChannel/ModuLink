package com.modulink.Model.Task;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "task", schema = "modulink")
@IdClass(TaskID.class)
public class TaskEntity {
    @Id
    @Column(name = "id_task")
    private int id_task;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", nullable = false, foreignKey = @ForeignKey(name = "fk_task_azienda"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AziendaEntity azienda;

    @Column(name = "ID_UtenteCreatore", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private int idUtenteCreatore;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "ID_UtenteCreatore", referencedColumnName = "ID_Utente", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    }, foreignKey = @ForeignKey(name = "fk_task_utente"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UtenteEntity utenteCreatore;

    @Column(name = "titolo", nullable = false)
    private String titolo;

    @Column(name = "priorita", nullable = false)
    private int priorita;

    @Column(name = "scadenza")
    private LocalDate scadenza;

    @Column(name = "data_creazione", nullable = false)
    private LocalDate dataCreazione;

    @Column(name = "data_completamento")
    private LocalDate dataCompletamento;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER)
    private List<AssegnazioneEntity> assegnazioni;

    public TaskEntity(){}

    public TaskEntity(AziendaEntity azienda, UtenteEntity utenteCreatore, String titolo, int priorita, LocalDate scadenza, LocalDate dataCreazione, LocalDate dataCompletamento) {
        this.azienda = azienda;
        this.utenteCreatore = utenteCreatore;
        if (utenteCreatore != null) {
            this.idUtenteCreatore = utenteCreatore.getId_utente();
        }
        this.titolo = titolo;
        this.priorita = priorita;
        this.scadenza = scadenza;
        this.dataCreazione = dataCreazione;
        this.dataCompletamento = dataCompletamento;
    }

    public int getId_task() {
        return id_task;
    }

    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    public AziendaEntity getAzienda() {
        return azienda;
    }

    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }

    public UtenteEntity getUtenteCreatore() {
        return utenteCreatore;
    }

    public void setUtenteCreatore(UtenteEntity utenteCreatore) {
        this.utenteCreatore = utenteCreatore;
        if (utenteCreatore != null) {
            this.idUtenteCreatore = utenteCreatore.getId_utente();
        }
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getPriorita() {
        return priorita;
    }

    public void setPriorita(int priorita) {
        this.priorita = priorita;
    }

    public LocalDate getScadenza() {
        return scadenza;
    }

    public void setScadenza(LocalDate scadenza) {
        this.scadenza = scadenza;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDate getDataCompletamento() {
        return dataCompletamento;
    }

    public boolean isCompletato() {
        return dataCompletamento != null;
    }

    public void setCompletato() {
        this.dataCompletamento = LocalDate.now();
    }

    public void setCompletato(LocalDate dataCompletamento) {
        this.dataCompletamento = dataCompletamento;
    }

    public List<AssegnazioneEntity> getAssegnazioni() {
        return assegnazioni;
    }

    public void setAssegnazioni(List<AssegnazioneEntity> assegnazioni) {
        this.assegnazioni = assegnazioni;
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id_task=" + id_task +
                ", azienda=" + azienda +
                ", utenteCreatore=" + utenteCreatore +
                ", titolo='" + titolo + '\'' +
                ", priorita=" + priorita +
                ", scadenza=" + scadenza +
                ", dataCreazione=" + dataCreazione +
                ", dataCompletamento=" + dataCompletamento +
                '}';
    }
}