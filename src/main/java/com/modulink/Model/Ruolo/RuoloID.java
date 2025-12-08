package com.modulink.Model.Ruolo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe che rappresenta la chiave primaria composta per l'entità {@link RuoloEntity}.
 * <p>
 * In JPA, una chiave composta richiede una classe dedicata annotata o referenziata da {@code @IdClass}.
 * Questa classe deve obbligatoriamente implementare l'interfaccia {@link Serializable}
 * e sovrascrivere i metodi {@code equals} e {@code hashCode} per garantire
 * l'unicità e il corretto funzionamento nella mappa di identità di Hibernate.
 * <p>
 * L'identità di un ruolo è definita non solo dal suo ID numerico, ma anche dall'azienda
 * a cui appartiene, permettendo a diverse aziende di avere ruoli con lo stesso ID locale.
 *
 * @see RuoloEntity
 * @author Modulink Team
 * @version 1.0
 */
public class RuoloID implements Serializable {

    /**
     * Identificativo numerico del ruolo.
     * Corrisponde al campo {@code id_ruolo} definito nell'entità.
     */
    private int id_ruolo;

    /**
     * Identificativo numerico dell'azienda.
     * <p>
     * Corrisponde alla foreign key verso l'entità Azienda.
     * Il nome del campo deve coincidere con il nome dell'attributo di relazione {@code @ManyToOne}
     * presente nella classe Entity.
     */
    private int azienda;

    /**
     * Costruttore vuoto predefinito.
     * <p>
     * Necessario per la creazione dell'istanza tramite reflection da parte del provider JPA.
     */
    public RuoloID(){}

    /**
     * Costruttore parametrico per istanziare una chiave specifica.
     *
     * @param id_ruolo   L'ID del ruolo.
     * @param azienda L'ID dell'azienda associata.
     */
    public RuoloID(int id_ruolo, int azienda) {
        this.id_ruolo = id_ruolo;
        this.azienda = azienda;
    }

    /**
     * Verifica l'uguaglianza tra due chiavi composte.
     * <p>
     * Due oggetti {@code RuoloID} sono considerati identici solo se entrambi
     * i campi {@code id_ruolo} e {@code azienda} coincidono.
     *
     * @param o L'oggetto con cui confrontare l'istanza corrente.
     * @return {@code true} se gli oggetti rappresentano la stessa chiave primaria, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RuoloID ruoloID = (RuoloID) o;
        return id_ruolo == ruoloID.id_ruolo && azienda == ruoloID.azienda;
    }

    /**
     * Genera l'hash code per la chiave composta.
     * <p>
     * Combina gli hash di {@code id_ruolo} e {@code azienda} per ottenere un identificativo
     * efficiente per l'utilizzo in strutture dati basate su hash (es. HashMap, HashSet).
     *
     * @return L'intero rappresentante l'hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id_ruolo, azienda);
    }

    // Getter e Setter (Opzionali per JPA ma utili per manipolazione manuale)

    public int getId_ruolo() {
        return id_ruolo;
    }

    public void setId_ruolo(int id_ruolo) {
        this.id_ruolo = id_ruolo;
    }

    public int getAzienda() {
        return azienda;
    }

    public void setAzienda(int azienda) {
        this.azienda = azienda;
    }
}