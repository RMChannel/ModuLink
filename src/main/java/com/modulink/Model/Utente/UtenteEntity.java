package com.modulink.Model.Utente;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta l'entità <strong>Utente</strong> nel sistema Modulink.
 * <p>
 * Questa classe mappa la tabella <code>Utente</code> all'interno dello schema <code>modulink</code>.
 * Utilizza una architettura a <strong>Chiave Primaria Composta</strong> definita dalla classe {@link UtenteID},
 * costituita dalla coppia (ID Azienda, ID Utente).
 * <p>
 * A differenza di un approccio standard con ID autoincrementale globale, qui l'identificativo dell'utente
 * è sequenziale e univoco solo all'interno della specifica azienda di appartenenza.
 *
 * @see UtenteID
 * @see AziendaEntity
 * @see RuoloEntity
 * @see AssociazioneEntity
 * @author Modulink Team
 * @version 1.3
 */
@Entity
@Table(name="Utente", schema="modulink")
@IdClass(UtenteID.class)
public class UtenteEntity {

    /**
     * Identificativo numerico dell'utente.
     * <p>
     * Questo campo è parte della chiave primaria composta.
     * <strong>Nota Importante:</strong> Non è annotato con <code>@GeneratedValue</code>.
     * Il valore deve essere calcolato e assegnato manualmente dall'applicazione (Service Layer)
     * prima del salvataggio (es. <code>MAX(ID) + 1</code> per l'azienda corrente).
     */
    @Id
    @Column(name="ID_Utente", nullable = false)
    private int id_utente;

    /**
     * L'azienda a cui appartiene l'utente.
     * <p>
     * Rappresenta una relazione Molti-a-Uno con {@link AziendaEntity}.
     * Oltre a essere una Foreign Key, questo campo costituisce la seconda parte
     * della chiave primaria composta, definendo il contesto di unicità dell'ID Utente.
     */
    @Id
    @ManyToOne
    @JoinColumn(name="ID_Azienda", referencedColumnName = "ID_Azienda", nullable = false, foreignKey = @ForeignKey(name = "FK_Utente_Azienda"))
    private AziendaEntity azienda;

    /**
     * Relazione One-To-Many verso l'entità di associazione intermedio.
     * <p>
     * Questa struttura sostituisce la classica annotazione <code>@ManyToMany</code> per risolvere
     * i conflitti generati dalla condivisione della colonna <code>ID_Azienda</code> nelle chiavi composte
     * di Utente e Ruolo.
     */
    @OneToMany(mappedBy = "utente", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AssociazioneEntity> associazioni = new HashSet<>();

    /**
     * Indirizzo email dell'utente.
     * Deve essere univoco all'interno dell'intero sistema (vincolo globale).
     */
    @Column(name="Email", nullable = false, unique = true)
    private String email;

    /**
     * Hash della password dell'utente.
     * <p>
     * La password non viene mai salvata in chiaro, ma hashata tramite BCrypt.
     */
    @Column(name="Password", nullable = false)
    private String hash_password;

    /**
     * Nome di battesimo dell'utente.
     */
    @Column(name="Nome", nullable = false)
    private String nome;

    /**
     * Cognome dell'utente.
     */
    @Column(name="Cognome", nullable = false)
    private String cognome;

    /**
     * Numero di telefono dell'utente.
     */
    @Column(name="Telefono", nullable = false)
    private String telefono;

    /**
     * Percorso (path) o URL dell'immagine del profilo dell'utente (opzionale).
     */
    @Column(name="Immagine_Profilo")
    private String path_immagine_profilo;

    /**
     * Costruttore vuoto predefinito.
     * <p>
     * Richiesto dalle specifiche JPA per la creazione dell'entità tramite reflection.
     */
    public UtenteEntity() {}

    /**
     * Costruttore completo per inizializzare un oggetto UtenteEntity.
     *
     * @param azienda               L'entità Azienda di appartenenza.
     * @param email                 L'indirizzo email dell'utente.
     * @param hash_password         La password cifrata dell'utente.
     * @param nome                  Il nome dell'utente.
     * @param cognome               Il cognome dell'utente.
     * @param telefono              Il numero di telefono.
     * @param path_immagine_profilo Il percorso dell'immagine profilo (può essere null).
     */
    public UtenteEntity(AziendaEntity azienda, String email, String hash_password, String nome, String cognome, String telefono, String path_immagine_profilo) {
        this.azienda = azienda;
        this.email = email;
        this.hash_password = hash_password;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.path_immagine_profilo = path_immagine_profilo;
    }

    // --- GETTER E SETTER STANDARD ---

    /**
     * Restituisce l'ID locale dell'utente.
     * @return L'intero rappresentante l'ID.
     */
    public int getId_utente() {
        return id_utente;
    }

    /**
     * Imposta l'ID locale dell'utente.
     * @param id_utente Il nuovo ID da assegnare (deve essere univoco per azienda).
     */
    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    /**
     * Restituisce l'azienda di appartenenza.
     * @return L'oggetto {@link AziendaEntity}.
     */
    public AziendaEntity getAzienda() {
        return azienda;
    }

    /**
     * Imposta l'azienda di appartenenza.
     * @param azienda La nuova azienda.
     */
    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }

    /**
     * Restituisce l'email dell'utente.
     * @return L'email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email dell'utente.
     * @param email La nuova email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Restituisce l'hash della password.
     * @return La stringa hashata.
     */
    public String getHash_password() {
        return hash_password;
    }

    /**
     * Imposta l'hash della password.
     * @param hash_password Il nuovo hash.
     */
    public void setHash_password(String hash_password) {
        this.hash_password = hash_password;
    }

    /**
     * Restituisce il nome dell'utente.
     * @return Il nome di battesimo.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'utente.
     * @param nome Il nuovo nome.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     * @return Il cognome.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'utente.
     * @param cognome Il nuovo cognome.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce il numero di telefono.
     * @return La stringa del telefono.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Imposta il numero di telefono.
     * @param telefono Il nuovo numero di telefono.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Restituisce il percorso dell'immagine profilo.
     * @return Il path o l'URL dell'immagine.
     */
    public String getPath_immagine_profilo() {
        return path_immagine_profilo;
    }

    /**
     * Imposta il percorso dell'immagine profilo.
     * @param path_immagine_profilo Il nuovo percorso immagine.
     */
    public void setPath_immagine_profilo(String path_immagine_profilo) {
        this.path_immagine_profilo = path_immagine_profilo;
    }

    /**
     * Restituisce il set delle entità di associazione.
     * <p>
     * Di solito non viene usato direttamente dalla logica di business,
     * ma serve a Hibernate per gestire la persistenza.
     * @return Il Set di {@link AssociazioneEntity}.
     */
    public Set<AssociazioneEntity> getAssociazioni() {
        return associazioni;
    }

    /**
     * Imposta il set delle associazioni.
     * @param associazioni Il nuovo set di associazioni.
     */
    public void setAssociazioni(Set<AssociazioneEntity> associazioni) {
        this.associazioni = associazioni;
    }

    // --- METODI HELPER ---

    /**
     * Metodo di utilità per recuperare direttamente i Ruoli astrattando la tabella intermedia.
     * <p>
     * Itera sulle associazioni e restituisce un Set pulito di {@link RuoloEntity}.
     *
     * @return Un Set contenente i ruoli dell'utente.
     */
    public Set<RuoloEntity> getRuoli() {
        Set<RuoloEntity> ruoli = new HashSet<>();
        if(associazioni != null) {
            for(AssociazioneEntity assoc : associazioni) {
                ruoli.add(assoc.getRuolo());
            }
        }
        return ruoli;
    }

    /**
     * Metodo di utilità per aggiungere un ruolo all'utente.
     * <p>
     * Crea automaticamente una nuova istanza di {@link AssociazioneEntity}
     * che collega questo utente al ruolo specificato e la aggiunge alla collezione.
     *
     * @param ruolo Il ruolo da assegnare.
     */
    public void addRuolo(RuoloEntity ruolo) {
        AssociazioneEntity assoc = new AssociazioneEntity(this, ruolo);
        this.associazioni.add(assoc);
    }
}