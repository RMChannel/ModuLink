package com.modulink.Model.Utente.Associazione;

import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;

/**
 * Entità intermedia che mappa la tabella di unione <strong>Associazione</strong>.
 * <p>
 * Questa classe implementa il pattern "Join Entity" (o Associative Entity) per modellare la relazione
 * Molti-a-Molti tra {@link UtenteEntity} e {@link RuoloEntity}.
 * <p>
 * <h2>Perché non usare @ManyToMany?</h2>
 * <p>
 * Poiché sia <code>Utente</code> che <code>Ruolo</code> hanno una chiave primaria composta che include
 * la colonna condivisa <code>ID_Azienda</code>, l'annotazione standard <code>@ManyToMany</code> di Hibernate
 * genera conflitti di mapping (errore "Repeated column") non sapendo a quale delle due entità
 * attribuire la proprietà della colonna <code>ID_Azienda</code> nella tabella di join.
 * <p>
 * Questa entità risolve il problema mappando esplicitamente le colonne ID come attributi semplici
 * e definendo le relazioni come <code>@ManyToOne</code> in sola lettura (insertable=false, updatable=false).
 *
 * @see AssociazioneID
 * @see UtenteEntity
 * @see RuoloEntity
 * @author Modulink Team
 * @version 1.0
 */
@Entity
@Table(name = "Associazione", schema = "modulink")
@IdClass(AssociazioneID.class)
public class AssociazioneEntity {

    /**
     * Parte della chiave primaria: ID dell'utente associato.
     * Mappato direttamente sulla colonna fisica.
     */
    @Id
    @Column(name = "ID_Utente")
    private int id_utente;

    /**
     * Parte della chiave primaria: ID del ruolo assegnato.
     * Mappato direttamente sulla colonna fisica.
     */
    @Id
    @Column(name = "ID_Ruolo")
    private int id_ruolo;

    /**
     * Parte della chiave primaria: ID dell'azienda condivisa.
     * <p>
     * Questo campo garantisce che l'associazione avvenga nello stesso contesto aziendale.
     * Essendo parte della PK di entrambe le entità collegate, funge da discriminante
     * per l'integrità dei dati (Multi-tenancy).
     */
    @Id
    @Column(name = "ID_Azienda")
    private int id_azienda;

    /**
     * Riferimento all'oggetto Utente.
     * <p>
     * Mappato con <code>insertable = false, updatable = false</code> perché la gestione
     * della persistenza dei valori avviene tramite i campi primitivi <code>id_utente</code> e <code>id_azienda</code>.
     * Serve a JPA per navigare la relazione e caricare l'oggetto completo.
     */
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Utente", referencedColumnName = "ID_Utente", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    private UtenteEntity utente;

    /**
     * Riferimento all'oggetto Ruolo.
     * <p>
     * Mappato con <code>insertable = false, updatable = false</code>.
     * Nota come <code>ID_Azienda</code> venga riutilizzato qui nella definizione delle JoinColumns
     * senza causare conflitti ("Repeated column") grazie alla modalità sola lettura.
     */
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Ruolo", referencedColumnName = "ID_Ruolo", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    private RuoloEntity ruolo;

    /**
     * Costruttore vuoto.
     * Necessario per JPA per istanziare la classe via reflection.
     */
    public AssociazioneEntity() {}

    /**
     * Costruttore principale per creare una nuova associazione.
     * <p>
     * Estrae automaticamente gli ID dalle entità passate e popola i campi della chiave primaria.
     * Questo garantisce che lo stato dell'oggetto sia consistente prima del salvataggio.
     *
     * @param utente L'utente a cui assegnare il ruolo.
     * @param ruolo  Il ruolo da assegnare.
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
     * Restituisce l'entità Utente associata.
     * @return L'oggetto UtenteEntity completo.
     */
    public UtenteEntity getUtente() { return utente; }

    /**
     * Restituisce l'entità Ruolo associata.
     * @return L'oggetto RuoloEntity completo.
     */
    public RuoloEntity getRuolo() { return ruolo; }

    // --- Getter per i campi ID (Opzionali ma utili per DTO o logica interna) ---

    public int getId_utente() {
        return id_utente;
    }

    public int getId_ruolo() {
        return id_ruolo;
    }

    public int getId_azienda() {
        return id_azienda;
    }
}