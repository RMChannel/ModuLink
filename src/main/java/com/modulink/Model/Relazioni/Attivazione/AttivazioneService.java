package com.modulink.Model.Relazioni.Attivazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaRepository;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneEntity;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneRepository;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneService;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AttivazioneService {
    private final AttivazioneRepository attivazioneRepository;
    private final ModuloRepository moduloRepository;
    private final AffiliazioneRepository affiliazioneRepository;
    private final RuoloService ruoloService;
    private final AziendaRepository aziendaRepository;

    public AttivazioneService(AttivazioneRepository attivazioneRepository, ModuloRepository moduloRepository, AffiliazioneRepository affiliazioneRepository, RuoloService ruoloService, AziendaRepository aziendaRepository) {
        this.attivazioneRepository=attivazioneRepository;
        this.moduloRepository=moduloRepository;
        this.affiliazioneRepository=affiliazioneRepository;
        this.ruoloService=ruoloService;
        this.aziendaRepository=aziendaRepository;
    }

    public AttivazioneEntity getAttivazioneById(AttivazioneID attivazioneID) {
        return attivazioneRepository.findById(attivazioneID).orElseThrow(() -> new IllegalArgumentException("L'attivazione non è stata trovata"));
    }

    @Transactional
    public void attivazioneDefault(AziendaEntity aziendaEntity) {
        List<AttivazioneEntity> attivazioni=new ArrayList<>();

        attivazioni.add(new AttivazioneEntity(moduloRepository.getReferenceById(0),aziendaEntity));
        attivazioni.add(new AttivazioneEntity(moduloRepository.getReferenceById(1),aziendaEntity));
        attivazioni.add(new AttivazioneEntity(moduloRepository.getReferenceById(2),aziendaEntity));
        attivazioni.add(new AttivazioneEntity(moduloRepository.getReferenceById(3),aziendaEntity));

        attivazioneRepository.saveAll(attivazioni);
    }

    public List<ModuloEntity> getNotPurchased(AziendaEntity aziendaEntity) {
        if (aziendaEntity == null) {
            return new ArrayList<>();
        }
        return attivazioneRepository.getAllNotPurchased(aziendaEntity);
    }

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

        // Check if already purchased
        if (attivazioneRepository.existsByAziendaAndModulo(managedAzienda, modulo)) {
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
            AffiliazioneEntity affiliazioneEntity = new AffiliazioneEntity(
                    resp.getId_ruolo(),
                    modulo.getId_modulo(),
                    managedAzienda.getId_azienda()
            );
            affiliazioneRepository.save(affiliazioneEntity);
        }

        return true;
    }
}