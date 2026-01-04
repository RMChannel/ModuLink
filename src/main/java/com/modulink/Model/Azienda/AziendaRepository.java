package com.modulink.Model.Azienda;

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
 * @version 1.2
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

    /**
     * Recupera un'istanza di {@link AziendaEntity} basandosi sul numero di telefono.
     * <p>
     * Metodo utilizzato principalmente in fase di registrazione per verificare che il
     * numero di telefono inserito non sia già associato ad un'altra azienda nel sistema.
     * <p>
     * Nota: Si assume che il parametro passato sia già normalizzato (es. senza spazi)
     * se i dati nel DB sono salvati in tale formato.
     *
     * @param telefono Il numero di telefono da cercare.
     * @return Un {@link Optional} contenente l'azienda se il telefono corrisponde, altrimenti vuoto.
     */
    Optional<AziendaEntity> findByTelefono(String telefono);
}