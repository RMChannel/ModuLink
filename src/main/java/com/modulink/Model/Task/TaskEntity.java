package com.modulink.Model.Task;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

/**
 * Rappresenta l'entità di un Task all'interno del sistema ModuLink.
 * <p>
 * Questa classe mappa la tabella "task" nel database e gestisce le informazioni
 * relative ai compiti assegnati all'interno di un'azienda, includendo dettagli
 * come priorità, scadenze, stato di completamento e l'utente creatore.
 * Utilizza una chiave primaria composta definita nella classe {@link TaskID}.
 * </p>
 *
 * @author Modulink Team
 * @version 1.3.4
 */
@Entity
@Table(name = "task", schema = "modulink")
@IdClass(TaskID.class)
public class TaskEntity {

    /**
     * Identificativo univoco del task all'interno dell'azienda.
     * Parte della chiave primaria composta.
     */
    @Id
    @Column(name = "id_task")
    private int id_task;

    /**
     * L'azienda a cui appartiene il task.
     * Parte della chiave primaria composta e chiave esterna verso la tabella Azienda.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", nullable = false, foreignKey = @ForeignKey(name = "fk_task_azienda"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AziendaEntity azienda;

    /**
     * ID dell'utente che ha creato il task.
     * Utilizzato per la mappatura della colonna nel database.
     */
    @Column(name = "ID_UtenteCreatore", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private int idUtenteCreatore;

    /**
     * Riferimento all'entità {@link UtenteEntity} che ha creato il task.
     * Mappato tramite una relazione Many-to-One composta.
     */
    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "ID_UtenteCreatore", referencedColumnName = "ID_Utente", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    }, foreignKey = @ForeignKey(name = "fk_task_utente"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UtenteEntity utenteCreatore;

    /**
     * Il titolo o la descrizione breve del task.
     */
    @Column(name = "titolo", nullable = false)
    private String titolo;

    /**
     * Il livello di priorità del task.
     * Valori più alti indicano solitamente una priorità maggiore.
     */
    @Column(name = "priorita", nullable = false)
    private int priorita;

    /**
     * La data di scadenza prevista per il completamento del task.
     */
    @Column(name = "scadenza")
    private LocalDate scadenza;

    /**
     * La data in cui il task è stato creato.
     */
    @Column(name = "data_creazione", nullable = false)
    private LocalDate dataCreazione;

    /**
     * La data in cui il task è stato effettivamente completato.
     * Se null, il task è considerato ancora aperto.
     */
    @Column(name = "data_completamento")
    private LocalDate dataCompletamento;

    /**
     * Lista delle assegnazioni associate a questo task.
     * Rappresenta la relazione One-to-Many verso {@link AssegnazioneEntity}.
     */
    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER)
    private List<AssegnazioneEntity> assegnazioni;

    /**
     * Costruttore vuoto richiesto da JPA.
     */
    public TaskEntity(){}

    /**
     * Costruisce un nuovo TaskEntity con i dettagli specificati.
     *
     * @param azienda           L'azienda di appartenenza del task.
     * @param utenteCreatore    L'utente che crea il task.
     * @param titolo            Il titolo del task.
     * @param priorita          Il livello di priorità del task.
     * @param scadenza          La data di scadenza del task.
     * @param dataCreazione     La data di creazione del task.
     * @param dataCompletamento La data di completamento (può essere null).
     */
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

    /**
     * Restituisce l'ID del task.
     *
     * @return L'identificativo del task.
     */
    public int getId_task() {
        return id_task;
    }

    /**
     * Imposta l'ID del task.
     *
     * @param id_task Il nuovo identificativo del task.
     */
    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    /**
     * Restituisce l'azienda associata al task.
     *
     * @return L'entità {@link AziendaEntity} associata.
     */
    public AziendaEntity getAzienda() {
        return azienda;
    }

    /**
     * Imposta l'azienda associata al task.
     *
     * @param azienda La nuova azienda di appartenenza.
     */
    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }

    /**
     * Restituisce l'utente creatore del task.
     *
     * @return L'entità {@link UtenteEntity} che ha creato il task.
     */
    public UtenteEntity getUtenteCreatore() {
        return utenteCreatore;
    }

    /**
     * Imposta l'utente creatore del task e aggiorna l'ID utente creatore corrispondente.
     *
     * @param utenteCreatore Il nuovo utente creatore.
     */
    public void setUtenteCreatore(UtenteEntity utenteCreatore) {
        this.utenteCreatore = utenteCreatore;
        if (utenteCreatore != null) {
            this.idUtenteCreatore = utenteCreatore.getId_utente();
        }
    }

    /**
     * Restituisce il titolo del task.
     *
     * @return Il titolo del task.
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta il titolo del task.
     *
     * @param titolo Il nuovo titolo del task.
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Restituisce la priorità del task.
     *
     * @return Un intero rappresentante la priorità.
     */
    public int getPriorita() {
        return priorita;
    }

    /**
     * Imposta la priorità del task.
     *
     * @param priorita Il nuovo livello di priorità.
     */
    public void setPriorita(int priorita) {
        this.priorita = priorita;
    }

    /**
     * Restituisce la data di scadenza del task.
     *
     * @return La data di scadenza.
     */
    public LocalDate getScadenza() {
        return scadenza;
    }

    /**
     * Imposta la data di scadenza del task.
     *
     * @param scadenza La nuova data di scadenza.
     */
    public void setScadenza(LocalDate scadenza) {
        this.scadenza = scadenza;
    }

    /**
     * Restituisce la data di creazione del task.
     *
     * @return La data di creazione.
     */
    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    /**
     * Imposta la data di creazione del task.
     *
     * @param dataCreazione La nuova data di creazione.
     */
    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    /**
     * Restituisce la data di completamento del task.
     *
     * @return La data di completamento, o null se non completato.
     */
    public LocalDate getDataCompletamento() {
        return dataCompletamento;
    }

    /**
     * Verifica se il task è stato completato.
     *
     * @return true se il task ha una data di completamento, false altrimenti.
     */
    public boolean isCompletato() {
        return dataCompletamento != null;
    }

    /**
     * Segna il task come completato impostando la data di completamento alla data odierna.
     */
    public void setCompletato() {
        this.dataCompletamento = LocalDate.now();
    }

    /**
     * Imposta una specifica data di completamento per il task.
     *
     * @param dataCompletamento La data in cui il task è stato completato.
     */
    public void setCompletato(LocalDate dataCompletamento) {
        this.dataCompletamento = dataCompletamento;
    }

    /**
     * Restituisce la lista delle assegnazioni per questo task.
     *
     * @return Una lista di oggetti {@link AssegnazioneEntity}.
     */
    public List<AssegnazioneEntity> getAssegnazioni() {
        return assegnazioni;
    }

    /**
     * Imposta la lista delle assegnazioni per questo task.
     *
     * @param assegnazioni La nuova lista di assegnazioni.
     */
    public void setAssegnazioni(List<AssegnazioneEntity> assegnazioni) {
        this.assegnazioni = assegnazioni;
    }

    /**
     * Restituisce una rappresentazione in formato stringa dell'oggetto TaskEntity.
     *
     * @return Una stringa contenente i dettagli principali del task.
     */
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