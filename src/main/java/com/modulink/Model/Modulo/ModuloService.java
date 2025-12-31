package com.modulink.Model.Modulo;

import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuloService {
    private final ModuloRepository moduloRepository;

    public ModuloService(ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    public List<ModuloEntity> findModuliByUtente(UtenteEntity utente) {
        return moduloRepository.findModuliByUtente(utente);
    }

    public boolean isAccessibleModulo(int id, UtenteEntity utente) {
        return !moduloRepository.isModuloAccessible(id, utente).isEmpty();
    }
}
