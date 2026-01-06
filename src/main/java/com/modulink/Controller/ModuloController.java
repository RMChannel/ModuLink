package com.modulink.Controller;

import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Utente.UtenteEntity;

import java.util.Optional;

public class ModuloController {
    private int id;
    private final ModuloService moduloService;

    public ModuloController(ModuloService moduloService, int id) {
        this.moduloService = moduloService;
        this.id = id;
    }

    protected boolean isAccessibleModulo(Optional<UtenteEntity> user) {
        if(user.isEmpty()) return false;
        UtenteEntity utente=user.get();
        return moduloService.isAccessibleModulo(this.id, utente);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
