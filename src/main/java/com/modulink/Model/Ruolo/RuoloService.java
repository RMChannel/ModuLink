package com.modulink.Model.Ruolo;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import com.modulink.Model.Utente.UtenteEntity;
import com.modulink.Model.Utente.CustomUserDetailsService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RuoloService {
    private final RuoloRepository ruoloRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public RuoloService(RuoloRepository ruoloRepository, @Lazy CustomUserDetailsService customUserDetailsService) {
        this.ruoloRepository = ruoloRepository;
        this.customUserDetailsService = customUserDetailsService;
    }
    
    @Transactional
    public void updateRoleAssociations(AziendaEntity azienda, int idRole, List<UtenteEntity> users) {
        RuoloEntity role = getRoleById(idRole, azienda);
        
        List<UtenteEntity> usersToUpdate = new ArrayList<>();

        // Trovo gli utenti da rimuovere le associazioni
        List<AssociazioneEntity> currentAssocs = new ArrayList<>(role.getAssociazioni());
        for (AssociazioneEntity assoc : currentAssocs) {
            UtenteEntity user = assoc.getUtente();
            if (user != null) {
                //Trovo le specifiche associazioni da rimuovere
                AssociazioneEntity toRemove = null;
                for (AssociazioneEntity ua : user.getAssociazioni()) {
                    if (ua.getId_ruolo() == role.getId_ruolo() && ua.getId_azienda() == azienda.getId_azienda()) {
                        toRemove = ua;
                        break;
                    }
                }
                if (toRemove != null) {
                    user.getAssociazioni().remove(toRemove);
                }
                usersToUpdate.add(user);
            }
        }
        
        // Puliamo le associazioni al ruolo
        role.getAssociazioni().clear();
        
        // Aggiungo le nuove associazioni
        for (UtenteEntity user : users) {
            AssociazioneEntity newAssoc = new AssociazioneEntity(user, role);
            role.getAssociazioni().add(newAssoc);
            
            // Aggiorno gli utenti
            user.getAssociazioni().add(newAssoc);
            
            if (!usersToUpdate.contains(user)) {
                usersToUpdate.add(user);
            }
        }
        
        ruoloRepository.save(role);

        // Avvio l'aggiornamento della cache degli utenti col metodo dell'userService
        for (UtenteEntity user : usersToUpdate) {
            customUserDetailsService.aggiornaUtente(user);
        }
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
        if(idRole<=2) {
            throw new IllegalArgumentException("Cannot delete system roles");
        }
        RuoloEntity role = getRoleById(idRole, azienda);

        // Remove associations from users to prevent TransientObjectException
        for (AssociazioneEntity assoc : new ArrayList<>(role.getAssociazioni())) {
            UtenteEntity utente = assoc.getUtente();
            if (utente != null) {
                utente.getAssociazioni().remove(assoc);
            }
        }
        role.getAssociazioni().clear();

        ruoloRepository.delete(role);
    }

    public RuoloEntity getRoleById(int idRole, AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(idRole, azienda.getId_azienda()))
                .orElseThrow(() -> new IllegalArgumentException("Ruolo non trovato"));
    }

    public List<RuoloEntity> getAllRolesFromIds(List<Integer> ids, int id_azienda) throws RuoloNotFoundException {
        List<RuoloEntity> ruoli=new ArrayList<>();
        for(int id:ids) {
            Optional<RuoloEntity> ruoloOpt=ruoloRepository.findById(new RuoloID(id,id_azienda));
            if(ruoloOpt.isEmpty()) throw new RuoloNotFoundException();
            else ruoli.add(ruoloOpt.get());
        }
        return ruoli;
    }
}
