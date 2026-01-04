package com.modulink.Model.Modulo;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneEntity;
import com.modulink.Model.Relazioni.Affiliazione.AffiliazioneService;
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

    @Cacheable(value = "moduliByUtente", key = "#utente.email")
    public List<ModuloEntity> findModuliByUtente(UtenteEntity utente) {
        return moduloRepository.findModuliByUtente(utente);
    }

    @Cacheable(value = "moduloAccess", key = "{#id, #utente.email}")
    public boolean isAccessibleModulo(int id, UtenteEntity utente) {
        return !moduloRepository.isModuloAccessible(id, utente).isEmpty();
    }

    @Cacheable(value = "modulo", key = "#idModulo")
    public ModuloEntity getModuloById(int idModulo) {
        return moduloRepository.findById(idModulo).orElseThrow(() -> new IllegalArgumentException("Modulo non trovato"));
    }

    @Transactional
    @CacheEvict(value = {"moduliByUtente", "moduloAccess", "modulo"}, allEntries = true)
    public void updateModuloAffiliations(AziendaEntity azienda, int idModulo, List<RuoloEntity> ruoli) {
        ModuloEntity modulo = getModuloById(idModulo);

        List<AffiliazioneEntity> existingAffiliations = affiliazioneService.findAllByAttivazione(new AttivazioneEntity(modulo, azienda));

        Set<Integer> existingRoleIds = existingAffiliations.stream()
                .map(AffiliazioneEntity::getId_ruolo)
                .collect(Collectors.toSet());

        Set<Integer> targetRoleIds = ruoli.stream()
                .map(RuoloEntity::getId_ruolo)
                .collect(Collectors.toSet());

        List<AffiliazioneEntity> toRemove = existingAffiliations.stream()
                .filter(a -> !targetRoleIds.contains(a.getId_ruolo()))
                .collect(Collectors.toList());

        modulo.getAffiliazioni().removeAll(toRemove);

        for (RuoloEntity ruolo : ruoli) {
            if (!existingRoleIds.contains(ruolo.getId_ruolo())) {
                AffiliazioneEntity affiliazione = new AffiliazioneEntity(ruolo.getId_ruolo(), idModulo, azienda.getId_azienda());
                affiliazione.setRuolo(ruolo);
                affiliazione.setAttivazione(attivazioneService.getAttivazioneById(new AttivazioneID(modulo, azienda)));
                modulo.getAffiliazioni().add(affiliazione);
            }
        }
        moduloRepository.save(modulo);
    }
}
