package com.modulink.Model.Azienda;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaccia di persistenza per l'entità {@link AziendaEntity}.
 * <p>
 * Estende {@link JpaRepository} per fornire un'astrazione completa sul livello di accesso ai dati (DAL).
 * Include il supporto nativo per operazioni CRUD, paginazione, ordinamento e flush del contesto di persistenza.
 * </p>
 * <p>
 * I metodi definiti in questa interfaccia beneficiano dell'implementazione proxy dinamica di Spring Data,
 * traducendo le firme dei metodi in query JPQL o SQL ottimizzate al momento del bootstrap dell'applicazione context.
 * </p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.data.repository.CrudRepository
 * @author Modulink Team
 * @version 1.5.2
 * @since 1.0.0
 */
public interface AziendaRepository extends JpaRepository<AziendaEntity, Integer> {

    /**
     * Esegue una query di selezione per individuare un'azienda tramite la Partita IVA.
     * <p>
     * La query generata corrisponderà semanticamente a:
     * <code>SELECT a FROM AziendaEntity a WHERE a.piva = :piva</code>.
     * </p>
     * <p>
     * Poiché la colonna <code>P_IVA</code> è soggetta a vincolo di unicità, il risultato è incapsulato in un {@link Optional}.
     * Questo approccio previene <code>NullPointerException</code> e favorisce uno stile di programmazione funzionale.
     * </p>
     *
     * @param piva La Partita IVA da ricercare (case-sensitive a seconda del collation del DB).
     * @return {@link Optional} contenente l'entity se presente, altrimenti {@link Optional#empty()}.
     * @since 1.0.0
     */
    Optional<AziendaEntity> findByPiva(String piva);

    /**
     * Recupera l'entità associata a uno specifico recapito telefonico.
     * <p>
     * Utilizzato per validazioni di unicità durante i processi di onboarding o modifica anagrafica.
     * Sfrutta l'indice univoco sulla colonna <code>Telefono</code> per garantire performance O(1) o O(log N).
     * </p>
     *
     * @param telefono La stringa del numero telefonico (si raccomanda la normalizzazione E.164 o standard locale prima della query).
     * @return {@link Optional} contenente l'entity trovata.
     * @since 1.2.1
     */
    Optional<AziendaEntity> findByTelefono(String telefono);
}