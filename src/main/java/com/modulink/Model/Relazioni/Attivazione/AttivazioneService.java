package com.modulink.Model.Relazioni.Attivazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Modulo.ModuloRepository;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneEntity;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneRepository;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneService;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttivazioneService {
    private final AttivazioneRepository attivazioneRepository;
    private final ModuloRepository moduloRepository;
    private final AffiliazioneRepository affiliazioneRepository;
    private final RuoloService ruoloService;

    public AttivazioneService(AttivazioneRepository attivazioneRepository, ModuloRepository moduloRepository, AffiliazioneRepository affiliazioneRepository, RuoloService ruoloService) {
        this.attivazioneRepository=attivazioneRepository;
        this.moduloRepository=moduloRepository;
        this.affiliazioneRepository=affiliazioneRepository;
        this.ruoloService=ruoloService;
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
    public boolean purchaseModulo(AziendaEntity azienda, int moduloId) {
        if (azienda == null) {
            return false;
        }

        ModuloEntity modulo = moduloRepository.findById(moduloId).orElse(null);
        if (modulo == null) {
            return false;
        }

        // Check if already purchased
        if (attivazioneRepository.existsByAziendaAndModulo(azienda, modulo)) {
            return false;
        }

        // 1. Create and Save Attivazione
        AttivazioneEntity nuovaAttivazione = new AttivazioneEntity(modulo, azienda);
        attivazioneRepository.save(nuovaAttivazione);

        // Ensure Attivazione is written to DB before Affiliazione
        attivazioneRepository.flush();

        // 2. Assign Responsabile Role for this Module
        RuoloEntity resp = ruoloService.getResponsabile(azienda);
        if (resp != null) {
            AffiliazioneEntity affiliazioneEntity = new AffiliazioneEntity(
                    resp.getId_ruolo(),
                    modulo.getId_modulo(),
                    azienda.getId_azienda()
            );
            affiliazioneRepository.save(affiliazioneEntity);
        }

        return true;
    }
}