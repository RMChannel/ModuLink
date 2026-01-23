package com.modulink.Model.Modulo;

import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interfaccia DAO per l'accesso e la gestione dei dati relativi ai Moduli funzionali.
 * <p>
 * Estende {@link JpaRepository} per fornire operazioni CRUD standard.
 * Include query JPQL specializzate per la risoluzione dei permessi di accesso basati su Ruoli (RBAC)
 * e associazioni Utente-Azienda-Modulo.
 * </p>
 *
 * @author Modulink Team
 * @version 1.4.2
 * @since 1.0.0
 */
public interface ModuloRepository extends JpaRepository<ModuloEntity, Integer> {

    /**
     * Recupera l'elenco dei moduli a cui un determinato utente ha accesso.
     * <p>
     * La query attraversa la catena di relazioni:
     * <code>Pertinenza (Modulo-Ruolo) -> Associazione (Ruolo-Utente)</code>.
     * Restituisce solo i moduli per i quali l'utente possiede un ruolo attivo che ne prevede l'utilizzo (pertinenza).
     * </p>
     *
     * @param utente L'entità utente per cui verificare gli accessi.
     * @return Lista di {@link ModuloEntity} accessibili.
     * @since 1.2.0
     */
    @Query("SELECT DISTINCT aff.attivazione.modulo FROM PertinenzaEntity aff " +
           "JOIN AssociazioneEntity ass ON aff.ruolo = ass.ruolo " +
           "WHERE ass.utente = :utente")
    List<ModuloEntity> findModuliByUtente(@Param("utente") UtenteEntity utente);

    /**
     * Verifica puntuale se uno specifico modulo è accessibile da un utente.
     * <p>
     * Esegue un controllo di intersezione tra i moduli abilitati per i ruoli dell'utente
     * e l'ID del modulo richiesto. Se la lista restituita non è vuota, l'accesso è consentito.
     * </p>
     *
     * @param id     L'ID del modulo da verificare.
     * @param utente L'utente che richiede l'accesso.
     * @return Lista contenente il modulo se accessibile, altrimenti vuota.
     * @since 1.2.5
     */
    @Query("SELECT DISTINCT aff.attivazione.modulo FROM PertinenzaEntity aff " +
            "JOIN AssociazioneEntity ass ON aff.ruolo = ass.ruolo " +
            "WHERE ass.utente = :utente AND aff.id_modulo= :id")
    List<ModuloEntity> isModuloAccessible(@Param("id") int id, @Param("utente") UtenteEntity utente);
}
