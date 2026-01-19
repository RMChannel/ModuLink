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

/**
 * Service layer responsabile della gestione della logica di business per l'entità {@link RuoloEntity}.
 * <p>
 * Fornisce metodi per la manipolazione dei ruoli aziendali, la gestione delle associazioni
 * tra ruoli e utenti, e l'inizializzazione dei ruoli predefiniti di sistema durante
 * l'onboarding di una nuova azienda.
 * </p>
 * <p>
 * La classe gestisce la coerenza dei dati attraverso transazioni database ({@link Transactional})
 * e si interfaccia con {@link CustomUserDetailsService} per l'aggiornamento della sicurezza utente.
 * </p>
 *
 * @author Modulink Team
 * @version 2.3.1
 * @since 1.0.0
 */
@Service
public class RuoloService {
    private final RuoloRepository ruoloRepository;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Costruttore per la Dependency Injection.
     * <p>
     * Utilizza l'annotazione {@link Lazy} per {@link CustomUserDetailsService} per evitare
     * problemi di dipendenze circolari durante il bootstrap del contesto Spring.
     * </p>
     *
     * @param ruoloRepository           Repository per l'accesso ai dati dei ruoli.
     * @param customUserDetailsService Servizio per la gestione dei dettagli di sicurezza degli utenti.
     * @since 1.0.0
     */
    public RuoloService(RuoloRepository ruoloRepository, @Lazy CustomUserDetailsService customUserDetailsService) {
        this.ruoloRepository = ruoloRepository;
        this.customUserDetailsService = customUserDetailsService;
    }
    
    /**
     * Sincronizza le associazioni tra un ruolo specifico e una lista di utenti fornita.
     * <p>
     * Il metodo esegue una logica di aggiornamento completa:
     * 1. Rimuove tutte le associazioni preesistenti per il ruolo indicato.
     * 2. Pulisce i riferimenti incrociati nelle entità {@link UtenteEntity}.
     * 3. Crea nuove istanze di {@link AssociazioneEntity} per ogni utente nella lista.
     * 4. Notifica il servizio di sicurezza per ricaricare i permessi degli utenti coinvolti.
     * </p>
     *
     * @param azienda L'azienda in cui opera il ruolo.
     * @param idRole  L'identificativo locale del ruolo da aggiornare.
     * @param users   La lista aggiornata di utenti da associare al ruolo.
     * @since 1.5.0
     */
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

    /**
     * Inizializza i ruoli di default necessari per il funzionamento di una nuova azienda.
     * <p>
     * Crea i seguenti ruoli predefiniti:
     * <ul>
     * <li>ID 0: Responsabile (Amministratore con pieni poteri)</li>
     * <li>ID 1: Utente Nuovo (Profilo limitato per utenti non ancora validati)</li>
     * <li>ID 2: Utente (Profilo operativo standard)</li>
     * </ul>
     *
     * @param aziendaEntity L'entità dell'azienda appena registrata.
     * @return L'entità del ruolo "Responsabile" appena creato.
     * @since 1.1.0
     */
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

    /**
     * Recupera il ruolo di "Responsabile" (ID 0) per l'azienda specificata.
     *
     * @param azienda L'azienda di riferimento.
     * @return L'entità {@link RuoloEntity} corrispondente.
     * @since 1.0.0
     */
    public RuoloEntity getResponsabile(AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(0, azienda.getId_azienda())).orElseThrow();
    }

    /**
     * Recupera il ruolo di "Utente Nuovo" (ID 1) per l'azienda specificata.
     *
     * @param azienda L'azienda di riferimento.
     * @return L'entità {@link RuoloEntity} corrispondente.
     * @since 1.0.0
     */
    public RuoloEntity getNewUser(AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(1, azienda.getId_azienda())).orElseThrow();
    }

    /**
     * Recupera il ruolo di "Utente Standard" (ID 2) per l'azienda specificata.
     *
     * @param azienda L'azienda di riferimento.
     * @return L'entità {@link RuoloEntity} corrispondente.
     * @since 1.0.0
     */
    public RuoloEntity getStandardUser(AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(2, azienda.getId_azienda())).orElseThrow();
    }

    /**
     * Restituisce tutti i ruoli definiti all'interno di un contesto aziendale.
     *
     * @param azienda L'azienda proprietaria.
     * @return Lista di tutti i ruoli trovati.
     * @since 1.0.0
     */
    public List<RuoloEntity> getAllRolesByAzienda(AziendaEntity azienda) {
        return ruoloRepository.findAllByAzienda(azienda);
    }

    /**
     * Crea un nuovo ruolo personalizzato generandone l'ID in modo sequenziale.
     * <p>
     * Calcola il nuovo ID incrementando il valore massimo attualmente presente per l'azienda.
     * </p>
     *
     * @param azienda L'azienda in cui creare il ruolo.
     * @param ruolo   L'entità ruolo parzialmente popolata.
     * @return L'entità del ruolo persistita con il nuovo ID assegnato.
     * @since 1.3.0
     */
    @Transactional
    public RuoloEntity createRole(AziendaEntity azienda, RuoloEntity ruolo) {
        int newId = ruoloRepository.findMaxIdByAzienda(azienda.getId_azienda()) + 1;
        ruolo.setId_ruolo(newId);
        return ruoloRepository.save(ruolo);
    }

    /**
     * Aggiorna i metadati di un ruolo esistente.
     *
     * @param azienda     L'azienda di appartenenza.
     * @param idRole      L'ID del ruolo da modificare.
     * @param nome        Nuovo nome del ruolo.
     * @param colore      Nuovo codice colore.
     * @param descrizione Nuova descrizione.
     * @return L'entità aggiornata.
     * @since 1.3.0
     */
    @Transactional
    public RuoloEntity updateRole(AziendaEntity azienda, int idRole, String nome, String colore, String descrizione) {
        RuoloEntity role = getRoleById(idRole, azienda);
        role.setNome(nome);
        role.setColore(colore);
        role.setDescrizione(descrizione);
        return ruoloRepository.save(role);
    }

    /**
     * Elimina permanentemente un ruolo dal database.
     * <p>
     * Impedisce la cancellazione dei ruoli di sistema (ID &lt;= 2) per garantire la stabilità operativa.
     * Prima dell'eliminazione, rimuove tutte le associazioni con gli utenti per evitare violazioni
     * di integrità referenziale o eccezioni di tipo {@code TransientObjectException}.
     * </p>
     *
     * @param azienda L'azienda proprietaria.
     * @param idRole  L'ID del ruolo da rimuovere.
     * @throws IllegalArgumentException Se si tenta di eliminare un ruolo protetto di sistema.
     * @since 1.6.0
     */
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

    /**
     * Cerca un ruolo specifico basandosi sulla chiave primaria composta.
     *
     * @param idRole  L'identificativo numerico del ruolo.
     * @param azienda L'azienda associata.
     * @return L'entità {@link RuoloEntity} trovata.
     * @throws IllegalArgumentException Se il ruolo non esiste.
     * @since 1.0.0
     */
    public RuoloEntity getRoleById(int idRole, AziendaEntity azienda) {
        return ruoloRepository.findById(new RuoloID(idRole, azienda.getId_azienda()))
                .orElseThrow(() -> new IllegalArgumentException("Ruolo non trovato"));
    }

    /**
     * Recupera una lista di entità ruolo a partire da una collezione di ID.
     *
     * @param ids        Collezione di identificativi numerici dei ruoli.
     * @param id_azienda L'ID dell'azienda di riferimento.
     * @return Lista di {@link RuoloEntity} corrispondenti agli ID forniti.
     * @throws RuoloNotFoundException Se anche uno solo degli ID non corrisponde ad alcun ruolo esistente.
     * @since 1.2.0
     */
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
