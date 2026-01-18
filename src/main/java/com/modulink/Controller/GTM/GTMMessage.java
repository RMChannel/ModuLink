package com.modulink.Controller.GTM;

import java.io.Serializable;

public class GTMMessage implements Serializable {
    private String nome;
    private String type;
    private int id;
    private int azienda;
    private String color;

    public GTMMessage() {}

    public GTMMessage(String nome, String type, int id, int azienda, String color) {
        this.nome = nome;
        this.type = type;
        this.id = id;
        this.azienda = azienda;
        this.color = color;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAzienda() {
        return azienda;
    }

    public void setAzienda(int azienda) {
        this.azienda = azienda;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "GTMMessage{" +
                "nome='" + nome + '\'' +
                ", type='" + type + '\'' +
                ", id=" + id +
                ", azienda=" + azienda +
                ", color='" + color + '\'' +
                '}';
    }
}
