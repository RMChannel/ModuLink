package com.modulink.Model.News;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class NewsEntity {
    @Id
    @GeneratedValue(generator = "news_seq")
    private int id;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false)
    private String testo;

    @Column(nullable = false)
    private LocalDate data;

    public NewsEntity() {}

    public NewsEntity(String titolo, String testo, LocalDate data) {
        this.titolo = titolo;
        this.testo = testo;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
