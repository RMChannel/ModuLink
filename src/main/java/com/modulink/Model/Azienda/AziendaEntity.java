package com.modulink.Model.Azienda;

import jakarta.persistence.*;

/**
 * Rappresenta l'entità di persistenza <strong>Azienda</strong> all'interno dell'architettura multi-tenant del sistema Modulink.
 * <p>
 * Questa classe definisce il modello dati per la tabella <code>Azienda</code> (schema <code>modulink</code>), fungendo da
 * root entity per l'aggregazione di utenti, ruoli e configurazioni specifiche per tenant.
 * La classe implementa la mappatura JPA standard e include vincoli di unicità a livello database
 * per garantire l'integrità dei dati aziendali critici.
 * </p>
 * <p>
 * Le proprietà fondamentali includono identificativi fiscali (P.IVA) e contatti operativi,
 * essenziali per la fatturazione e la gestione logistica.
 * </p>
 *
 * @author Modulink Team
 * @version 2.1.4
 * @since 1.0.0
 */
@Entity
@Table(name="Azienda", schema="modulink", uniqueConstraints = {
        @UniqueConstraint(columnNames = "P_IVA"),
        @UniqueConstraint(columnNames = "Nome"),
        @UniqueConstraint(columnNames = "Telefono")
})
public class AziendaEntity {

    /**
     * Identificativo primario univoco (Surrogate Key) dell'entità Azienda.
     * <p>
     * Questo valore è generato automaticamente dal database mediante strategia {@link GenerationType#IDENTITY}
     * (tipicamente <code>AUTO_INCREMENT</code> su MySQL/PostgreSQL).
     * </p>
     *
     * @since 1.0.0
     */
    @Id
    @Column(name="ID_Azienda", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id_azienda;

    /**
     * Ragione sociale o denominazione commerciale dell'azienda.
     * <p>
     * Vincolo di unicità applicato a livello di database. Non può assumere valore <code>null</code>.
     * </p>
     *
     * @since 1.0.0
     */
    @Column(name="Nome", nullable = false, unique = true, length = 100)
    private String nome;

    /**
     * Partita IVA (Identificativo Fiscale).
     * <p>
     * Stringa alfanumerica che identifica univocamente il soggetto fiscale.
     * Deve rispettare il formato standard nazionale.
     * </p>
     *
     * @since 1.0.0
     */
    @Column(name="P_IVA", nullable = false, unique = true, length = 16)
    private String piva;

    /**
     * Indirizzo fisico della sede legale.
     *
     * @since 1.1.0
     */
    @Column(name="Indirizzo", nullable = false)
    private String indirizzo;

    /**
     * Città di residenza della sede legale.
     *
     * @since 1.1.0
     */
    @Column(name="Citta", nullable = false, length = 50)
    private String citta;

    /**
     * Codice di Avviamento Postale (CAP).
     * <p>
     * Memorizzato come stringa per supportare CAP internazionali con zeri iniziali o caratteri alfanumerici.
     * </p>
     *
     * @since 1.1.0
     */
    @Column(name="CAP", nullable = false, length = 10)
    private String cap;

    /**
     * Recapito telefonico principale per comunicazioni amministrative.
     *
     * @since 1.2.0
     */
    @Column(name="Telefono", nullable = false, unique = true, length = 20)
    private String telefono;

    /**
     * Riferimento alla risorsa grafica del logo aziendale.
     * <p>
     * Può contenere un percorso relativo al filesystem server (es. <code>/uploads/logos/xyz.png</code>)
     * o un URL assoluto se ospitato su CDN esterna.
     * </p>
     *
     * @since 1.3.5
     */
    @Column(name="Logo", nullable = false)
    private String logo;

    /**
     * Costruttore predefinito richiesto dalle specifiche JPA (Java Persistence API).
     * <p>
     * Deve essere <code>public</code> o <code>protected</code> per permettere al provider di persistenza
     * di istanziare la classe tramite reflection.
     * </p>
     *
     * @since 1.0.0
     */
    public AziendaEntity() {}

    /**
     * Costruttore parametrico per l'inizializzazione completa di una nuova istanza.
     *
     * @param nome        Ragione sociale dell'azienda.
     * @param piva        Partita IVA univoca.
     * @param indirizzo   Indirizzo della sede legale.
     * @param citta       Città della sede legale.
     * @param cap         Codice di Avviamento Postale.
     * @param telefono    Recapito telefonico principale.
     * @param logo        URI o path del logo aziendale.
     * @since 1.0.0
     */
    public AziendaEntity(String nome, String piva, String indirizzo, String citta, String cap, String telefono, String logo) {
        this.nome = nome;
        this.piva = piva;
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.cap = cap;
        this.telefono = telefono;
        this.logo = logo;
    }

    // --- GETTER E SETTER ---

    /**
     * Recupera l'identificativo primario dell'azienda.
     *
     * @return L'intero rappresentante la Primary Key.
     * @since 1.0.0
     */
    public int getId_azienda() {
        return id_azienda;
    }

    /**
     * Imposta l'identificativo primario dell'azienda.
     * <p>
     * <strong>Nota:</strong> L'uso manuale di questo metodo è sconsigliato per entità gestite,
     * in quanto l'ID è gestito automaticamente dal provider di persistenza.
     * </p>
     *
     * @param id_azienda Il nuovo ID da assegnare.
     * @since 1.0.0
     */
    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    /**
     * Recupera la ragione sociale.
     *
     * @return Stringa contenente il nome dell'azienda.
     * @since 1.0.0
     */
    public String getNome() { return nome; }

    /**
     * Aggiorna la ragione sociale.
     *
     * @param nome La nuova ragione sociale.
     * @since 1.0.0
     */
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Recupera la Partita IVA.
     *
     * @return Stringa rappresentante la P.IVA.
     * @since 1.0.0
     */
    public String getPiva() { return piva; }

    /**
     * Aggiorna la Partita IVA.
     *
     * @param piva La nuova P.IVA.
     * @since 1.0.0
     */
    public void setPiva(String piva) { this.piva = piva; }

    /**
     * Recupera l'indirizzo della sede legale.
     *
     * @return Stringa dell'indirizzo.
     * @since 1.1.0
     */
    public String getIndirizzo() { return indirizzo; }

    /**
     * Imposta l'indirizzo della sede legale.
     *
     * @param indirizzo Il nuovo indirizzo.
     * @since 1.1.0
     */
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    /**
     * Recupera la città.
     *
     * @return Stringa nome città.
     * @since 1.1.0
     */
    public String getCitta() { return citta; }

    /**
     * Imposta la città.
     *
     * @param citta Nome della città.
     * @since 1.1.0
     */
    public void setCitta(String citta) { this.citta = citta; }

    /**
     * Recupera il CAP.
     *
     * @return Stringa del CAP.
     * @since 1.1.0
     */
    public String getCap() { return cap; }

    /**
     * Imposta il CAP.
     *
     * @param cap Il nuovo CAP.
     * @since 1.1.0
     */
    public void setCap(String cap) { this.cap = cap; }

    /**
     * Recupera il numero di telefono.
     *
     * @return Stringa del telefono.
     * @since 1.2.0
     */
    public String getTelefono() { return telefono; }

    /**
     * Imposta il numero di telefono.
     *
     * @param telefono Il nuovo numero di telefono.
     * @since 1.2.0
     */
    public void setTelefono(String telefono) { this.telefono = telefono; }

    /**
     * Recupera il path o URL del logo.
     *
     * @return Stringa identificativa della risorsa logo.
     * @since 1.3.5
     */
    public String getLogo() { return logo; }

    /**
     * Imposta il path o URL del logo.
     *
     * @param logo La nuova stringa identificativa della risorsa logo.
     * @since 1.3.5
     */
    public void setLogo(String logo) { this.logo = logo; }

}