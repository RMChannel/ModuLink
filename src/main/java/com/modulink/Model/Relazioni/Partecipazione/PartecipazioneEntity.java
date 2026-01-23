package com.modulink.Model.Relazioni.Partecipazione;

import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entità di persistenza <strong>Partecipazione</strong>, che rappresenta l'adesione di un utente a un evento aziendale.
 * <p>
 * Implementa una relazione molti-a-molti tra {@link UtenteEntity} ed {@link EventoEntity}.
 * La partecipazione è sempre contestualizzata all'interno di una specifica azienda per garantire
 * la segregazione dei dati in ambiente multi-tenant.
 * </p>
 * <p>
 * La chiave primaria è composta dalla tripla (Utente, Evento, Azienda), definita in {@link PartecipazioneID}.
 * </p>
 *
 * @see PartecipazioneID
 * @see UtenteEntity
 * @see EventoEntity
 * @author Modulink Team
 * @version 1.5.0
 * @since 1.2.0
 */
@Entity
@Table(name = "partecipazione", schema = "modulink")
@IdClass(PartecipazioneID.class)
public class PartecipazioneEntity {

    /**
     * Identificativo dell'Utente partecipante.
     * Parte della chiave primaria composta.
     *
     * @since 1.2.0
     */
    @Id
    @Column(name = "ID_Utente")
    private int id_utente;

    /**
     * Identificativo dell'Evento a cui si partecipa.
     * Parte della chiave primaria composta.
     *
     * @since 1.2.0
     */
    @Id
    @Column(name = "id_evento")
    private int id_evento;

    /**
     * Identificativo dell'Azienda organizzatrice.
     * Garantisce che l'evento e l'utente appartengano allo stesso contesto.
     * Parte della chiave primaria composta.
     *
     * @since 1.2.0
     */
    @Id
    @Column(name = "ID_Azienda")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private int id_azienda;

    /**
     * Riferimento all'entità Utente.
     * Mappato in sola lettura per la navigazione JPA.
     *
     * @since 1.2.0
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumns({
            @JoinColumn(name = "ID_Utente", referencedColumnName = "ID_Utente", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    private UtenteEntity utente;

    /**
     * Riferimento all'entità Evento.
     * Mappato in sola lettura per la navigazione JPA.
     *
     * @since 1.2.0
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumns({
            @JoinColumn(name = "id_evento", referencedColumnName = "id_evento", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "id_azienda", insertable = false, updatable = false)
    })
    private EventoEntity evento;

    /**
     * Costruttore vuoto richiesto da JPA.
     *
     * @since 1.2.0
     */
    public PartecipazioneEntity() {
    }

    /**
     * Costruttore completo per creare una nuova partecipazione.
     *
     * @param id_utente  ID dell'utente.
     * @param id_evento  ID dell'evento.
     * @param id_azienda ID dell'azienda.
     * @since 1.2.0
     */
    public PartecipazioneEntity(int id_utente, int id_evento, int id_azienda) {
        this.id_utente = id_utente;
        this.id_evento = id_evento;
        this.id_azienda = id_azienda;
    }

    /**
     * Restituisce l'ID dell'utente.
     *
     * @return ID utente.
     * @since 1.2.0
     */
    public int getId_utente() {
        return id_utente;
    }

    /**
     * Imposta l'ID dell'utente.
     *
     * @param id_utente Nuovo ID utente.
     * @since 1.2.0
     */
    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    /**
     * Restituisce l'ID dell'evento.
     *
     * @return ID evento.
     * @since 1.2.0
     */
    public int getId_evento() {
        return id_evento;
    }

    /**
     * Imposta l'ID dell'evento.
     *
     * @param id_evento Nuovo ID evento.
     * @since 1.2.0
     */
    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    /**
     * Restituisce l'ID dell'azienda.
     *
     * @return ID azienda.
     * @since 1.2.0
     */
    public int getId_azienda() {
        return id_azienda;
    }

    /**
     * Imposta l'ID dell'azienda.
     *
     * @param id_azienda Nuovo ID azienda.
     * @since 1.2.0
     */
    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    /**
     * Restituisce l'oggetto Utente associato.
     *
     * @return Entità Utente.
     * @since 1.2.0
     */
    public UtenteEntity getUtente() {
        return utente;
    }

    /**
     * Associa l'oggetto Utente.
     *
     * @param utente Nuova entità Utente.
     * @since 1.2.0
     */
    public void setUtente(UtenteEntity utente) {
        this.utente = utente;
    }

    /**
     * Restituisce l'oggetto Evento associato.
     *
     * @return Entità Evento.
     * @since 1.2.0
     */
    public EventoEntity getEvento() {
        return evento;
    }

    /**
     * Associa l'oggetto Evento.
     *
     * @param evento Nuova entità Evento.
     * @since 1.2.0
     */
    public void setEvento(EventoEntity evento) {
        this.evento = evento;
    }
}
