package com.modulink.Model.Ruolo;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta l'entità <strong>Ruolo</strong> nel sistema Modulink.
 * <p>
 * Questa classe mappa la tabella <code>Ruolo</code> dello schema <code>modulink</code>.
 * I ruoli definiscono le mansioni o i livelli di accesso degli utenti all'interno di una specifica azienda,
 * costituendo la base per il controllo degli accessi basato sui ruoli (RBAC).
 * <p>
 * L'entità utilizza una chiave primaria composta definita dalla classe {@link RuoloID},
 * formata dall'unione dell'ID del ruolo e dell'ID dell'azienda. Questo permette a diverse aziende
 * di avere ruoli con lo stesso ID locale (es. ID 1 = Admin per l'azienda A, ID 1 = Admin per l'azienda B),
 * mantenendo l'isolamento dei dati (Multi-tenancy).
 *
 * @see RuoloID
 * @see AziendaEntity
 * @author Modulink Team
 * @version 1.0
 */
@Entity
@Table(name="Ruolo", schema="modulink")
@IdClass(RuoloID.class)
public class RuoloEntity {

    /**
     * Identificativo numerico del ruolo.
     * <p>
     * Questo campo è parte della chiave primaria composta.
     * Non essendo annotato con <code>@GeneratedValue</code>, il valore deve essere
     * assegnato logicamente dall'applicazione (es. sequenziale per azienda).
     */
    @Id
    @Column(name="ID_Ruolo", nullable = false)
    private int id_ruolo;

    /**
     * L'azienda a cui questo ruolo appartiene.
     * <p>
     * Parte della chiave primaria composta. Definisce il contesto aziendale
     * in cui il ruolo è valido e garantisce l'univocità della coppia (ID_Ruolo, ID_Azienda).
     * Mappata tramite relazione Many-to-One.
     */
    @Id
    @ManyToOne
    @JoinColumn(name="ID_Azienda", referencedColumnName = "ID_Azienda", nullable = false, foreignKey = @ForeignKey(name = "FK_Ruolo_Azienda"))
    private AziendaEntity azienda;

    /**
     * Nome del ruolo (es. "Amministratore", "Dipendente", "Manager").
     * <p>
     * Utilizzato per la visualizzazione nelle interfacce utente e per la logica
     * di business non basata su ID.
     */
    @Column(name="Nome", nullable = false)
    private String nome;

    /**
     * Rappresentazione cromatica del ruolo.
     * <p>
     * Solitamente contiene un codice esadecimale (es. "#FF5733") o il nome di un colore
     * utilizzato nel frontend per distinguere visivamente i ruoli (es. nei badge o calendari).
     */
    @Column(name="Colore", nullable = false)
    private String colore;

    /**
     * Descrizione opzionale delle responsabilità o dei permessi associati al ruolo.
     * Utile per fornire dettagli aggiuntivi agli amministratori di sistema.
     */
    @Column(name="Descrizione")
    private String descrizione;

    /**
     * Relazione One-To-Many verso l'entità di associazione intermedio.
     * <p>
     * Questa struttura permette di navigare dai ruoli agli utenti che li possiedono.
     */
    @OneToMany(mappedBy = "ruolo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AssociazioneEntity> associazioni = new HashSet<>();

    @OneToMany(mappedBy = "ruolo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AffiliazioneEntity> affiliazioni = new HashSet<>();

    /**
     * Costruttore vuoto predefinito.
     * <p>
     * Richiesto obbligatoriamente da JPA per l'istanziazione dell'entità tramite reflection
     * durante il recupero dei dati dal database.
     */
    public RuoloEntity(){}

    public RuoloEntity(AziendaEntity azienda, String nome, String colore, String descrizione) {
        this.id_ruolo = 0;
        this.azienda = azienda;
        this.nome = nome;
        this.colore = colore;
        this.descrizione = descrizione;
    }

    /**
     * Costruttore completo per inizializzare un nuovo Ruolo.
     *
     * @param id_ruolo    L'ID numerico del ruolo (parte della PK).
     * @param azienda     L'azienda di appartenenza (parte della PK).
     * @param nome        Il nome descrittivo del ruolo.
     * @param colore      Il colore associato al ruolo (es. Hex code).
     * @param descrizione Una descrizione breve del ruolo.
     */
    public RuoloEntity(int id_ruolo, AziendaEntity azienda, String nome, String colore, String descrizione) {
        this.id_ruolo = id_ruolo;
        this.azienda = azienda;
        this.nome = nome;
        this.colore = colore;
        this.descrizione = descrizione;
    }

    /**
     * Restituisce l'ID locale del ruolo.
     *
     * @return L'intero che identifica il ruolo all'interno dell'azienda.
     */
    public int getId_ruolo() {
        return id_ruolo;
    }

    /**
     * Imposta l'ID locale del ruolo.
     *
     * @param id_ruolo Il nuovo ID da assegnare.
     */
    public void setId_ruolo(int id_ruolo) {
        this.id_ruolo = id_ruolo;
    }

    /**
     * Restituisce l'azienda associata a questo ruolo.
     *
     * @return L'oggetto {@link AziendaEntity} di appartenenza.
     */
    public AziendaEntity getAzienda() {
        return azienda;
    }

    /**
     * Associa il ruolo a una specifica azienda.
     *
     * @param azienda La nuova azienda di appartenenza.
     */
    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }

    /**
     * Restituisce il nome visualizzabile del ruolo.
     *
     * @return Il nome del ruolo.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del ruolo.
     *
     * @param nome Il nome da assegnare.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il colore associato al ruolo per scopi di UI.
     *
     * @return La stringa rappresentante il colore.
     */
    public String getColore() {
        return colore;
    }

    /**
     * Imposta il colore del ruolo.
     *
     * @param colore Il nuovo colore (es. codice HEX o nome CSS).
     */
    public void setColore(String colore) {
        this.colore = colore;
    }

    /**
     * Restituisce la descrizione dettagliata del ruolo.
     *
     * @return La descrizione, o null se non presente.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione del ruolo.
     *
     * @param descrizione La nuova descrizione.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Set<AssociazioneEntity> getAssociazioni() {
        return associazioni;
    }

    public void setAssociazioni(Set<AssociazioneEntity> associazioni) {
        this.associazioni = associazioni;
    }

    public Set<AffiliazioneEntity> getAffiliazioni() {
        return affiliazioni;
    }

    public void setAffiliazioni(Set<AffiliazioneEntity> affiliazioni) {
        this.affiliazioni = affiliazioni;
    }
}