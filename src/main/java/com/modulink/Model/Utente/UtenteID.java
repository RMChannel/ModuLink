package com.modulink.Model.Utente;

import java.io.Serializable;
import java.util.Objects;

/**
 * Rappresenta la chiave primaria composta per l'entità {@link UtenteEntity}.
 * <p>
 * In questa architettura, un utente è univocamente identificato dalla coppia:
 * <ul>
 * <li><strong>ID Azienda:</strong> Il contesto aziendale di appartenenza.</li>
 * <li><strong>ID Utente:</strong> Un numero sequenziale (es. 1, 2, 3...) univoco solo all'interno di quell'azienda.</li>
 * </ul>
 * <p>
 * <strong>Regola di Mapping JPA (@IdClass):</strong><br>
 * I nomi dei campi definiti in questa classe devono corrispondere <em>esattamente</em>
 * ai nomi degli attributi annotati con {@code @Id} nella classe {@link UtenteEntity}.
 * </p>
 * <p>
 * La classe implementa {@link Serializable} come richiesto obbligatoriamente dalle specifiche JPA
 * per le classi utilizzate come identificatori composti.
 * </p>
 *
 * @see UtenteEntity
 * @author Modulink Team
 * @version 1.0.9
 */
public class UtenteID implements Serializable {

    /**
     * Identificativo numerico dell'utente.
     * <p>
     * Corrisponde all'attributo {@code private int id_utente} in {@link UtenteEntity}.
     * Questo valore viene solitamente calcolato manualmente (es. MAX ID + 1) in fase di registrazione
     * per garantire la sequenzialità specifica per azienda.
     * </p>
     */
    private int id_utente;

    /**
     * Identificativo numerico dell'azienda (Foreign Key).
     * <p>
     * <strong>ATTENZIONE:</strong> Il nome di questo campo deve essere "azienda" (e non "id_azienda")
     * perché deve coincidere con il nome dell'attributo di relazione nella Entity:
     * {@code private AziendaEntity azienda;}.
     * </p>
     * <p>
     * JPA si occupa automaticamente di mappare la Primary Key dell'oggetto {@code AziendaEntity}
     * a questo valore intero durante le operazioni di persistenza.
     * </p>
     */
    private int azienda;

    /**
     * Costruttore vuoto predefinito.
     * <p>
     * Richiesto obbligatoriamente dalle specifiche JPA per l'istanziazione tramite reflection
     * (es. durante il caricamento da database).
     * </p>
     */
    public UtenteID() {}

    /**
     * Costruttore parametrico per creare un'istanza della chiave composta.
     * <p>
     * Utile per istanziare chiavi per ricerche `findById` o riferimenti.
     * </p>
     *
     * @param id_utente L'ID sequenziale dell'utente.
     * @param azienda   L'ID dell'azienda di appartenenza.
     */
    public UtenteID(int id_utente, int azienda) {
        this.id_utente = id_utente;
        this.azienda = azienda;
    }

    /**
     * Verifica l'uguaglianza tra due chiavi composte.
     * <p>
     * Fondamentale per il corretto funzionamento di Hibernate quando l'entità
     * viene inserita in Collezioni (es. {@code Set}) o gestita nella Cache di primo livello.
     * Confronta sia l'ID utente che l'ID azienda.
     * </p>
     *
     * @param o L'oggetto da confrontare con l'istanza corrente.
     * @return {@code true} se entrambi i campi (id_utente e azienda) coincidono e l'oggetto è dello stesso tipo.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UtenteID utenteID = (UtenteID) o;
        return id_utente == utenteID.id_utente &&
                azienda == utenteID.azienda;
    }

    /**
     * Genera l'hash code per la chiave composta.
     * <p>
     * Utilizza {@link Objects#hash(Object...)} per combinare i due identificativi,
     * garantendo una distribuzione uniforme e la coerenza con il metodo {@link #equals(Object)}.
     * </p>
     *
     * @return L'hash code calcolato.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id_utente, azienda);
    }

    // --- GETTER E SETTER ---

    /**
     * Restituisce l'ID utente parte della chiave.
     * @return L'intero dell'ID utente.
     */
    public int getId_utente() {
        return id_utente;
    }

    /**
     * Imposta l'ID utente parte della chiave.
     * @param id_utente Il nuovo ID utente.
     */
    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    /**
     * Restituisce l'ID dell'azienda parte della chiave.
     * @return L'intero rappresentante la chiave primaria dell'azienda.
     */
    public int getAzienda() {
        return azienda;
    }

    /**
     * Imposta l'ID dell'azienda parte della chiave.
     * @param azienda Il nuovo ID azienda.
     */
    public void setAzienda(int azienda) {
        this.azienda = azienda;
    }
}