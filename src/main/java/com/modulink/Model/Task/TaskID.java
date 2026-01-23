package com.modulink.Model.Task;

import java.util.Objects;

/**
 * Classe che rappresenta la chiave primaria composta per l'entità {@link TaskEntity}.
 * <p>
 * Questa classe è utilizzata per identificare univocamente un task all'interno
 * del database, combinando l'ID del task e l'ID dell'azienda di appartenenza.
 * È necessaria per il mapping JPA con {@code @IdClass}.
 * </p>
 *
 * @author Modulink Team
 * @version 1.2.9
 */
public class TaskID {
    /**
     * Identificativo numerico del task.
     */
    private int id_task;

    /**
     * Identificativo numerico dell'azienda associata al task.
     */
    private int azienda;

    /**
     * Costruttore vuoto di default.
     */
    public TaskID(){}

    /**
     * Costruisce un'istanza della chiave composta con gli ID specificati.
     *
     * @param id_task L'ID del task.
     * @param azienda L'ID dell'azienda.
     */
    public TaskID(int id_task, int azienda) {
        this.id_task = id_task;
        this.azienda = azienda;
    }

    /**
     * Restituisce l'ID del task.
     *
     * @return L'identificativo del task.
     */
    public int getId_task() {
        return id_task;
    }

    /**
     * Imposta l'ID del task.
     *
     * @param id_task Il nuovo identificativo del task.
     */
    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    /**
     * Restituisce l'ID dell'azienda.
     *
     * @return L'identificativo dell'azienda.
     */
    public int getAzienda() {
        return azienda;
    }

    /**
     * Imposta l'ID dell'azienda.
     *
     * @param azienda Il nuovo identificativo dell'azienda.
     */
    public void setAzienda(int azienda) {
        this.azienda = azienda;
    }

    /**
     * Verifica l'uguaglianza tra questo oggetto e un altro oggetto.
     * Due oggetti TaskID sono considerati uguali se hanno lo stesso id_task e lo stesso id azienda.
     *
     * @param o L'oggetto da confrontare.
     * @return true se gli oggetti sono uguali, false altrimenti.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskID taskID = (TaskID) o;
        return id_task == taskID.id_task && azienda == taskID.azienda;
    }

    /**
     * Calcola l'hash code per questa chiave composta.
     *
     * @return Il valore di hash basato su id_task e azienda.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id_task, azienda);
    }
}
