package com.modulink.Model.Relazioni.Pertinenza;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Layer per la gestione dei permessi (Pertinenze) sui moduli.
 *
 * @author Modulink Team
 * @version 1.6.5
 * @since 1.1.5
 */
@Service
public class PertinenzaService {
    private final PertinenzaRepository pertinenzaRepository;

    /**
     * Costruttore.
     *
     * @param pertinenzaRepository Repository pertinenze.
     * @since 1.1.5
     */
    public PertinenzaService(PertinenzaRepository pertinenzaRepository) {
        this.pertinenzaRepository = pertinenzaRepository;
    }

    /**
     * Assegna i permessi predefiniti per i moduli base al ruolo di amministrazione/default di una nuova azienda.
     * <p>
     * Viene chiamata tipicamente all'atto della creazione di una nuova azienda per garantire
     * l'accesso immediato ai moduli Core (ID 0, 1, 2, 3, 9999) al ruolo con ID 0 (Admin/Responsabile).
     * </p>
     *
     * @param aziendaEntity L'azienda appena creata.
     * @since 1.1.5
     */
    @Transactional
    public void attivazioneDefault(AziendaEntity aziendaEntity) {
        List<PertinenzaEntity> affiliazioni=new ArrayList<>();

        affiliazioni.add(new PertinenzaEntity(0,0,aziendaEntity.getId_azienda()));
        affiliazioni.add(new PertinenzaEntity(0,1,aziendaEntity.getId_azienda()));
        affiliazioni.add(new PertinenzaEntity(0,2,aziendaEntity.getId_azienda()));
        affiliazioni.add(new PertinenzaEntity(0,3,aziendaEntity.getId_azienda()));
        affiliazioni.add(new PertinenzaEntity(0,9999,aziendaEntity.getId_azienda()));

        pertinenzaRepository.saveAll(affiliazioni);
    }

    /**
     * Recupera tutte le pertinenze associate a una specifica attivazione.
     *
     * @param attivazione L'attivazione del modulo.
     * @return Lista di permessi associati.
     * @since 1.2.0
     */
    public List<PertinenzaEntity> findAllByAttivazione(AttivazioneEntity attivazione) {
        return pertinenzaRepository.findAllByAttivazione(attivazione);
    }
}
