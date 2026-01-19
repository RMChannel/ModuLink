package com.modulink.Model.Modulo;

import com.modulink.Model.Relazioni.Pertinenza.PertinenzaEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta l'entità anagrafica di un <strong>Modulo</strong> funzionale all'interno della piattaforma Modulink.
 * <p>
 * I moduli costituiscono le unità atomiche di funzionalità del sistema (es. Gestione Dipendenti, Magazzino, ecc.).
 * Questa entità mappa la tabella <code>modulo</code> dello schema <code>modulink</code> e definisce le proprietà
 * descrittive e di navigazione (URL) necessarie per il rendering dinamico dell'interfaccia utente (Dashboard).
 * </p>
 * <p>
 * La relazione con {@link PertinenzaEntity} gestisce l'associazione logica tra il modulo e le aziende abilitate al suo utilizzo.
 * </p>
 *
 * @author Modulink Team
 * @version 2.1.0
 * @since 1.0.0
 */
@Entity
@Table(name = "modulo",schema = "modulink")
public class ModuloEntity {

    /**
     * Identificativo univoco (Primary Key) del modulo.
     * <p>
     * Questo ID è assegnato staticamente o manualmente (non autogenerato) per garantire la coerenza
     * dei riferimenti hardcoded o di configurazione attraverso diversi ambienti di deploy.
     * </p>
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "id_modulo", nullable = false)
    private int id_modulo;

    /**
     * Nome visualizzato del modulo (Label).
     *
     * @since 1.0.0
     */
    @Column(name = "nome", nullable = false)
    private String nome;

    /**
     * Descrizione estesa delle funzionalità offerte dal modulo.
     * Utilizzata nei tooltip o nelle pagine di dettaglio del marketplace interno.
     *
     * @since 1.0.0
     */
    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    /**
     * Endpoint o percorso relativo per l'accesso al modulo.
     * Utilizzato dal router lato client/server per indirizzare l'utente.
     *
     * @since 1.0.0
     */
    @Column(name = "url_modulo", nullable = false)
    private String url_modulo;

    /**
     * Percorso o URL dell'icona rappresentativa del modulo.
     * Supporta formati standard web (SVG, PNG).
     *
     * @since 1.1.0
     */
    @Column(name = "url_icona")
    private String url_icona;

    /**
     * Flag di visibilità globale.
     * <p>
     * Se <code>false</code>, il modulo viene nascosto a tutti gli utenti indipendentemente dalle pertinenze.
     * Utile per disabilitare temporaneamente funzionalità in manutenzione (Feature Toggle).
     * </p>
     *
     * @since 1.2.0
     */
    @Column(name = "visible", nullable = false)
    private boolean Visible;

    /**
     * Insieme delle relazioni di pertinenza che legano il modulo alle aziende.
     * <p>
     * Relazione 1-N gestita con fetch EAGER per ottimizzare il caricamento delle dashboard.
     * La cancellazione a cascata assicura l'integrità referenziale.
     * </p>
     *
     * @since 1.0.0
     */
    @OneToMany(mappedBy = "id_modulo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PertinenzaEntity> affiliazioni = new HashSet<>();


    /**
     * Costruttore predefinito (No-Args) richiesto da JPA.
     *
     * @since 1.0.0
     */
    public ModuloEntity(){}

    /**
     * Costruttore parametrico completo per l'inizializzazione dell'entità.
     *
     * @param id_modulo   ID univoco statico.
     * @param nome        Nome visualizzato.
     * @param descrizione Descrizione funzionale.
     * @param url_modulo  Path di navigazione.
     * @param url_icona   Path dell'icona.
     * @param Visible     Stato di visibilità.
     * @since 1.0.0
     */
    public ModuloEntity(int id_modulo, String nome, String descrizione, String url_modulo, String url_icona, boolean Visible) {
        this.id_modulo = id_modulo;
        this.nome = nome;
        this.descrizione = descrizione;
        this.url_modulo = url_modulo;
        this.url_icona = url_icona;
        this.Visible = Visible;
    }

    /**
     * Recupera l'ID del modulo.
     * @return Intero ID.
     * @since 1.0.0
     */
    public int getId_modulo() {
        return id_modulo;
    }

    /**
     * Imposta l'ID del modulo.
     * @param id_modulo Nuovo ID.
     * @since 1.0.0
     */
    public void setId_modulo(int id_modulo) {
        this.id_modulo = id_modulo;
    }

    /**
     * Recupera il nome del modulo.
     * @return Stringa nome.
     * @since 1.0.0
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del modulo.
     * @param nome Nuovo nome.
     * @since 1.0.0
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Recupera la descrizione.
     * @return Stringa descrizione.
     * @since 1.0.0
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione.
     * @param descrizione Nuova descrizione.
     * @since 1.0.0
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Recupera l'URL del modulo.
     * @return Stringa URL.
     * @since 1.0.0
     */
    public String getUrl_modulo() {
        return url_modulo;
    }

    /**
     * Imposta l'URL del modulo.
     * @param url_modulo Nuovo URL.
     * @since 1.0.0
     */
    public void setUrl_modulo(String url_modulo) {
        this.url_modulo = url_modulo;
    }

    /**
     * Recupera l'URL dell'icona.
     * @return Stringa URL icona.
     * @since 1.1.0
     */
    public String getUrl_icona() {
        return url_icona;
    }

    /**
     * Imposta l'URL dell'icona.
     * @param url_icona Nuovo URL icona.
     * @since 1.1.0
     */
    public void setUrl_icona(String url_icona) {
        this.url_icona = url_icona;
    }

    /**
     * Recupera le affiliazioni (pertinenze) associate.
     * @return Set di {@link PertinenzaEntity}.
     * @since 1.0.0
     */
    public Set<PertinenzaEntity> getAffiliazioni() {
        return affiliazioni;
    }

    /**
     * Imposta le affiliazioni.
     * @param affiliazioni Nuovo set di pertinenze.
     * @since 1.0.0
     */
    public void setAffiliazioni(Set<PertinenzaEntity> affiliazioni) {
        this.affiliazioni = affiliazioni;
    }

    /**
     * Verifica se il modulo è visibile globalmente.
     * @return {@code true} se visibile, {@code false} altrimenti.
     * @since 1.2.0
     */
    public boolean isVisible() {
        return Visible;
    }

    /**
     * Imposta la visibilità globale del modulo.
     * @param visible Stato di visibilità.
     * @since 1.2.0
     */
    public void setVisible(boolean visible) {
        Visible = visible;
    }

    /**
     * Rappresentazione stringa dell'oggetto per debug.
     * @return Stringa contenente i valori dei campi principali.
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return "ModuloEntity{" +
                "id_modulo=" + id_modulo +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", url_modulo='" + url_modulo + '\'' +
                ", url_icona='" + url_icona + '\'' +
                ", Visible=" + Visible +
                ", affiliazioni=" + affiliazioni +
                '}';
    }
}
