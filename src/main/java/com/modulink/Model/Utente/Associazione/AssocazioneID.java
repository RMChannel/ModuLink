package com.modulink.Model.Utente.Associazione;

import java.io.Serializable;
import java.util.Objects;

/**
 * Rappresenta la chiave primaria composta per l'entità di unione {@link AssociazioneEntity}.
 * <p>
 * Questa classe definisce l'identità univoca di un'assegnazione di ruolo.
 * L'identità è formata dalla tripla:
 * <ul>
 * <li><strong>Utente:</strong> Chi possiede il ruolo.</li>
 * <li><strong>Ruolo:</strong> Quale mansione svolge.</li>
 * <li><strong>Azienda:</strong> Il contesto condiviso (l'utente e il ruolo devono appartenere alla stessa azienda).</li>
 * </ul>
 * <p>
 * È necessaria per il funzionamento di {@link jakarta.persistence.IdClass} su {@link AssociazioneEntity}.
 * I nomi dei campi qui definiti devono corrispondere esattamente ai nomi degli attributi
 * annotati con {@code @Id} nella classe Entity corrispondente.
 *
 * @see AssociazioneEntity
 * @author Modulink Team
 * @version 1.0
 */
public class AssocazioneID implements Serializable {

    /**
     * Parte della FK verso Utente.
     * Identifica lo specifico utente all'interno dell'azienda.
     * Deve coincidere con il nome dell'attributo mappato nella relazione {@code @ManyToOne} verso Utente.
     */
    private int id_utente;

    /**
     * Parte della FK verso Ruolo.
     * Identifica lo specifico ruolo all'interno dell'azienda.
     * Deve coincidere con il nome dell'attributo mappato nella relazione {@code @ManyToOne} verso Ruolo.
     */
    private int id_ruolo;

    /**
     * Contesto Aziendale condiviso.
     * <p>
     * Questo campo è fondamentale: fa parte sia della chiave esterna verso {@code Utente},
     * sia della chiave esterna verso {@code Ruolo}. Garantisce l'integrità referenziale
     * impedendo di associare un utente dell'Azienda A a un ruolo dell'Azienda B.
     * <p>
     * In JPA, questo singolo campo mappa la colonna "ID_Azienda" che è condivisa tra le due
     * relazioni {@code @MapsId} o {@code @JoinColumn} parziali.
     */
    private int id_azienda;

    /**
     * Costruttore vuoto.
     * Richiesto da JPA per la creazione dell'istanza tramite reflection.
     */
    public AssocazioneID() {}

    /**
     * Costruttore completo.
     *
     * @param id_utente  L'ID dell'utente.
     * @param id_ruolo   L'ID del ruolo.
     * @param id_azienda L'ID dell'azienda condivisa.
     */
    public AssocazioneID(int id_utente, int id_ruolo, int id_azienda) {
        this.id_utente = id_utente;
        this.id_ruolo = id_ruolo;
        this.id_azienda = id_azienda;
    }

    /**
     * Verifica l'uguaglianza tra due chiavi composte.
     * Tutti e tre i campi (Utente, Ruolo, Azienda) devono coincidere.
     * <p>
     * Essenziale per il corretto funzionamento in {@code Set} e cache di Hibernate.
     *
     * @param o L'oggetto da confrontare.
     * @return true se le chiavi sono identiche.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssocazioneID that = (AssocazioneID) o;
        return id_utente == that.id_utente &&
                id_ruolo == that.id_ruolo &&
                id_azienda == that.id_azienda;
    }

    /**
     * Genera l'hash code basato sui tre identificativi.
     *
     * @return L'hash code calcolato.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id_utente, id_ruolo, id_azienda);
    }

    // --- GETTER E SETTER (Aggiunti per completezza) ---

    public int getId_utente() {
        return id_utente;
    }

    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    public int getId_ruolo() {
        return id_ruolo;
    }

    public void setId_ruolo(int id_ruolo) {
        this.id_ruolo = id_ruolo;
    }

    public int getId_azienda() {
        return id_azienda;
    }

    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }
}