package com.modulink.Model.Eventi;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/**
 * Rappresenta un evento schedulato all'interno del calendario aziendale nel sistema Modulink.
 * <p>
 * Questa entità utilizza una chiave primaria composta definita nella classe {@link EventoID},
 * costituita dalla coppia ({@code id_evento}, {@code azienda}). Ciò consente la partizione
 * logica degli eventi per tenant (Azienda), permettendo la duplicazione degli ID sequenziali
 * locali in contesti aziendali differenti.
 * </p>
 * <p>
 * La relazione con {@link AziendaEntity} è di tipo Many-To-One ed è vincolata da una Foreign Key
 * con policy di cancellazione a cascata ({@code ON DELETE CASCADE}), garantendo che l'eliminazione
 * di un'azienda comporti la rimozione di tutti i suoi eventi associati.
 * </p>
 *
 * @see EventoID
 * @author Modulink Team
 * @version 2.0.3
 * @since 1.2.0
 */
@Entity
@Table(name="evento", schema="modulink")
@IdClass(EventoID.class)
public class EventoEntity {

    /**
     * Identificativo sequenziale locale dell'evento.
     * <p>
     * Parte della chiave primaria composta. Non è univoco globalmente, ma lo è all'interno
     * dello scope della singola {@link #azienda}.
     * </p>
     *
     * @since 1.2.0
     */
    @Id
    @Column(name="id_evento", nullable = false)
    private int id_evento;

    /**
     * Riferimento all'azienda proprietaria dell'evento.
     * <p>
     * Parte della chiave primaria composta. Implementa la segregazione dei dati multi-tenant.
     * </p>
     *
     * @since 1.2.0
     */
    @Id
    @ManyToOne
    @JoinColumn(name="id_azienda", referencedColumnName = "id_azienda", nullable = false, foreignKey = @ForeignKey(name = "FK_Evento_Azienda"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AziendaEntity azienda;

    /**
     * Titolo o nome descrittivo dell'evento.
     *
     * @since 1.2.0
     */
    @Column(name="nome", nullable = false, length = 200)
    private String nome;

    /**
     * Luogo fisico o virtuale in cui si svolge l'evento.
     *
     * @since 1.3.0
     */
    @Column(name="luogo", length = 200)
    private String luogo;

    /**
     * Utente che ha creato l'evento.
     * <p>
     * Relazione opzionale utile per audit trail e gestione permessi.
     * </p>
     *
     * @since 1.4.1
     */
    @ManyToOne
    private UtenteEntity creatore;

    /**
     * Timestamp di inizio evento.
     *
     * @since 1.2.0
     */
    @Column(name="data_ora_inizio", nullable = false)
    private LocalDateTime data_ora_inizio;

    /**
     * Timestamp di fine evento (opzionale).
     *
     * @since 1.2.0
     */
    @Column(name="data_fine")
    private LocalDateTime data_fine;

    /**
     * Costruttore di default richiesto dalla specifica JPA.
     *
     * @since 1.2.0
     */
    public EventoEntity() {}

    /**
     * Costruttore parametrico completo.
     * <p>
     * Nota: L'ID locale {@code id_evento} non è incluso in questo costruttore poiché
     * viene tipicamente calcolato e assegnato dal Service layer al momento della creazione.
     * </p>
     *
     * @param azienda         Azienda proprietaria.
     * @param nome            Nome dell'evento.
     * @param luogo           Luogo dell'evento.
     * @param data_ora_inizio Data e ora di inizio.
     * @param data_fine       Data e ora di fine.
     * @since 1.2.0
     */
    public EventoEntity(AziendaEntity azienda, String nome, String luogo, LocalDateTime data_ora_inizio, LocalDateTime data_fine) {
        this.azienda = azienda;
        this.nome = nome;
        this.luogo = luogo;
        this.data_ora_inizio = data_ora_inizio;
        this.data_fine = data_fine;
    }

    /**
     * Recupera l'ID locale dell'evento.
     * @return Intero rappresentante l'ID locale.
     * @since 1.2.0
     */
    public int getId_evento() {
        return id_evento;
    }

    /**
     * Imposta l'ID locale dell'evento.
     * @param id_evento Nuovo ID locale.
     * @since 1.2.0
     */
    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    /**
     * Recupera l'azienda associata.
     * @return Entità {@link AziendaEntity}.
     * @since 1.2.0
     */
    public AziendaEntity getAzienda() {
        return azienda;
    }

    /**
     * Associa l'evento a un'azienda.
     * @param azienda Nuova azienda proprietaria.
     * @since 1.2.0
     */
    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }

    /**
     * Recupera il nome dell'evento.
     * @return Stringa del nome.
     * @since 1.2.0
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'evento.
     * @param nome Nuovo nome.
     * @since 1.2.0
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Recupera il luogo dell'evento.
     * @return Stringa del luogo.
     * @since 1.3.0
     */
    public String getLuogo() {
        return luogo;
    }

    /**
     * Imposta il luogo dell'evento.
     * @param luogo Nuovo luogo.
     * @since 1.3.0
     */
    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    /**
     * Recupera la data e ora di inizio.
     * @return {@link LocalDateTime} di inizio.
     * @since 1.2.0
     */
    public LocalDateTime getData_ora_inizio() {
        return data_ora_inizio;
    }

    /**
     * Imposta la data e ora di inizio.
     * @param data_ora_inizio Nuovo timestamp di inizio.
     * @since 1.2.0
     */
    public void setData_ora_inizio(LocalDateTime data_ora_inizio) {
        this.data_ora_inizio = data_ora_inizio;
    }

    /**
     * Recupera la data e ora di fine.
     * @return {@link LocalDateTime} di fine.
     * @since 1.2.0
     */
    public LocalDateTime getData_fine() {
        return data_fine;
    }

    /**
     * Imposta la data e ora di fine.
     * @param data_fine Nuovo timestamp di fine.
     * @since 1.2.0
     */
    public void setData_fine(LocalDateTime data_fine) {
        this.data_fine = data_fine;
    }

    /**
     * Recupera il creatore dell'evento.
     * @return Entità {@link UtenteEntity}.
     * @since 1.4.1
     */
    public UtenteEntity getCreatore() {
        return creatore;
    }

    /**
     * Imposta il creatore dell'evento.
     * @param creatore Utente creatore.
     * @since 1.4.1
     */
    public void setCreatore(UtenteEntity creatore) {
        this.creatore = creatore;
    }
}