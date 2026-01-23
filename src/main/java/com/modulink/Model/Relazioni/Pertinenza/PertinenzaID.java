package com.modulink.Model.Relazioni.Pertinenza;

import java.io.Serializable;
import java.util.Objects;

/**
 * Chiave primaria composta per l'entità {@link PertinenzaEntity}.
 * <p>
 * Identifica univocamente un permesso assegnato a un ruolo per un modulo specifico,
 * all'interno del contesto di una determinata azienda.
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.2
 * @since 1.0.0
 */
public class PertinenzaID implements Serializable {
    /**
     * Identificativo del Ruolo.
     */
    private int id_ruolo;

    /**
     * Identificativo del Modulo.
     */
    private int id_modulo;

    /**
     * Identificativo dell'Azienda.
     */
    private int id_azienda;

    /**
     * Costruttore vuoto.
     *
     * @since 1.0.0
     */
    public PertinenzaID(){}

    /**
     * Costruttore completo per inizializzare la chiave composta.
     *
     * @param id_ruolo   ID Ruolo.
     * @param id_modulo  ID Modulo.
     * @param id_azienda ID Azienda.
     * @since 1.0.0
     */
    public PertinenzaID(int id_ruolo, int id_modulo, int id_azienda) {
        this.id_ruolo = id_ruolo;
        this.id_modulo = id_modulo;
        this.id_azienda = id_azienda;
    }

    /**
     * Verifica uguaglianza.
     *
     * @param o Oggetto da confrontare.
     * @return true se identici.
     * @since 1.0.0
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PertinenzaID that = (PertinenzaID) o;
        return id_ruolo == that.id_ruolo && id_modulo == that.id_modulo && id_azienda == that.id_azienda;
    }

    /**
     * Genera hash code.
     *
     * @return Hash code.
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(id_ruolo, id_modulo, id_azienda);
    }

    /**
     * Recupera ID ruolo.
     *
     * @return ID ruolo.
     * @since 1.0.0
     */
    public int getId_ruolo() {
        return id_ruolo;
    }

    /**
     * Imposta ID ruolo.
     *
     * @param id_ruolo Nuovo ID ruolo.
     * @since 1.0.0
     */
    public void setId_ruolo(int id_ruolo) {
        this.id_ruolo = id_ruolo;
    }

    /**
     * Recupera ID modulo.
     *
     * @return ID modulo.
     * @since 1.0.0
     */
    public int getId_modulo() {
        return id_modulo;
    }

    /**
     * Imposta ID modulo.
     *
     * @param id_modulo Nuovo ID modulo.
     * @since 1.0.0
     */
    public void setId_modulo(int id_modulo) {
        this.id_modulo = id_modulo;
    }

    /**
     * Recupera ID azienda.
     *
     * @return ID azienda.
     * @since 1.0.0
     */
    public int getId_azienda() {
        return id_azienda;
    }

    /**
     * Imposta ID azienda.
     *
     * @param id_azienda Nuovo ID azienda.
     * @since 1.0.0
     */
    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }
}
