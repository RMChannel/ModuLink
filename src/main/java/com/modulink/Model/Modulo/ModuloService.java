package com.modulink.Model.Modulo;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Pertinenza.PertinenzaEntity;
import com.modulink.Model.Relazioni.Pertinenza.PertinenzaService;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneID;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneService;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Layer responsabile della logica di business e della gestione dell'accesso ai Moduli.
 * <p>
 * Questa classe orchestra le operazioni relative ai moduli funzionali del sistema, integrando
 * controlli di sicurezza (RBAC), gestione della cache per performance e sincronizzazione delle
 * relazioni di pertinenza tra Aziende e Ruoli.
 * </p>
 * <p>
 * Fa ampio uso delle astrazioni di Caching di Spring ({@link Cacheable}, {@link CacheEvict})
 * per ridurre il carico sul database durante i frequenti controlli di autorizzazione.
 * </p>
 *
 * @author Modulink Team
 * @version 1.5.0
 * @since 1.0.0
 */
@Service
public class ModuloService {
    private final ModuloRepository moduloRepository;
    private final PertinenzaService pertinenzaService;
    private final AttivazioneService attivazioneService;

    /**
     * Costruttore per Dependency Injection.
     *
     * @param moduloRepository   Repository principale Moduli.
     * @param pertinenzaService  Service per gestione pertinenze.
     * @param attivazioneService Service per gestione attivazioni.
     * @since 1.0.0
     */
    public ModuloService(ModuloRepository moduloRepository, PertinenzaService pertinenzaService, AttivazioneService attivazioneService) {
        this.moduloRepository = moduloRepository;
        this.pertinenzaService = pertinenzaService;
        this.attivazioneService = attivazioneService;
    }

    /**
     * Recupera la lista dei moduli accessibili da un utente, con supporto caching.
     * <p>
     * Il risultato viene memorizzato nella cache <code>moduliByUtente</code> indicizzata dall'email utente.
     * Questo evita di rieseguire la complessa query di join ad ogni visualizzazione della dashboard.
     * </p>
     *
     * @param utente L'utente richiedente.
     * @return Lista di moduli abilitati.
     * @since 1.2.0
     */
    @Cacheable(value = "moduliByUtente", key = "#utente.email")
    public List<ModuloEntity> findModuliByUtente(UtenteEntity utente) {
        return moduloRepository.findModuliByUtente(utente);
    }

    /**
     * Verifica l'accessibilità puntuale di un modulo, con caching.
     * <p>
     * Metodo ad alta frequenza di chiamata (es. nei filtri di sicurezza web).
     * Il risultato booleano è cachato in <code>moduloAccess</code> con chiave composta {idModulo, email}.
     * </p>
     *
     * @param id     ID del modulo.
     * @param utente L'utente da verificare.
     * @return <code>true</code> se l'accesso è consentito, <code>false</code> altrimenti.
     * @since 1.2.5
     */
    @Cacheable(value = "moduloAccess", key = "{#id, #utente.email}")
    public boolean isAccessibleModulo(int id, UtenteEntity utente) {
        return !moduloRepository.isModuloAccessible(id, utente).isEmpty();
    }

    /**
     * Recupera un modulo per ID, con caching.
     * <p>
     * Poiché i metadati dei moduli (nome, descrizione, url) cambiano raramente,
     * il caching riduce significativamente gli accessi al DB.
     * </p>
     *
     * @param idModulo ID del modulo.
     * @return L'entità modulo.
     * @throws IllegalArgumentException se l'ID non corrisponde a nessun modulo.
     * @since 1.0.0
     */
    @Cacheable(value = "modulo", key = "#idModulo")
    public ModuloEntity getModuloById(int idModulo) {
        return moduloRepository.findById(idModulo).orElseThrow(() -> new IllegalArgumentException("Modulo non trovato"));
    }


    /**
     * Aggiorna le associazioni (Pertinenze) tra un Modulo e i Ruoli di un'Azienda.
     * <p>
     * Questo metodo implementa una logica differenziale (Sync):
     * 1. Identifica le associazioni esistenti.
     * 2. Rimuove quelle non più presenti nella lista <code>ruoli</code> target.
     * 3. Aggiunge le nuove associazioni mancanti.
     * </p>
     * <p>
     * <strong>Gestione Cache:</strong> Essendo un'operazione che modifica i permessi, annotata con {@link CacheEvict}
     * per invalidare tutte le entry delle cache correlate (<code>moduliByUtente</code>, <code>moduloAccess</code>, <code>modulo</code>),
     * forzando il ricalcolo dei permessi al prossimo accesso.
     * </p>
     *
     * @param azienda   L'azienda contesto dell'operazione.
     * @param idModulo  L'ID del modulo da configurare.
     * @param ruoli     La nuova lista completa dei ruoli abilitati per quel modulo in quell'azienda.
     * @since 1.5.0
     */
    @Transactional
    @CacheEvict(value = {"moduliByUtente", "moduloAccess", "modulo"}, allEntries = true)
    public void updateModuloAffiliations(AziendaEntity azienda, int idModulo, List<RuoloEntity> ruoli) {
        ModuloEntity modulo = getModuloById(idModulo);

        List<PertinenzaEntity> existingAffiliations = pertinenzaService.findAllByAttivazione(new AttivazioneEntity(modulo, azienda));

        Set<Integer> existingRoleIds = existingAffiliations.stream()
                .map(PertinenzaEntity::getId_ruolo)
                .collect(Collectors.toSet());

        Set<Integer> targetRoleIds = ruoli.stream()
                .map(RuoloEntity::getId_ruolo)
                .collect(Collectors.toSet());

        List<PertinenzaEntity> toRemove = existingAffiliations.stream()
                .filter(a -> !targetRoleIds.contains(a.getId_ruolo()))
                .collect(Collectors.toList());

        modulo.getAffiliazioni().removeAll(toRemove);

        for (RuoloEntity ruolo : ruoli) {
            if (!existingRoleIds.contains(ruolo.getId_ruolo())) {
                PertinenzaEntity affiliazione = new PertinenzaEntity(ruolo.getId_ruolo(), idModulo, azienda.getId_azienda());
                affiliazione.setRuolo(ruolo);
                affiliazione.setAttivazione(attivazioneService.getAttivazioneById(new AttivazioneID(modulo, azienda)));
                modulo.getAffiliazioni().add(affiliazione);
            }
        }
        moduloRepository.save(modulo);
    }
}
