package com.modulink.Model.Azienda;

import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Prodotto.ProdottoEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta l'entità <strong>Azienda</strong> nel sistema Modulink.
 * <p>
 * Questa classe mappa la tabella <code>Azienda</code> e funge da root per il contesto multi-tenant
 * dell'applicazione. Ogni azienda è identificata univocamente e possiede un proprio set di utenti e ruoli.
 *
 * @author Modulink Team
 * @version 1.3
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
    @Column(name="ID_Azienda", nullable = false, unique = true)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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

    /**
     * Percorso relativo, URL o nome file del logo aziendale.
     * <p>
     * Questo campo può contenere il path dell'immagine memorizzata sul server
     * o un riferimento esterno.
     */
    @Column(name="Logo", nullable = false)
    private String logo;
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
     * @param logo        Percorso o nome file del logo aziendale.
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

    /**
     * Restituisce il percorso o il riferimento al logo.
     * @return Stringa contenente il path del logo.
     */
    public String getLogo() { return logo; }

    /**
     * Imposta il percorso o il riferimento al logo.
     * @param logo Stringa contenente il nuovo path del logo.
     */
    public void setLogo(String logo) { this.logo = logo; }

}