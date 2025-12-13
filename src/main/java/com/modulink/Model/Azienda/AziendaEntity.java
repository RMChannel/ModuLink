package com.modulink.Model.Azienda;

import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;

/**
 * Rappresenta l'entità <strong>Azienda</strong> nel sistema Modulink.
 * <p>
 * Questa classe mappa la tabella <code>Azienda</code> e funge da root per il contesto multi-tenant
 * dell'applicazione. Ogni azienda è identificata univocamente e possiede un proprio set di utenti e ruoli.
 * <p>
 * <strong>Gestione della Relazione con il Responsabile:</strong>
 * <p>
 * Poiché l'entità {@link UtenteEntity} utilizza una chiave primaria composta ({@code ID_Utente, ID_Azienda}),
 * mappare direttamente il responsabile creerebbe un conflitto sulla colonna {@code ID_Azienda}
 * (che è contemporaneamente PK di questa tabella e parte della FK verso l'utente).
 * <p>
 * Per risolvere il problema, viene adottata una strategia di mapping ibrida:
 * <ul>
 * <li>Una colonna "raw" ({@code responsabileIdUtente}) gestisce la scrittura nel DB.</li>
 * <li>Una relazione JPA ({@code responsabile}) in sola lettura gestisce il recupero dell'oggetto.</li>
 * </ul>
 *
 * @see UtenteEntity
 * @author Modulink Team
 * @version 1.1
 */
@Entity
@Table(name="Azienda", schema="modulink")
public class AziendaEntity {

    /**
     * Identificativo univoco dell'azienda (Chiave Primaria).
     * <p>
     * Generato automaticamente dal database (auto-increment).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_Azienda", nullable = false, unique = true)
    private int id_azienda;

    /**
     * Nome o ragione sociale dell'azienda.
     */
    @Column(name="Nome", nullable = false, unique = true)
    private String nome;

    /**
     * Partita IVA dell'azienda.
     * Identificativo fiscale univoco.
     */
    @Column(name="P_IVA", nullable = false, unique = true)
    private String piva;

    /**
     * Indirizzo della sede legale o operativa.
     */
    @Column(name="Indirizzo", nullable = false)
    private String indirizzo;

    /**
     * Città di locazione dell'azienda.
     */
    @Column(name="Citta", nullable = false)
    private String citta;

    /**
     * Codice di Avviamento Postale.
     */
    @Column(name="CAP", nullable = false)
    private String cap;

    /**
     * Recapito telefonico principale dell'azienda.
     */
    @Column(name="Telefono", nullable = false, unique = true)
    private String telefono;

    // --- MODIFICA 1: Mappiamo la colonna fisica dell'ID responsabile ---
    // Questo campo gestirà il salvataggio effettivo nel database (INSERT/UPDATE).
    /**
     * Identificativo numerico dell'utente responsabile (Foreign Key logica).
     * <p>
     * Questo campo mappa la colonna fisica sul database ed è l'unico responsabile
     * delle operazioni di scrittura (INSERT/UPDATE) per la relazione.
     */
    @Column(name="Responsabile_ID_Utente", nullable = false)
    private Integer responsabileIdUtente;

    // --- MODIFICA 2: Mappiamo la relazione in SOLA LETTURA ---
    // Serve solo per caricare l'oggetto Utente quando leggiamo l'Azienda.
    // Entrambe le colonne sono insertable=false, updatable=false per evitare l'errore "mix".
    /**
     * Riferimento all'oggetto Utente che funge da responsabile.
     * <p>
     * Mappato come {@code insertable=false, updatable=false}.
     * Questo oggetto viene popolato automaticamente da Hibernate in lettura,
     * ma le modifiche devono passare attraverso il setter che aggiorna {@code responsabileIdUtente}.
     */
    @OneToOne
    @JoinColumns({
            @JoinColumn(name="Responsabile_ID_Utente", referencedColumnName = "ID_Utente", insertable = false, updatable = false),
            @JoinColumn(name="ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    private UtenteEntity responsabile;

    /**
     * Costruttore vuoto predefinito.
     * Richiesto dalle specifiche JPA.
     */
    public AziendaEntity() {}

    /**
     * Costruttore completo per la creazione di una nuova azienda.
     *
     * @param nome        Ragione sociale.
     * @param piva        Partita IVA.
     * @param indirizzo   Indirizzo.
     * @param citta       Città.
     * @param cap         CAP.
     * @param telefono    Telefono.
     * @param responsabile L'utente responsabile (può essere null inizialmente).
     */
    public AziendaEntity(String nome, String piva, String indirizzo, String citta, String cap, String telefono, UtenteEntity responsabile) {
        this.nome = nome;
        this.piva = piva;
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.cap = cap;
        this.telefono = telefono;
        this.setResponsabile(responsabile); // Usiamo il setter per sincronizzare l'ID
    }

    // --- GETTER E SETTER ---

    // ... altri getter e setter invariati ...

    /**
     * Restituisce l'ID univoco dell'azienda.
     * @return L'intero ID.
     */
    public int getId_azienda() {
        return id_azienda;
    }

    /**
     * Imposta l'ID dell'azienda.
     * @param id_azienda Il nuovo ID.
     */
    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    /**
     * Restituisce l'entità Utente responsabile.
     * @return L'oggetto UtenteEntity.
     */
    public UtenteEntity getResponsabile() {
        return responsabile;
    }

    // --- MODIFICA 3: Setter intelligente ---
    // Quando impostiamo l'oggetto Utente, aggiorniamo automaticamente
    // anche l'Integer responsabileIdUtente che verrà salvato sul DB.
    /**
     * Imposta il responsabile dell'azienda e sincronizza la chiave esterna.
     * <p>
     * Questo metodo contiene la logica fondamentale per il funzionamento del mapping ibrido:
     * quando viene passato un oggetto {@link UtenteEntity}, estrae il suo ID e aggiorna
     * il campo {@code responsabileIdUtente} che verrà effettivamente persistito nel DB.
     *
     * @param responsabile Il nuovo utente responsabile (o null).
     */
    public void setResponsabile(UtenteEntity responsabile) {
        this.responsabile = responsabile;
        if (responsabile != null) {
            this.responsabileIdUtente = responsabile.getId_utente();
        } else {
            this.responsabileIdUtente = null;
        }
    }

    // Getter per il campo raw (utile ma opzionale)
    /**
     * Restituisce l'ID numerico del responsabile (campo raw).
     * @return L'ID utente del responsabile.
     */
    public Integer getResponsabileIdUtente() {
        return responsabileIdUtente;
    }

    // Nota: Non serve un setter pubblico per responsabileIdUtente se usiamo setResponsabile,
    // ma JPA potrebbe richiederlo per la reflection, quindi lo mettiamo.
    /**
     * Imposta manualmente l'ID del responsabile.
     * <p>
     * Solitamente è preferibile usare {@link #setResponsabile(UtenteEntity)},
     * ma questo metodo è necessario per la reflection di JPA.
     * @param responsabileIdUtente L'ID numerico.
     */
    public void setResponsabileIdUtente(Integer responsabileIdUtente) {
        this.responsabileIdUtente = responsabileIdUtente;
    }

    // ... getter e setter per nome, piva, indirizzo, ecc...
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getPiva() { return piva; }
    public void setPiva(String piva) { this.piva = piva; }
    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }
    public String getCap() { return cap; }
    public void setCap(String cap) { this.cap = cap; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}