package com.modulink.Model.Relazioni.Attivazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaRepository;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import com.modulink.Model.Relazioni.Pertinenza.PertinenzaEntity;
import com.modulink.Model.Relazioni.Pertinenza.PertinenzaRepository;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Layer responsabile della gestione del ciclo di vita delle attivazioni dei moduli.
 * <p>
 * Gestisce l'attivazione predefinita per i nuovi utenti, l'acquisto di nuovi moduli (con relativa assegnazione
 * dei permessi al responsabile) e la dismissione (vendita) dei moduli con pulizia delle dipendenze.
 * Integra la gestione della cache per garantire performance ottimali.
 * </p>
 *
 * @author Modulink Team
 * @version 3.1.5
 * @since 1.2.0
 */
@Service
public class AttivazioneService {

    private final AttivazioneRepository attivazioneRepository;
    private final ModuloRepository moduloRepository;
    private final PertinenzaRepository pertinenzaRepository;
    private final RuoloService ruoloService;
    private final AziendaRepository aziendaRepository;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param attivazioneRepository Repository delle attivazioni.
     * @param moduloRepository      Repository dei moduli.
     * @param pertinenzaRepository  Repository delle pertinenze (permessi).
     * @param ruoloService          Service per la gestione ruoli.
     * @param aziendaRepository     Repository delle aziende.
     * @since 1.2.0
     */
    public AttivazioneService(AttivazioneRepository attivazioneRepository, ModuloRepository moduloRepository, PertinenzaRepository pertinenzaRepository, RuoloService ruoloService, AziendaRepository aziendaRepository) {
        this.attivazioneRepository=attivazioneRepository;
        this.moduloRepository=moduloRepository;
        this.pertinenzaRepository = pertinenzaRepository;
        this.ruoloService=ruoloService;
        this.aziendaRepository=aziendaRepository;

    }

    /**
     * Recupera un'attivazione specifica tramite il suo ID composto.
     *
     * @param attivazioneID L'ID composto dell'attivazione.
     * @return L'entità {@link AttivazioneEntity} trovata.
     * @throws IllegalArgumentException se l'attivazione non viene trovata.
     * @since 1.2.0
     */
    public AttivazioneEntity getAttivazioneById(AttivazioneID attivazioneID) {
        return attivazioneRepository.findById(attivazioneID).orElseThrow(() -> new IllegalArgumentException("L'attivazione non è stata trovata"));
    }

    /**
     * Esegue l'attivazione dei moduli predefiniti (Core) per una nuova azienda.
     * <p>
     * Viene chiamata tipicamente durante la fase di registrazione di un nuovo tenant.
     * I moduli con ID 0, 1, 2, 3 e 9999 vengono assegnati automaticamente.
     * </p>
     *
     * @param aziendaEntity L'azienda appena creata.
     * @since 1.2.0
     */
    @Transactional
    public void attivazioneDefault(AziendaEntity aziendaEntity) {
        List<AttivazioneEntity> attivazioni=new ArrayList<>();

        attivazioni.add(new AttivazioneEntity(moduloRepository.getReferenceById(0),aziendaEntity));
        attivazioni.add(new AttivazioneEntity(moduloRepository.getReferenceById(1),aziendaEntity));
        attivazioni.add(new AttivazioneEntity(moduloRepository.getReferenceById(2),aziendaEntity));
        attivazioni.add(new AttivazioneEntity(moduloRepository.getReferenceById(3),aziendaEntity));
        attivazioni.add(new AttivazioneEntity(moduloRepository.getReferenceById(9999),aziendaEntity));

        attivazioneRepository.saveAll(attivazioni);
    }

    /**
     * Restituisce la lista dei moduli disponibili per l'acquisto da parte di un'azienda.
     *
     * @param aziendaEntity L'azienda cliente.
     * @return Lista di moduli non ancora attivi.
     * @since 1.3.0
     */
    public List<ModuloEntity> getNotPurchased(AziendaEntity aziendaEntity) {
        if (aziendaEntity == null) {
            return new ArrayList<>();
        }
        return attivazioneRepository.getAllNotPurchased(aziendaEntity);
    }

    /**
     * Restituisce la lista dei moduli "Premium" acquistati dall'azienda (escludendo quelli core).
     *
     * @param aziendaEntity L'azienda cliente.
     * @return Lista di moduli acquistati (filtrata dai moduli di sistema).
     * @since 1.3.0
     */
    public List<ModuloEntity> getAllPurchased(AziendaEntity aziendaEntity) {
        if (aziendaEntity == null) {
            return new ArrayList<>();
        }
        List<ModuloEntity> moduli = attivazioneRepository.getAllPurchased(aziendaEntity);
        moduli.removeIf(m -> m.getId_modulo() == 0 || m.getId_modulo() == 1 || m.getId_modulo() == 2 || m.getId_modulo() == 3 || m.getId_modulo() == 9999);
        return moduli;
    }

    /**
     * Gestisce la logica di acquisto di un nuovo modulo.
     * <p>
     * Esegue i seguenti passaggi in transazione:
     * <ol>
     *     <li>Verifica l'esistenza e la visibilità del modulo.</li>
     *     <li>Controlla che non sia già attivo.</li>
     *     <li>Crea e salva l'entità {@link AttivazioneEntity}.</li>
     *     <li>Assegna automaticamente i permessi di gestione (Pertinenza) al Ruolo Responsabile dell'azienda.</li>
     * </ol>
     * Invalida le cache relative ai moduli utente.
     *
     *
     * @param azienda   L'azienda acquirente.
     * @param moduloId  L'ID del modulo da acquistare.
     * @return true se l'operazione ha successo, false altrimenti.
     * @since 2.0.0
     */
    @Transactional
    @CacheEvict(value = {"moduliByUtente", "moduloAccess", "modulo"}, allEntries = true)
    public boolean purchaseModulo(AziendaEntity azienda, int moduloId) {
        if (azienda == null) {
            return false;
        }

        Optional<AziendaEntity> managedAziendaOpt = aziendaRepository.findById(azienda.getId_azienda());
        if (managedAziendaOpt.isEmpty()) {
            return false;
        }
        AziendaEntity managedAzienda = managedAziendaOpt.get();

        ModuloEntity modulo = moduloRepository.findById(moduloId).orElse(null);
        if (modulo == null) {
            return false;
        }

        //Controllo se il modulo è disponibile per tutti, in caso contrario ritorna false
        if (!modulo.isVisible()) return false;
        // Check if already purchased
        else if (attivazioneRepository.existsByAziendaAndModulo(managedAzienda, modulo)) {
            return false;
        }

        // 1. Create and Save Attivazione
        AttivazioneEntity nuovaAttivazione = new AttivazioneEntity(modulo, managedAzienda);
        attivazioneRepository.save(nuovaAttivazione);

        // Ensure Attivazione is written to DB before Affiliazione
        attivazioneRepository.flush();

        // 2. Assign Responsabile Role for this Module
        RuoloEntity resp = ruoloService.getResponsabile(managedAzienda);
        if (resp != null) {
            PertinenzaEntity pertinenzaEntity = new PertinenzaEntity(
                    resp.getId_ruolo(),
                    modulo.getId_modulo(),
                    managedAzienda.getId_azienda()
            );
            pertinenzaRepository.save(pertinenzaEntity);
        }

        return true;
    }

    /**
     * Gestisce la dismissione (vendita/rimozione) di un modulo.
     * <p>
     * Esegue una pulizia profonda per mantenere la coerenza dei dati:
     * <ol>
     *     <li>Verifica che il modulo sia effettivamente attivo.</li>
     *     <li>Rimuove tutte le {@link PertinenzaEntity} (permessi) associate a quel modulo per quell'azienda.</li>
     *     <li>Rimuove l'attivazione stessa.</li>
     * </ol>
     * Gestisce con attenzione le collezioni Hibernate per evitare {@code TransientObjectException}.
     *
     *
     * @param azienda   L'azienda che dismette il modulo.
     * @param moduloId  L'ID del modulo da rimuovere.
     * @return true se l'operazione ha successo, false altrimenti.
     * @since 2.1.0
     */
    @Transactional
    @CacheEvict(value = {"moduliByUtente", "moduloAccess", "modulo"}, allEntries = true)
    public boolean sellModulo(AziendaEntity azienda, int moduloId) {
        if (azienda == null) {
            return false;
        }

        Optional<AziendaEntity> managedAziendaOpt = aziendaRepository.findById(azienda.getId_azienda());
        if (managedAziendaOpt.isEmpty()) {
            return false;
        }
        AziendaEntity managedAzienda = managedAziendaOpt.get();

        ModuloEntity modulo = moduloRepository.findById(moduloId).orElse(null);
        if (modulo == null) {
            return false;
        }

        //Controllo se il modulo è disponibile per tutti, in caso contrario ritorna false
        if (!modulo.isVisible()) return false;
            // Check if already purchased
        else if (!attivazioneRepository.existsByAziendaAndModulo(managedAzienda, modulo)) {
            return false;
        }

        //Cancello l'attivazione presente
        Optional<AttivazioneEntity> attivazioneOpt = attivazioneRepository.findById(new AttivazioneID(modulo, managedAzienda));

        if(attivazioneOpt.isEmpty()) return false;

        AttivazioneEntity attivazioneEntity = attivazioneOpt.get();

        // Rimuovo tutte le pertinenze (permessi) associate a questa attivazione
        List<PertinenzaEntity> pertinenze = pertinenzaRepository.findAllByAttivazione(attivazioneEntity);

        // Rimuovi in modo sicuro da Modulo per evitare TransientObjectException dovuta al CascadeType.ALL
        if (modulo.getAffiliazioni() != null) {
            modulo.getAffiliazioni().removeIf(p ->
                    p.getId_azienda() == managedAzienda.getId_azienda() &&
                            p.getId_modulo() == modulo.getId_modulo()
            );
        }

        for (PertinenzaEntity p : pertinenze) {
            // Rimuovi da Ruolo (se caricato)
            if (p.getRuolo() != null && p.getRuolo().getAffiliazioni() != null) {
                // Rimuovi usando l'identità o ID per sicurezza
                boolean removed = p.getRuolo().getAffiliazioni().remove(p);
                if (!removed) {
                    p.getRuolo().getAffiliazioni().removeIf(rp ->
                            rp.getId_ruolo() == p.getId_ruolo() &&
                                    rp.getId_modulo() == p.getId_modulo() &&
                                    rp.getId_azienda() == p.getId_azienda()
                    );
                }
            }
        }

        pertinenzaRepository.deleteAll(pertinenze);
        pertinenzaRepository.flush();

        attivazioneRepository.delete(attivazioneEntity);
        attivazioneRepository.flush();

        return true;
    }
}