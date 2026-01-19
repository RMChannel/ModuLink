package com.modulink.Model.Relazioni.Attivazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository JPA per la gestione dell'entità {@link AttivazioneEntity}.
 * <p>
 * Fornisce metodi per interrogare il database riguardo ai moduli attivi per azienda,
 * identificare moduli disponibili per l'acquisto e verificare lo stato di attivazione.
 * </p>
 *
 * @author Modulink Team
 * @version 2.0.1
 * @since 1.1.0
 */
public interface AttivazioneRepository extends JpaRepository<AttivazioneEntity,AttivazioneID> {
    /**
     * Recupera tutti i moduli visibili che NON sono ancora stati acquistati dall'azienda specificata.
     * Utile per popolare il marketplace o la vetrina acquisti.
     *
     * @param azienda L'azienda per cui verificare la disponibilità.
     * @return Lista di {@link ModuloEntity} acquistabili.
     * @since 1.2.0
     */
    @Query("SELECT m FROM ModuloEntity m WHERE m NOT IN (SELECT a.modulo FROM AttivazioneEntity a WHERE a.azienda = :azienda) AND m.Visible = true")
    public List<ModuloEntity> getAllNotPurchased(@Param("azienda") AziendaEntity azienda);

    /**
     * Recupera tutti i moduli visibili che sono stati già acquistati dall'azienda specificata.
     *
     * @param azienda L'azienda proprietaria.
     * @return Lista di {@link ModuloEntity} già posseduti.
     * @since 1.2.0
     */
    @Query("SELECT m FROM ModuloEntity m WHERE m IN (SELECT a.modulo FROM AttivazioneEntity a WHERE a.azienda = :azienda) AND m.Visible = true")
    public List<ModuloEntity> getAllPurchased(@Param("azienda") AziendaEntity azienda);

    /**
     * Verifica se esiste un'attivazione specifica per una coppia Azienda-Modulo.
     *
     * @param azienda L'azienda da verificare.
     * @param modulo  Il modulo da verificare.
     * @return true se l'attivazione esiste, false altrimenti.
     * @since 1.1.0
     */
    boolean existsByAziendaAndModulo(AziendaEntity azienda, ModuloEntity modulo);
}
