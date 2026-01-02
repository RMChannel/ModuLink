package com.modulink.Model.Modulo;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneEntity;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneService;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneID;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneService;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModuloService {
    private final ModuloRepository moduloRepository;
    private final AffiliazioneService affiliazioneService;
    private final AttivazioneService attivazioneService;

    public ModuloService(ModuloRepository moduloRepository, AffiliazioneService affiliazioneService, AttivazioneService attivazioneService) {
        this.moduloRepository = moduloRepository;
        this.affiliazioneService = affiliazioneService;
        this.attivazioneService = attivazioneService;
    }

    public List<ModuloEntity> findModuliByUtente(UtenteEntity utente) {
        return moduloRepository.findModuliByUtente(utente);
    }

    public boolean isAccessibleModulo(int id, UtenteEntity utente) {
        return !moduloRepository.isModuloAccessible(id, utente).isEmpty();
    }

    public ModuloEntity getModuloById(int idModulo) {
        return moduloRepository.findById(idModulo).orElseThrow(() -> new IllegalArgumentException("Modulo non trovato"));
    }

    @Transactional
    public void updateModuloAffiliations(AziendaEntity azienda, int idModulo, List<RuoloEntity> ruoli) {
        ModuloEntity modulo = getModuloById(idModulo);
        modulo.getAffiliazioni().removeAll(affiliazioneService.findAllByAttivazione(new AttivazioneEntity(modulo,azienda)));
        for(RuoloEntity ruolo:ruoli) {
            AffiliazioneEntity affiliazione = new AffiliazioneEntity(ruolo.getId_ruolo(),idModulo,azienda.getId_azienda());
            affiliazione.setRuolo(ruolo);
            affiliazione.setAttivazione(attivazioneService.getAttivazioneById(new AttivazioneID(modulo,azienda)));
            modulo.getAffiliazioni().add(affiliazione);
        }
        moduloRepository.save(modulo);
    }
}
