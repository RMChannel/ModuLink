package com.modulink.Model.Utente;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Azienda.AziendaRepository;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Ruolo.RuoloService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Servizio core per la gestione dell'autenticazione e registrazione utenti.
 * <p>
 * Questa classe implementa l'interfaccia standard {@link UserDetailsService} per Spring Security
 * e fornisce metodi aggiuntivi per la gestione della logica di business legata agli utenti,
 * come la registrazione con calcolo manuale dell'ID.
 * <p>
 * Il servizio agisce da ponte tra il database (tramite Repository) e il SecurityContext,
 * trasformando le entità di dominio {@link UtenteEntity} in oggetti {@link UserDetails}
 * comprensibili dal framework di sicurezza.
 *
 * @see UserDetailsService
 * @see CustomUserDetails
 * @see UserRepository
 * @author Modulink Team
 * @version 1.1
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository per l'accesso ai dati persistenti dell'utente.
     * Utilizzato per il recupero dell'entità durante il login e il salvataggio in registrazione.
     */
    private final UserRepository userRepository;

    /**
     * Repository per recuperare le informazioni dell'azienda in fase di registrazione.
     * Necessario per associare correttamente l'utente al suo contesto aziendale.
     */
    private final AziendaRepository aziendaRepository;

    private final RuoloService ruoloService;

    /**
     * Costruttore per l'iniezione delle dipendenze (Dependency Injection).
     * <p>
     * Spring 4.3+ inietta automaticamente le dipendenze nei costruttori unici senza
     * necessità dell'annotazione {@code @Autowired}.
     *
     * @param userRepository    L'istanza del repository Utente gestita dal container.
     * @param aziendaRepository L'istanza del repository Azienda gestita dal container.
     */
    public CustomUserDetailsService(UserRepository userRepository, AziendaRepository aziendaRepository, RuoloService ruoloService) {
        this.userRepository = userRepository;
        this.aziendaRepository = aziendaRepository;
        this.ruoloService = ruoloService;
    }

    /**
     * Carica un utente basandosi sul suo identificativo di login (Email).
     * <p>
     * Sebbene il metodo si chiami {@code loadUserByUsername}, nel sistema Modulink
     * l'identificativo univoco per il login è l'indirizzo <strong>email</strong>.
     * <p>
     * Questo metodo viene invocato dal {@code DaoAuthenticationProvider} durante il processo
     * di autenticazione. Se l'utente viene trovato, la verifica della password verrà effettuata
     * automaticamente dal provider confrontando l'hash memorizzato con quello fornito.
     *
     * @param email L'email fornita dall'utente nel form di login.
     * @return Un'istanza di {@link CustomUserDetails} contenente le credenziali e i ruoli dell'utente.
     * @throws UsernameNotFoundException Se nessun utente viene trovato con l'email fornita.
     */
    @Cacheable(value = "userDetails", key = "#email")
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UtenteEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));

        return new CustomUserDetails(user);
    }

    /**
     * Gestisce la registrazione di un nuovo utente con ID calcolato manualmente.
     * <p>
     * Poiché l'architettura utilizza una chiave primaria composta (ID Azienda + ID Utente)
     * senza auto-increment globale, questo metodo:
     * <ol>
     * <li>Verifica l'esistenza dell'azienda specificata nel database.</li>
     * <li>Interroga il database per trovare l'ID utente massimo corrente per quell'azienda specifica.</li>
     * <li>Assegna al nuovo utente l'ID calcolato (MAX + 1).</li>
     * <li>Salva l'utente nel database rendendo persistente la relazione.</li>
     * </ol>
     * <p>
     * L'annotazione {@link Transactional} garantisce che tutte le operazioni (lettura MAX ID e salvataggio)
     * avvengano atomicamente.
     *
     * @param nuovoUtente L'oggetto {@link UtenteEntity} popolato con i dati anagrafici (Nome, Email, Password Hash).
     * @param idAzienda   L'identificativo dell'azienda a cui associare l'utente.
     * @throws IllegalArgumentException Se l'azienda con l'ID specificato non esiste, interrompendo la registrazione.
     */
    @Caching(evict = {
            @CacheEvict(value = {"users", "userDetails"}, key = "#nuovoUtente.email"),
            @CacheEvict(value = "usersByAzienda", key = "#idAzienda")
    })
    @Transactional
    public void registraUtente(UtenteEntity nuovoUtente, int idAzienda) {
        // 1. Recupera l'azienda con controllo di esistenza (Safety Check)
        AziendaEntity aziendaRecuperata = aziendaRepository.findById(idAzienda)
                .orElseThrow(() -> new IllegalArgumentException("Impossibile registrare l'utente: Azienda non trovata con ID " + idAzienda));

        // 2. Calcola il prossimo ID per QUESTA specifica azienda
        // Utilizza la query custom definita nel repository per ottenere il MAX(ID) attuale.
        int currentMaxId = userRepository.findMaxIdByAzienda(idAzienda);
        int nextId = currentMaxId + 1;

        // 3. Assegna l'ID manuale e la relazione
        nuovoUtente.setId_utente(nextId);
        nuovoUtente.setAzienda(aziendaRecuperata);

        // 4. Salva le modifiche
        userRepository.save(nuovoUtente);
    }

    @Cacheable(value = "users", key = "#email")
    public Optional<UtenteEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Cacheable(value = "usersByAzienda", key = "#aziendaEntity.id_azienda")
    public List<UtenteEntity> getAllByAzienda(AziendaEntity aziendaEntity) {
        return userRepository.getAllByAziendaIs(aziendaEntity);
    }

    @Caching(evict = {
            @CacheEvict(value = {"users", "userDetails"}, key = "#utente.email"),
            @CacheEvict(value = "usersByAzienda", key = "#utente.azienda.id_azienda")
    })
    @Transactional
    public void rimuoviUtente(UtenteEntity utente) {
        // Disaccoppia le associazioni dai ruoli per mantenere la coerenza del grafo oggetti
        if (utente.getAssociazioni() != null) {
            List<AssociazioneEntity> associazioni = new ArrayList<>(utente.getAssociazioni());
            for (AssociazioneEntity assoc : associazioni) {
                RuoloEntity ruolo = assoc.getRuolo();
                if (ruolo != null) {
                    ruolo.getAssociazioni().remove(assoc);
                }
            }
        }
        userRepository.delete(utente);
    }

    @Caching(evict = {
            @CacheEvict(value = {"users", "userDetails"}, key = "#utente.email"),
            @CacheEvict(value = "usersByAzienda", key = "#utente.azienda.id_azienda")
    })
    @Transactional
    public void aggiornaUtente(UtenteEntity utente) {
        userRepository.save(utente);
    }

    public boolean isThisaNewUtente(UtenteEntity utente) {
        Set<RuoloEntity> ruoli=utente.getRuoli();
        return  ruoli.contains(ruoloService.getNewUser(utente.getAzienda()));
    }

    public List<UtenteEntity> getAllUsersFromIDs(List<Integer> ids, int id_azienda) throws UserNotFoundException {
        List<UtenteEntity> utenti=new ArrayList<>();
        for(int id:ids) {
            Optional<UtenteEntity> utenteOpt=userRepository.findById(new UtenteID(id,id_azienda));
            if(utenteOpt.isEmpty()) throw new UserNotFoundException();
            else utenti.add(utenteOpt.get());
        }
        return utenti;
    }

    public UtenteEntity getByID(int id, int id_azienda) throws UserNotFoundException {
        Optional<UtenteEntity> utenteOpt=userRepository.findById(new UtenteID(id,id_azienda));
        if(utenteOpt.isEmpty()) throw new UserNotFoundException();
        return utenteOpt.get();
    }
}
