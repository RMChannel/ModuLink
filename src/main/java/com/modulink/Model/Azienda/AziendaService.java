package com.modulink.Model.Azienda;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AziendaService {
    private final AziendaRepository aziendaRepository;

    public AziendaService(AziendaRepository aziendaRepository) {
        this.aziendaRepository = aziendaRepository;
    }

    public AziendaEntity getAziendaByPIVA(String piva) {
        Optional<AziendaEntity> azienda = aziendaRepository.findByPiva(piva);
        return azienda.orElse(null);
    }

    @Transactional
    public void registraAzienda(AziendaEntity azienda) {
        Integer maxID=aziendaRepository.findMaxId();
        if(maxID==null) maxID=-1;
        azienda.setId_azienda(maxID+1);
        aziendaRepository.save(azienda);
    }
}
