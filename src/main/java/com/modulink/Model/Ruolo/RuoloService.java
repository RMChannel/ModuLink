package com.modulink.Model.Ruolo;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuoloService {
    private final RuoloRepository ruoloRepository;

    public RuoloService(RuoloRepository ruoloRepository) {
        this.ruoloRepository = ruoloRepository;
    }
    
    @Transactional
    public void updateRoleAssociations(AziendaEntity azienda, int idRole, List<UtenteEntity> users) {
        RuoloEntity role = getRoleById(idRole, azienda);
        role.getAssociazioni().clear();
        for (UtenteEntity user : users) {
            role.getAssociazioni().add(new AssociazioneEntity(user, role));
        }
        ruoloRepository.save(role);
    }

    @Transactional
    public RuoloEntity attivazioneDefault(AziendaEntity aziendaEntity) {
        List<RuoloEntity> ruoli=new ArrayList<>();
        RuoloEntity ruoloResponsabile = new RuoloEntity(0,aziendaEntity,"Responsabile","#000000","Responsabile dell'azienda");
        ruoli.add(ruoloResponsabile);
        ruoli.add(new RuoloEntity(1,aziendaEntity,"Utente Nuovo","blue","Utente non ancora ufficialmente registrato"));
        ruoli.add(new RuoloEntity(2,aziendaEntity,"Utente","grey","Utente Standard"));
        ruoloRepository.saveAll(ruoli);
        return ruoloResponsabile;
    }

    public RuoloEntity getResponsabile(AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(0, azienda.getId_azienda())).orElseThrow();
    }

    public RuoloEntity getNewUser(AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(1, azienda.getId_azienda())).orElseThrow();
    }

    public RuoloEntity getStandardUser(AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(2, azienda.getId_azienda())).orElseThrow();
    }

    public List<RuoloEntity> getAllRolesByAzienda(AziendaEntity azienda) {
        return ruoloRepository.findAllByAzienda(azienda);
    }

    @Transactional
    public RuoloEntity createRole(AziendaEntity azienda, RuoloEntity ruolo) {
        int newId = ruoloRepository.findMaxIdByAzienda(azienda.getId_azienda()) + 1;
        ruolo.setId_ruolo(newId);
        return ruoloRepository.save(ruolo);
    }

    @Transactional
    public RuoloEntity updateRole(AziendaEntity azienda, int idRole, String nome, String colore, String descrizione) {
        RuoloEntity role = getRoleById(idRole, azienda);
        role.setNome(nome);
        role.setColore(colore);
        role.setDescrizione(descrizione);
        return ruoloRepository.save(role);
    }

    @Transactional
    public void deleteRole(AziendaEntity azienda, int idRole) {
        // Optional: Add check to prevent deleting system roles (e.g. 0, 1, 2)
        // if (idRole <= 2) throw new IllegalArgumentException("Cannot delete system roles");
        if(idRole<=2) {
            throw new IllegalArgumentException("Cannot delete system roles");
        }
        RuoloEntity role = getRoleById(idRole, azienda);
        ruoloRepository.delete(role);
    }

    public RuoloEntity getRoleById(int idRole, AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(idRole, azienda.getId_azienda()))
                .orElseThrow(() -> new IllegalArgumentException("Ruolo non trovato"));
    }
}
