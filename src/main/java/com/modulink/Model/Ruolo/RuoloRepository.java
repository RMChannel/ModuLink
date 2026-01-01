package com.modulink.Model.Ruolo;

import com.modulink.Model.Azienda.AziendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    List<RuoloEntity> findAllByAzienda(AziendaEntity azienda);

    @Query("SELECT COALESCE(MAX(r.id_ruolo), 0) FROM RuoloEntity r WHERE r.azienda.id_azienda = :idAzienda")
    int findMaxIdByAzienda(@Param("idAzienda") int idAzienda);
}