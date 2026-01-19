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

    /**
     * Recupera l'elenco completo di tutti i ruoli configurati per una specifica azienda.
     * <p>
     * Sfrutta la generazione automatica delle query di Spring Data JPA basata sul nome del metodo.
     * Filtra i risultati applicando una clausola <code>WHERE</code> sulla foreign key dell'azienda.
     * </p>
     *
     * @param azienda L'entità {@link AziendaEntity} di cui si vogliono ottenere i ruoli.
     * @return Una {@link List} di {@link RuoloEntity} associati all'azienda indicata.
     * @since 1.0.0
     */
    List<RuoloEntity> findAllByAzienda(AziendaEntity azienda);

    /**
     * Individua l'identificativo numerico massimo (ID_Ruolo) attualmente in uso per una determinata azienda.
     * <p>
     * Utilizza una query JPQL custom per aggregare i dati. La funzione <code>COALESCE</code> garantisce che,
     * qualora l'azienda non abbia ancora alcun ruolo registrato, venga restituito il valore predefinito <code>0</code>
     * anziché <code>null</code>, prevenendo potenziali errori di unboxing.
     * </p>
     *
     * @param idAzienda L'identificativo primario dell'azienda su cui effettuare la ricerca.
     * @return L'ID numerico massimo trovato, o 0 se non sono presenti record.
     * @since 1.1.0
     */
    @Query("SELECT COALESCE(MAX(r.id_ruolo), 0) FROM RuoloEntity r WHERE r.azienda.id_azienda = :idAzienda")
    int findMaxIdByAzienda(@Param("idAzienda") int idAzienda);
}