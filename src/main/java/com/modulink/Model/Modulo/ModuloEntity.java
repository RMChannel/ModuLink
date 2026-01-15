package com.modulink.Model.Modulo;

import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "modulo",schema = "modulink")
public class ModuloEntity {
    @Id
    @Column(name = "id_modulo", nullable = false)
    private int id_modulo;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @Column(name = "url_modulo", nullable = false)
    private String url_modulo;

    @Column(name = "url_icona")
    private String url_icona;

    private boolean Visible;

    @OneToMany(mappedBy = "id_modulo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AffiliazioneEntity> affiliazioni = new HashSet<>();


    public ModuloEntity(){}

    public ModuloEntity(int id_modulo, String nome, String descrizione, String url_modulo, String url_icona, boolean Visible) {
        this.id_modulo = id_modulo;
        this.nome = nome;
        this.descrizione = descrizione;
        this.url_modulo = url_modulo;
        this.url_icona = url_icona;
        this.Visible = Visible;
    }

    public int getId_modulo() {
        return id_modulo;
    }

    public void setId_modulo(int id_modulo) {
        this.id_modulo = id_modulo;
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

    public String getUrl_modulo() {
        return url_modulo;
    }

    public void setUrl_modulo(String url_modulo) {
        this.url_modulo = url_modulo;
    }

    public String getUrl_icona() {
        return url_icona;
    }

    public void setUrl_icona(String url_icona) {
        this.url_icona = url_icona;
    }

    public Set<AffiliazioneEntity> getAffiliazioni() {
        return affiliazioni;
    }

    public void setAffiliazioni(Set<AffiliazioneEntity> affiliazioni) {
        this.affiliazioni = affiliazioni;
    }

    public boolean isVisible() {
        return Visible;
    }

    public void setVisible(boolean visible) {
        Visible = visible;
    }

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
