package com.modulink.Model.Relazioni.Associazione;

import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entità di persistenza <strong>Associazione</strong>, utilizzata per modellare la relazione molti-a-molti (Many-to-Many) tra Utenti e Ruoli.
 * <p>
 * Questa classe funge da entità associativa esplicita per gestire chiavi primarie composte in un ambiente multi-tenant.
 * L'utilizzo di un'entità intermedia dedicata permette di superare i limiti delle annotazioni <code>@ManyToMany</code> standard
 * di Hibernate, garantendo il controllo granulare sulla colonna <code>ID_Azienda</code> condivisa tra {@link UtenteEntity} e {@link RuoloEntity}.
 * </p>
 * <p>
 * L'entità implementa la strategia di chiave composta tramite la classe {@link AssocazioneID}.
 * </p>
 *
 * @see AssocazioneID
 * @see UtenteEntity
 * @see RuoloEntity
 * @author Modulink Team
 * @version 1.1.2
 * @since 1.0.0
 */
@Entity
@Table(name = "Associazione", schema = "modulink")
@IdClass(AssocazioneID.class)
public class AssociazioneEntity {

    /**
     * Identificativo univoco dell'Utente.
     * Parte della chiave primaria composta dell'associazione.
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "ID_Utente")
    private int id_utente;

    /**
     * Identificativo univoco del Ruolo.
     * Parte della chiave primaria composta dell'associazione.
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "ID_Ruolo")
    private int id_ruolo;

    /**
     * Identificativo univoco dell'Azienda (Tenant).
     * <p>
     * Garantisce l'isolamento dei dati a livello aziendale. Una cancellazione a cascata
     * sull'entità Azienda comporta la rimozione automatica di tutte le associazioni correlate.
     * </p>
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "ID_Azienda")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private int id_azienda;

    /**
     * Riferimento oggettuale all'entità {@link UtenteEntity}.
     * <p>
     * Mappatura in sola lettura (insertable=false, updatable=false) per la navigazione del grafo degli oggetti.
     * La persistenza degli ID è gestita tramite i campi primitivi.
     * </p>
     *
     * @since 1.0.0
     */
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Utente", referencedColumnName = "ID_Utente", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UtenteEntity utente;

    /**
     * Riferimento oggettuale all'entità {@link RuoloEntity}.
     * <p>
     * Mappatura in sola lettura per garantire l'integrità del mapping della colonna <code>ID_Azienda</code>
     * riutilizzata in più join.
     * </p>
     *
     * @since 1.0.0
     */
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Ruolo", referencedColumnName = "ID_Ruolo", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RuoloEntity ruolo;

    /**
     * Costruttore predefinito richiesto dalle specifiche JPA.
     *
     * @since 1.0.0
     */
    public AssociazioneEntity() {}

    /**
     * Costruttore parametrico per l'istanziazione di una nuova relazione Utente-Ruolo.
     * <p>
     * Popola automaticamente gli identificativi primitivi estraendoli dalle entità fornite,
     * assicurando la coerenza del modello multi-tenant.
     * </p>
     *
     * @param utente Entità utente assegnataria.
     * @param ruolo  Entità ruolo da assegnare.
     * @since 1.0.0
     */
    public AssociazioneEntity(UtenteEntity utente, RuoloEntity ruolo) {
        this.utente = utente;
        this.ruolo = ruolo;

        // Popoliamo gli ID della chiave primaria prendendoli dagli oggetti
        this.id_utente = utente.getId_utente();
        this.id_ruolo = ruolo.getId_ruolo();

        // L'azienda è condivisa: la prendiamo dall'utente (assumendo logicamente che coincidano)
        this.id_azienda = utente.getAzienda().getId_azienda();
    }

    /**
     * Recupera l'oggetto UtenteEntity.
     * @return L'utente associato.
     * @since 1.0.0
     */
    public UtenteEntity getUtente() { return utente; }

    /**
     * Recupera l'oggetto RuoloEntity.
     * @return Il ruolo associato.
     * @since 1.0.0
     */
    public RuoloEntity getRuolo() { return ruolo; }

    /**
     * Recupera l'ID primitivo dell'utente.
     * @return ID utente.
     * @since 1.1.0
     */
    public int getId_utente() {
        return id_utente;
    }

    /**
     * Recupera l'ID primitivo del ruolo.
     * @return ID ruolo.
     * @since 1.1.0
     */
    public int getId_ruolo() {
        return id_ruolo;
    }

    /**
     * Recupera l'ID primitivo dell'azienda.
     * @return ID azienda.
     * @since 1.1.0
     */
    public int getId_azienda() {
        return id_azienda;
    }
}