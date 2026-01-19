package com.modulink.Model.Relazioni.Attivazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;

import java.util.Objects;

/**
 * Classe che definisce la chiave primaria composta per l'entità {@link AttivazioneEntity}.
 * <p>
 * Identifica univocamente un record nella tabella di attivazione combinando le chiavi esterne
 * del Modulo e dell'Azienda. Necessaria per la corretta gestione delle entità JPA con chiavi composte.
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.0
 * @since 1.0.0
 */
public class AttivazioneID {
    /**
     * Riferimento all'entità Modulo (parte della chiave).
     * Il nome del campo deve corrispondere all'attributo nella Entity principale.
     */
    private ModuloEntity modulo;

    /**
     * Riferimento all'entità Azienda (parte della chiave).
     * Il nome del campo deve corrispondere all'attributo nella Entity principale.
     */
    private AziendaEntity azienda;

    /**
     * Costruttore vuoto richiesto per la serializzazione e reflection.
     *
     * @since 1.0.0
     */
    public AttivazioneID() {}

    /**
     * Costruttore completo per inizializzare la chiave composta.
     *
     * @param modulo  Il modulo attivato.
     * @param azienda L'azienda proprietaria dell'attivazione.
     * @since 1.0.0
     */
    public AttivazioneID(ModuloEntity modulo, AziendaEntity azienda) {
        this.modulo = modulo;
        this.azienda = azienda;
    }

    /**
     * Recupera il modulo parte della chiave.
     *
     * @return L'oggetto modulo.
     * @since 1.0.0
     */
    public ModuloEntity getModulo() {
        return modulo;
    }

    /**
     * Imposta il modulo parte della chiave.
     *
     * @param modulo Il nuovo modulo.
     * @since 1.0.0
     */
    public void setModulo(ModuloEntity modulo) {
        this.modulo = modulo;
    }

    /**
     * Recupera l'azienda parte della chiave.
     *
     * @return L'oggetto azienda.
     * @since 1.0.0
     */
    public AziendaEntity getAzienda() {
        return azienda;
    }

    /**
     * Imposta l'azienda parte della chiave.
     *
     * @param azienda La nuova azienda.
     * @since 1.0.0
     */
    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }

    /**
     * Verifica l'uguaglianza tra due chiavi composte.
     *
     * @param o L'oggetto da confrontare.
     * @return true se Modulo e Azienda coincidono, false altrimenti.
     * @since 1.0.0
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AttivazioneID that = (AttivazioneID) o;
        return Objects.equals(modulo, that.modulo) && Objects.equals(azienda, that.azienda);
    }

    /**
     * Calcola l'hash code basato sui componenti della chiave.
     *
     * @return L'hash code calcolato.
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(modulo, azienda);
    }
}
