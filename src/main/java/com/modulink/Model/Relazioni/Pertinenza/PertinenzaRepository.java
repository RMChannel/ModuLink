package com.modulink.Model.Relazioni.Pertinenza;

import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository JPA per gestire i permessi di accesso (Pertinenze).
 *
 * @author Modulink Team
 * @version 2.1.0
 * @since 1.1.0
 */
public interface PertinenzaRepository extends JpaRepository<PertinenzaEntity, PertinenzaID> {

    /**
     * Trova tutti i moduli accessibili per un dato ruolo.
     * Naviga la relazione {@code Pertinenza -> Attivazione -> Modulo}.
     *
     * @param ruolo Il ruolo per cui cercare i permessi.
     * @return Lista di moduli accessibili.
     * @since 1.1.0
     */
    @Query("SELECT a.attivazione.modulo FROM PertinenzaEntity a WHERE a.ruolo = :ruolo")
    List<ModuloEntity> findModuliByRuolo(@Param("ruolo") RuoloEntity ruolo);

    /**
     * Trova i moduli accessibili dato l'ID del ruolo e dell'azienda.
     * Versione ottimizzata che evita il caricamento dell'entità Ruolo completa.
     *
     * @param idRuolo   ID del ruolo.
     * @param idAzienda ID dell'azienda.
     * @return Lista di moduli accessibili.
     * @since 1.1.5
     */
    @Query("SELECT a.attivazione.modulo FROM PertinenzaEntity a WHERE a.id_ruolo = :idRuolo AND a.id_azienda = :idAzienda")
    List<ModuloEntity> findModuliByRuoloId(@Param("idRuolo") int idRuolo, @Param("idAzienda") int idAzienda);

    /**
     * Recupera tutte le pertinenze legate a una specifica attivazione di un modulo.
     * Utile quando un modulo viene disattivato per trovare tutti i permessi da revocare.
     *
     * @param attivazione L'attivazione del modulo.
     * @return Lista di pertinenze.
     * @since 1.2.0
     */
    List<PertinenzaEntity> findAllByAttivazione(AttivazioneEntity attivazione);
}
