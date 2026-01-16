package com.modulink.Model.Prodotto;

import com.modulink.Model.Azienda.AziendaEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@IdClass(ProdottoID.class)
@Table(name = "prodotti", schema = "modulink")
public class ProdottoEntity {
    @Id
    @Column(name = "id_prodotto")
    private int id_prodotto;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_azienda", referencedColumnName = "id_azienda", nullable = false, foreignKey = @ForeignKey(name = "FK_Prodotto_Azienda"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AziendaEntity azienda;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "quantita", nullable = false)
    private int quantita;

    @Column(name = "prezzo")
    private double prezzo;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "categoria")
    private String categoria;

    public ProdottoEntity() {}

    public ProdottoEntity(AziendaEntity azienda, String nome, int quantita, double prezzo, String descrizione, String categoria) {
        this.azienda = azienda;
        this.nome = nome;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.categoria = categoria;
    }

    public int getId_prodotto() {
        return id_prodotto;
    }

    public void setId_prodotto(int id_prodotto) {
        this.id_prodotto = id_prodotto;
    }

    public AziendaEntity getAzienda() {
        return azienda;
    }

    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
