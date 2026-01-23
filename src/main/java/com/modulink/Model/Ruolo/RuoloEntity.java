package com.modulink.Model.Ruolo;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Pertinenza.PertinenzaEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta l'entità di persistenza <strong>Ruolo</strong> all'interno dell'architettura RBAC (Role-Based Access Control) di Modulink.
 * <p>
 * Questa classe mappa la tabella <code>Ruolo</code> dello schema <code>modulink</code> e definisce i profili di autorizzazione
 * assegnabili agli utenti all'interno di un contesto aziendale (Tenant).
 * </p>
 * <p>
 * L'entità utilizza una <strong>chiave primaria composta</strong> definita dalla classe {@link RuoloID},
 * costituita dalla coppia ({@code id_ruolo}, {@code id_azienda}). Questa strategia di partizionamento consente
 * la coesistenza di ID ruolo identici (es. ID 1) in tenant differenti, garantendo l'isolamento logico dei dati.
 * </p>
 *
 * @see RuoloID
 * @see AziendaEntity
 * @author Modulink Team
 * @version 1.5.0
 * @since 1.0.0
 */
@Entity
@Table(name="Ruolo", schema="modulink")
@IdClass(RuoloID.class)
public class RuoloEntity {

    /**
     * Identificativo numerico locale del ruolo.
     * <p>
     * Parte della chiave primaria composta. Questo valore viene gestito programmaticamente per mantenere sequenze separate per ogni azienda.
     * </p>
     *
     * @since 1.0.0
     */
    @Id
    @Column(name="ID_Ruolo", nullable = false)
    private int id_ruolo;

    /**
     * Riferimento all'azienda proprietaria del ruolo.
     * <p>
     * Parte della chiave primaria composta (Partition Key).
     * La relazione è gestita con una Foreign Key vincolata a livello database con clausola <code>ON DELETE CASCADE</code>,
     * assicurando che l'eliminazione di un'azienda comporti la rimozione automatica di tutti i suoi ruoli.
     * </p>
     *
     * @since 1.0.0
     */
    @Id
    @ManyToOne
    @JoinColumn(name="ID_Azienda", referencedColumnName = "ID_Azienda", nullable = false, foreignKey = @ForeignKey(name = "FK_Ruolo_Azienda"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AziendaEntity azienda;

    /**
     * Denominazione funzionale del ruolo (es. "Amministratore", "Revisore").
     * <p>
     * Campo obbligatorio utilizzato per l'identificazione visiva nelle interfacce di gestione.
     * </p>
     *
     * @since 1.0.0
     */
    @Column(name="Nome", nullable = false)
    private String nome;

    /**
     * Codice colore esadecimale (es. "#FF5733") o nome CSS standard.
     * <p>
     * Attributo di presentazione utilizzato dal frontend per renderizzare badge o etichette distintive per il ruolo.
     * </p>
     *
     * @since 1.0.0
     */
    @Column(name="Colore", nullable = false)
    private String colore;

    /**
     * Descrizione testuale estesa delle responsabilità e dei privilegi conferiti dal ruolo.
     * <p>
     * Campo opzionale (Nullable).
     * </p>
     *
     * @since 1.0.0
     */
    @Column(name="Descrizione")
    private String descrizione;

    /**
     * Insieme delle associazioni Utente-Ruolo attive.
     * <p>
     * Relazione One-To-Many bidirezionale verso {@link AssociazioneEntity}.
     * Configurato con <code>FetchType.EAGER</code> per caricare immediatamente gli utenti associati
     * e <code>CascadeType.ALL</code> per propagare le operazioni di persistenza/rimozione.
     * </p>
     *
     * @since 1.0.0
     */
    @OneToMany(mappedBy = "ruolo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AssociazioneEntity> associazioni = new HashSet<>();

    /**
     * Insieme delle affiliazioni o pertinenze ai Moduli.
     * <p>
     * Relazione One-To-Many verso {@link PertinenzaEntity}, definisce quali moduli funzionali
     * sono accessibili agli utenti che possiedono questo ruolo.
     * </p>
     *
     * @since 1.2.0
     */
    @OneToMany(mappedBy = "ruolo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PertinenzaEntity> affiliazioni = new HashSet<>();

    /**
     * Costruttore predefinito (No-Args).
     * <p>
     * Necessario per la conformità alle specifiche JPA (Java Persistence API).
     * </p>
     *
     * @since 1.0.0
     */
    public RuoloEntity(){}

    /**
     * Costruttore di convenienza per la creazione di nuovi ruoli (con ID provvisorio 0).
     *
     * @param azienda     L'azienda di appartenenza.
     * @param nome        Il nome del ruolo.
     * @param colore      Il colore distintivo.
     * @param descrizione La descrizione funzionale.
     * @since 1.0.0
     */
    public RuoloEntity(AziendaEntity azienda, String nome, String colore, String descrizione) {
        this.id_ruolo = 0;
        this.azienda = azienda;
        this.nome = nome;
        this.colore = colore;
        this.descrizione = descrizione;
    }

    /**
     * Costruttore completo per l'inizializzazione dell'entità.
     *
     * @param id_ruolo    L'ID numerico locale.
     * @param azienda     L'azienda di appartenenza.
     * @param nome        Il nome del ruolo.
     * @param colore      Il colore distintivo.
     * @param descrizione La descrizione funzionale.
     * @since 1.0.0
     */
    public RuoloEntity(int id_ruolo, AziendaEntity azienda, String nome, String colore, String descrizione) {
        this.id_ruolo = id_ruolo;
        this.azienda = azienda;
        this.nome = nome;
        this.colore = colore;
        this.descrizione = descrizione;
    }

    /**
     * Recupera l'ID locale del ruolo.
     * @return Intero ID.
     * @since 1.0.0
     */
    public int getId_ruolo() {
        return id_ruolo;
    }

    /**
     * Imposta l'ID locale del ruolo.
     * @param id_ruolo Nuovo ID.
     * @since 1.0.0
     */
    public void setId_ruolo(int id_ruolo) {
        this.id_ruolo = id_ruolo;
    }

    /**
     * Recupera l'azienda associata.
     * @return Oggetto {@link AziendaEntity}.
     * @since 1.0.0
     */
    public AziendaEntity getAzienda() {
        return azienda;
    }

    /**
     * Imposta l'azienda associata.
     * @param azienda Nuova azienda.
     * @since 1.0.0
     */
    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }

    /**
     * Recupera il nome del ruolo.
     * @return Stringa nome.
     * @since 1.0.0
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del ruolo.
     * @param nome Nuovo nome.
     * @since 1.0.0
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Recupera il colore del ruolo.
     * @return Stringa codice colore.
     * @since 1.0.0
     */
    public String getColore() {
        return colore;
    }

    /**
     * Imposta il colore del ruolo.
     * @param colore Nuovo codice colore.
     * @since 1.0.0
     */
    public void setColore(String colore) {
        this.colore = colore;
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
     * Recupera le associazioni utente collegate.
     * @return Set di {@link AssociazioneEntity}.
     * @since 1.0.0
     */
    public Set<AssociazioneEntity> getAssociazioni() {
        return associazioni;
    }

    /**
     * Imposta le associazioni utente.
     * @param associazioni Nuovo set di associazioni.
     * @since 1.0.0
     */
    public void setAssociazioni(Set<AssociazioneEntity> associazioni) {
        this.associazioni = associazioni;
    }

    /**
     * Recupera le affiliazioni ai moduli.
     * @return Set di {@link PertinenzaEntity}.
     * @since 1.2.0
     */
    public Set<PertinenzaEntity> getAffiliazioni() {
        return affiliazioni;
    }

    /**
     * Imposta le affiliazioni ai moduli.
     * @param affiliazioni Nuovo set di affiliazioni.
     * @since 1.2.0
     */
    public void setAffiliazioni(Set<PertinenzaEntity> affiliazioni) {
        this.affiliazioni = affiliazioni;
    }
}