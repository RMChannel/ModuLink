package com.modulink.Model.Relazioni.Associazione;

import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer dedicato alla gestione della logica di business per le associazioni Utente-Ruolo.
 * <p>
 * Questa classe funge da intermediario tra il controller e il repository, garantendo l'integrità
 * delle operazioni di assegnazione e revoca dei ruoli all'interno del sistema Modulink.
 * Tutte le operazioni di modifica sono eseguite all'interno di un contesto transazionale.
 * </p>
 *
 * @author Modulink Team
 * @version 1.2.0
 * @since 1.1.0
 */
@Service
public class AssociazioneService {
    /**
     * Repository per l'accesso ai dati delle associazioni.
     */
    private final AssociazioneRepository associazioneRepository;

    /**
     * Costruttore per l'iniezione della dipendenza repository.
     *
     * @param associazioneRepository Il repository delle associazioni.
     * @since 1.1.0
     */
    public AssociazioneService(AssociazioneRepository associazioneRepository) {
        this.associazioneRepository = associazioneRepository;
    }

    /**
     * Persiste una nuova associazione nel database.
     *
     * @param associazioneEntity L'entità da salvare.
     * @since 1.1.0
     */
    @Transactional
    public void save(AssociazioneEntity associazioneEntity) {
        associazioneRepository.save(associazioneEntity);
    }

    /**
     * Rimuove globalmente tutte le associazioni legate a un determinato ruolo.
     * Operazione critica utilizzata per la pulizia delle relazioni prima dell'eliminazione di un ruolo.
     *
     * @param ruolo Il ruolo per il quale revocare tutte le assegnazioni.
     * @since 1.1.0
     */
    @Transactional
    public void removeAllAssociazioni(RuoloEntity ruolo) {
        associazioneRepository.removeAllByRuolo(ruolo);
    }

    /**
     * Recupera l'elenco di tutti gli utenti che possiedono un determinato ruolo.
     * <p>
     * Esegue una trasformazione dello stream delle associazioni per restituire direttamente
     * gli oggetti {@link UtenteEntity}.
     * </p>
     *
     * @param ruolo Il ruolo di ricerca.
     * @return Lista di utenti associati al ruolo.
     * @since 1.1.5
     */
    public List<UtenteEntity> getAllByRole(RuoloEntity ruolo) {
        List<AssociazioneEntity> associazioni = associazioneRepository.findByRuolo(ruolo);
        return associazioni.stream().map(AssociazioneEntity::getUtente).toList();
    }
}
