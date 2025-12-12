package com.modulink.Model.Ruolo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaccia di repository per gestire la persistenza e l'accesso ai dati dell'entità {@link RuoloEntity}.
 * <p>
 * Questa interfaccia estende {@link JpaRepository}, ereditando metodi standard per effettuare:
 * <ul>
 * <li>Operazioni CRUD (Create, Read, Update, Delete) sui ruoli aziendali.</li>
 * <li>Paginazione e ordinamento dei record.</li>
 * <li>Operazioni di batch.</li>
 * </ul>
 * <p>
 * Spring Data JPA fornirà automaticamente l'implementazione di questa interfaccia a runtime.
 * La chiave primaria utilizzata è {@link RuoloID}, il che implica che metodi standard come <code>findById</code>
 * richiederanno un'istanza completa della chiave composta (ID Ruolo + ID Azienda) per identificare univocamente una risorsa.
 *
 * @see RuoloEntity
 * @see RuoloID
 * @see JpaRepository
 * @author Modulink Team
 * @version 1.0
 */
@Repository
public interface RuoloRepository extends JpaRepository<RuoloEntity, RuoloID> {
}