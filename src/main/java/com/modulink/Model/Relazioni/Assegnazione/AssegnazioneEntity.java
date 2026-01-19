package com.modulink.Model.Relazioni.Assegnazione;

import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Rappresenta l'entità di relazione che gestisce l'assegnazione di un Task a un Utente all'interno di una specifica Azienda.
 * <p>
 * Questa classe mappa la tabella "assegnazione" nel database e utilizza una chiave primaria composta definita nella classe {@link AssegnazioneID}.
 * La relazione permette di collegare un {@link TaskEntity} e un {@link UtenteEntity}, garantendo l'integrità referenziale
 * attraverso vincoli di chiave esterna e azioni di cancellazione a cascata.
 * </p>
 *
 * @author Modulink Team
 * @version 1.2.4
 * @since 1.0.0
 */
@Entity
@IdClass(AssegnazioneID.class)
@Table(name = "assegnazione", schema = "modulink")
public class AssegnazioneEntity {
    /**
     * Identificativo univoco del Task assegnato.
     * Parte della chiave primaria composta.
     */
    @Id
    @Column(name = "ID_Task")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private int id_task;

    /**
     * Identificativo univoco dell'Azienda a cui appartengono Task e Utente.
     * Parte della chiave primaria composta.
     */
    @Id
    @Column(name = "ID_Azienda")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private int id_azienda;

    /**
     * Identificativo univoco dell'Utente a cui è assegnato il Task.
     * Parte della chiave primaria composta.
     */
    @Id
    @Column(name = "ID_Utente")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private int id_utente;

    /**
     * Riferimento all'entità {@link TaskEntity} associata a questa assegnazione.
     * La relazione è di tipo Many-to-One.
     */
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Task", referencedColumnName = "ID_Task", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TaskEntity task;

    /**
     * Riferimento all'entità {@link UtenteEntity} associata a questa assegnazione.
     * La relazione è di tipo Many-to-One.
     */
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Utente", referencedColumnName = "ID_Utente", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UtenteEntity utente;

    /**
     * Costruttore vuoto richiesto da JPA.
     */
    public AssegnazioneEntity() {}

    /**
     * Costruttore parametrico per creare una nuova istanza di assegnazione.
     * Inizializza gli identificativi basandosi sulle entità Task e Utente fornite.
     *
     * @param task   L'entità {@link TaskEntity} da assegnare.
     * @param utente L'entità {@link UtenteEntity} destinataria del task.
     */
    public AssegnazioneEntity(TaskEntity task, UtenteEntity utente) {
        this.task = task;
        this.utente = utente;
        this.id_task = task.getId_task();
        this.id_azienda = task.getAzienda().getId_azienda();
        this.id_utente = utente.getId_utente();
    }

    /**
     * Restituisce l'utente associato a questa assegnazione.
     *
     * @return L'oggetto {@link UtenteEntity}.
     */
    public UtenteEntity getUtente() {
        return utente;
    }

    /**
     * Imposta l'utente per questa assegnazione e aggiorna l'ID utente corrispondente.
     *
     * @param utente Il nuovo oggetto {@link UtenteEntity}.
     */
    public void setUtente(UtenteEntity utente) {
        this.utente = utente;
        this.id_utente = utente.getId_utente();
    }

    /**
     * Restituisce il task associato a questa assegnazione.
     *
     * @return L'oggetto {@link TaskEntity}.
     */
    public TaskEntity getTask() {
        return task;
    }

    /**
     * Imposta il task per questa assegnazione e aggiorna gli ID task e azienda corrispondenti.
     *
     * @param task Il nuovo oggetto {@link TaskEntity}.
     */
    public void setTask(TaskEntity task) {
        this.task = task;
        this.id_task = task.getId_task();
        this.id_azienda = task.getAzienda().getId_azienda();
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
     * Restituisce l'ID dell'azienda.
     *
     * @return L'identificativo dell'azienda.
     */
    public int getId_azienda() {
        return id_azienda;
    }

    /**
     * Restituisce l'ID dell'utente.
     *
     * @return L'identificativo dell'utente.
     */
    public int getId_utente() {
        return id_utente;
    }

    /**
     * Imposta manualmente l'ID del task.
     *
     * @param id_task Il nuovo identificativo del task.
     */
    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    /**
     * Imposta manualmente l'ID dell'azienda.
     *
     * @param id_azienda Il nuovo identificativo dell'azienda.
     */
    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    /**
     * Imposta manualmente l'ID dell'utente.
     *
     * @param id_utente Il nuovo identificativo dell'utente.
     */
    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    /**
     * Verifica l'uguaglianza tra questo oggetto e un altro oggetto.
     * L'uguaglianza è basata sulla chiave primaria composta (ID Task, ID Azienda, ID Utente).
     *
     * @param o L'oggetto da confrontare.
     * @return true se gli oggetti sono uguali, false altrimenti.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssegnazioneEntity that = (AssegnazioneEntity) o;
        return id_task == that.id_task && id_azienda == that.id_azienda && id_utente == that.id_utente;
    }

    /**
     * Calcola l'hash code dell'oggetto basandosi sulla chiave primaria composta.
     *
     * @return Il valore hash code.
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id_task, id_azienda, id_utente);
    }
}
