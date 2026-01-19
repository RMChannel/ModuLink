package com.modulink.Model.Prodotto;

import com.modulink.Model.Azienda.AziendaEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Rappresenta l'entità di persistenza per la gestione dei prodotti o articoli di magazzino.
 * <p>
 * Questa classe mappa la tabella <code>prodotti</code> e utilizza una chiave primaria composta definita da {@link ProdottoID},
 * costituita dall'identificativo locale del prodotto e dal riferimento all'azienda proprietaria.
 * Questa strategia consente a diverse aziende (tenant) di avere ID prodotto sequenziali indipendenti (1, 2, 3...)
 * all'interno dello stesso schema database condiviso.
 * </p>
 * <p>
 * La gestione della concorrenza e dell'integrità referenziale verso l'azienda è garantita da vincoli Foreign Key
 * con clausola <code>ON DELETE CASCADE</code>.
 * </p>
 *
 * @see ProdottoID
 * @author Modulink Team
 * @version 2.0.1
 * @since 1.2.0
 */
@Entity
@IdClass(ProdottoID.class)
@Table(name = "prodotti", schema = "modulink")
public class ProdottoEntity {

    /**
     * Identificativo numerico locale del prodotto.
     * <p>
     * Parte della chiave primaria composta. Non è univoco a livello globale, ma solo all'interno
     * del contesto definito dall'attributo {@link #azienda}.
     * </p>
     *
     * @since 1.2.0
     */
    @Id
    @Column(name = "id_prodotto")
    private int id_prodotto;

    /**
     * Riferimento all'azienda proprietaria del catalogo prodotti.
     * <p>
     * Parte della chiave primaria composta (Partition Key).
     * La cancellazione dell'azienda comporta la rimozione a cascata di tutti i suoi prodotti.
     * </p>
     *
     * @since 1.2.0
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "id_azienda", referencedColumnName = "id_azienda", nullable = false, foreignKey = @ForeignKey(name = "FK_Prodotto_Azienda"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AziendaEntity azienda;

    /**
     * Nome commerciale o etichetta identificativa del prodotto.
     *
     * @since 1.2.0
     */
    @Column(name = "nome", nullable = false)
    private String nome;

    /**
     * Giacenza attuale in magazzino.
     * <p>
     * Rappresenta lo stock fisico disponibile.
     * </p>
     *
     * @since 1.2.0
     */
    @Column(name = "quantita", nullable = false)
    private int quantita;

    /**
     * Prezzo unitario di vendita.
     * <p>
     * Memorizzato come valore in virgola mobile a doppia precisione.
     * </p>
     *
     * @since 1.2.0
     */
    @Column(name = "prezzo")
    private double prezzo;

    /**
     * Descrizione estesa delle caratteristiche del prodotto.
     *
     * @since 1.2.0
     */
    @Column(name = "descrizione")
    private String descrizione;

    /**
     * Categoria merceologica di appartenenza.
     * <p>
     * Utilizzata per raggruppamenti logici o filtraggio nei report.
     * </p>
     *
     * @since 1.3.0
     */
    @Column(name = "categoria")
    private String categoria;

    /**
     * Costruttore di default richiesto dalla specifica JPA.
     *
     * @since 1.2.0
     */
    public ProdottoEntity() {}

    /**
     * Costruttore parametrico completo (escluso ID locale).
     * <p>
     * L'ID locale viene solitamente calcolato e assegnato dal Service Layer prima della persistenza.
     * </p>
     *
     * @param azienda     L'azienda proprietaria.
     * @param nome        Nome del prodotto.
     * @param quantita    Quantità iniziale.
     * @param prezzo      Prezzo unitario.
     * @param descrizione Descrizione opzionale.
     * @param categoria   Categoria merceologica.
     * @since 1.2.0
     */
    public ProdottoEntity(AziendaEntity azienda, String nome, int quantita, double prezzo, String descrizione, String categoria) {
        this.azienda = azienda;
        this.nome = nome;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.categoria = categoria;
    }

    /**
     * Recupera l'ID locale del prodotto.
     * @return Intero ID.
     * @since 1.2.0
     */
    public int getId_prodotto() {
        return id_prodotto;
    }

    /**
     * Imposta l'ID locale del prodotto.
     * @param id_prodotto Nuovo ID.
     * @since 1.2.0
     */
    public void setId_prodotto(int id_prodotto) {
        this.id_prodotto = id_prodotto;
    }

    /**
     * Recupera l'azienda proprietaria.
     * @return Entità azienda.
     * @since 1.2.0
     */
    public AziendaEntity getAzienda() {
        return azienda;
    }

    /**
     * Imposta l'azienda proprietaria.
     * @param azienda Nuova azienda.
     * @since 1.2.0
     */
    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }

    /**
     * Recupera il nome del prodotto.
     * @return Stringa nome.
     * @since 1.2.0
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del prodotto.
     * @param nome Nuovo nome.
     * @since 1.2.0
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Recupera la quantità disponibile.
     * @return Intero quantità.
     * @since 1.2.0
     */
    public int getQuantita() {
        return quantita;
    }

    /**
     * Imposta la quantità disponibile.
     * @param quantita Nuova quantità.
     * @since 1.2.0
     */
    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    /**
     * Recupera il prezzo unitario.
     * @return Valore double del prezzo.
     * @since 1.2.0
     */
    public double getPrezzo() {
        return prezzo;
    }

    /**
     * Imposta il prezzo unitario.
     * @param prezzo Nuovo prezzo.
     * @since 1.2.0
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * Recupera la descrizione.
     * @return Stringa descrizione.
     * @since 1.2.0
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione.
     * @param descrizione Nuova descrizione.
     * @since 1.2.0
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Recupera la categoria.
     * @return Stringa categoria.
     * @since 1.3.0
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Imposta la categoria.
     * @param categoria Nuova categoria.
     * @since 1.3.0
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
