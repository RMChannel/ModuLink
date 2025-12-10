package com.modulink.Model.Utente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Utente",schema="modulink")
public class UtenteEntity {
    @Id
    @Column(name="ID_Utente",nullable = false,unique = true)
    private int id_utente;

    @Column(name="ID_Azienda",nullable = false)
    private int id_azienda;

    @Column(name="Email",nullable = false,unique = true)
    private String email;

    @Column(name="Password",nullable = false)
    private String hash_password;

    @Column(name="Nome",nullable = false)
    private String nome;

    @Column(name="Cognome",nullable = false)
    private String cognome;

    @Column(name="Telefono")
    private String telefono;

    @Column(name="Immagine_Profilo")
    private String path_immagine_profilo;

    public UtenteEntity() {}

    public UtenteEntity(int id_utente, int id_azienda, String email, String hash_password, String nome, String cognome, String telefono, String path_immagine_profilo) {
        this.id_utente = id_utente;
        this.id_azienda = id_azienda;
        this.email = email;
        this.hash_password = hash_password;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.path_immagine_profilo = path_immagine_profilo;
    }

    public int getId_utente() {
        return id_utente;
    }

    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    public int getId_azienda() {
        return id_azienda;
    }

    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash_password() {
        return hash_password;
    }

    public void setHash_password(String hash_password) {
        this.hash_password = hash_password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPath_immagine_profilo() {
        return path_immagine_profilo;
    }

    public void setPath_immagine_profilo(String path_immagine_profilo) {
        this.path_immagine_profilo = path_immagine_profilo;
    }
}
