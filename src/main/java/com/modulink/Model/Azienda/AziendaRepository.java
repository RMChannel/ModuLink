package com.modulink.Model.Azienda;

import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository per l'accesso e la gestione delle entità {@link AziendaEntity}.
 * <p>
 * Estende {@link JpaRepository}, fornendo automaticamente i metodi CRUD
 * standard (Create, Read, Update, Delete) e funzionalità di Paging and Sorting
 * per l'entità {@code AziendaEntity}.
 * <p>
 * Questa interfaccia funge da componente DAO (Data Access Object) principale per
 * interagire con la tabella delle aziende nel database.
 *
 * @see AziendaEntity
 * @author Modulink Team
 * @version 1.0
 */
public interface AziendaRepository extends JpaRepository<AziendaEntity, Integer> {

    /**
     * Recupera un'istanza di {@link AziendaEntity} basandosi sulla Partita IVA.
     * <p>
     * Questo è un "Query Method" derivato: Spring Data JPA analizza il nome del metodo
     * e crea automaticamente la query SQL corrispondente (es. {@code SELECT * FROM Azienda WHERE Piva = ?}).
     * La Partita IVA è un identificativo univoco fiscale per l'azienda.
     *
     * @param piva La stringa rappresentante la Partita IVA da cercare.
     * @return Un {@link Optional} contenente l'azienda se trovata, oppure un Optional vuoto se nessuna corrispondenza è stata trovata.
     */
    Optional<AziendaEntity> findByPiva(String piva);
}