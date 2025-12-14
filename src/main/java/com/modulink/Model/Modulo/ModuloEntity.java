package com.modulink.Model.Modulo;

import jakarta.persistence.*;

@Entity
@IdClass(ModuloID.class)
@Table(name = "modulo",schema = "modulink")
public class ModuloEntity {
    @Id
    @Column(name = "id_modulo", nullable = false)
    private int id_modulo;

    @Id
    @Column(name = "id_azienda", nullable = false)
    private int id_azienda;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @Column(name = "url_icona")
    private String url_icona;

    public ModuloEntity(){}

    public ModuloEntity(int id_modulo, int id_azienda, String nome, String descrizione, String url_icona) {
        this.id_modulo = id_modulo;
        this.id_azienda = id_azienda;
        this.nome = nome;
        this.descrizione = descrizione;
        this.url_icona = url_icona;
    }

    public int getId_modulo() {
        return id_modulo;
    }

    public void setId_modulo(int id_modulo) {
        this.id_modulo = id_modulo;
    }

    public int getId_azienda() {
        return id_azienda;
    }

    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUrl_icona() {
        return url_icona;
    }

    public void setUrl_icona(String url_icona) {
        this.url_icona = url_icona;
    }
}
