package com.modulink.Model.Utente;

import com.modulink.Model.Azienda.AziendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository per la gestione della persistenza dell'entità {@link UtenteEntity}.
 * <p>
 * Questa interfaccia estende {@link JpaRepository} per fornire operazioni CRUD standard
 * su database relazionali. Utilizza {@link UtenteID} come tipo della chiave primaria,
 * riflettendo la struttura a chiave composta dell'entità.
 * <p>
 * Spring Data JPA genera automaticamente l'implementazione di questa interfaccia a runtime,
 * riducendo la necessità di scrivere codice boilerplate per l'accesso ai dati.
 *
 * @see UtenteEntity
 * @see UtenteID
 * @author Modulink Team
 * @version 1.1
 */
@Repository
public interface UserRepository extends JpaRepository<UtenteEntity, UtenteID> {

    /**
     * Recupera un'istanza di {@link UtenteEntity} basandosi sull'indirizzo email.
     * <p>
     * Questo è un "Query Method" derivato: Spring Data JPA analizza il nome del metodo
     * e crea automaticamente la query SQL corrispondente (es. <code>SELECT * FROM Utente WHERE Email = ?</code>).
     * Utilizzato principalmente durante le fasi di login e recupero credenziali.
     *
     * @param email L'indirizzo email univoco dell'utente da cercare.
     * @return Un {@link Optional} contenente l'utente se trovato, oppure un Optional vuoto se nessun utente corrisponde all'email fornita.
     */
    Optional<UtenteEntity> findByEmail(String email);

    /**
     * Trova il valore massimo attuale dell'ID Utente all'interno di una specifica azienda.
     * <p>
     * Questo metodo è fondamentale per la strategia di generazione manuale degli ID:
     * permette di conoscere l'ultimo ID assegnato per calcolare il successivo (<code>MAX + 1</code>).
     * <p>
     * La funzione JPQL <code>COALESCE(MAX(u.id_utente), 0)</code> garantisce che, se l'azienda
     * non ha ancora utenti (e quindi MAX restituisce <code>null</code>), il metodo ritorni <code>0</code>
     * invece di lanciare un'eccezione o restituire null.
     *
     * @param idAzienda L'ID dell'azienda di cui si vuole trovare il massimo ID utente corrente.
     * @return L'ID più alto presente (o 0 se l'azienda è vuota).
     */
    @Query("SELECT COALESCE(MAX(u.id_utente), 0) FROM UtenteEntity u WHERE u.azienda.id_azienda = :idAzienda")
    int findMaxIdByAzienda(@Param("idAzienda") int idAzienda);


    Object getAllByAziendaIs(AziendaEntity azienda);
}