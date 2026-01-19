package com.modulink.Controller.UserModules.GDU;

import com.modulink.Model.Azienda.AziendaService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import com.modulink.Model.Utente.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller REST per l'esposizione dei dati utente tramite API JSON.
 * <p>
 * Fornisce endpoint per il recupero asincrono delle informazioni di base degli utenti (nome, cognome, email),
 * filtrati rigorosamente per azienda di appartenenza.
 * Utilizzato principalmente da componenti frontend come campi di autocompletamento o liste dinamiche.
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.5
 * @since 1.4.0
 */
@RestController
@RequestMapping("/dashboard/gdu/api")
public class UserRestApi {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final AziendaService aziendaService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param customUserDetailsService Servizio utenti.
     * @param userRepository           Repository utenti.
     * @param aziendaService           Servizio aziende.
     * @since 1.4.0
     */
    public UserRestApi(CustomUserDetailsService customUserDetailsService, UserRepository userRepository, AziendaService aziendaService) {
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
        this.aziendaService = aziendaService;
    }

    /**
     * Recupera la lista di tutti gli utenti colleghi (stessa azienda) dell'utente richiedente.
     * <p>
     * Esclude l'utente richiedente dalla lista risultante.
     * </p>
     *
     * @param principal Identità dell'utente autenticato.
     * @return Lista di {@link UserBasicInfo} in formato JSON.
     * @since 1.4.0
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserBasicInfo>> getUsers(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String email = principal.getName();
        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(email);

        if (currentUserOpt.isEmpty()) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        UtenteEntity currentUser = currentUserOpt.get();

        // Recupera tutti gli utenti della stessa azienda del richiedente
        List<UtenteEntity> colleagues = userRepository.findAllByAziendaAndIdUtenteNot(currentUser.getAzienda(), currentUser.getId_utente());



        // Mappa le entità in DTO leggeri
        List<UserBasicInfo> response = colleagues.stream()
                .map(u -> new UserBasicInfo(
                        u.getId_utente(),
                        u.getNome(),
                        u.getCognome(),
                        u.getEmail()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    /**
     * Metodo di utilità per la conversione di una lista di entità Utente in DTO leggeri.
     *
     * @param users Lista di entità Utente.
     * @return Lista di DTO UserBasicInfo.
     */
    public static List<UserBasicInfo> parseUsers(List<UtenteEntity> users) {
        return users.stream()
                .map(u-> new UserBasicInfo(
                        u.getId_utente(),
                        u.getNome(),
                        u.getCognome(),
                        u.getEmail()
                ))
                .collect(Collectors.toList());
    }


    /**
     * Endpoint amministrativo (in sviluppo) per recuperare utenti di una specifica azienda.
     * <p>
     * Attualmente protetto da logica hardcoded (ID azienda 0) per restrizione accesso.
     * </p>
     *
     * @param id        ID dell'azienda target.
     * @param principal Identità richiedente.
     * @return Lista utenti o stato Forbidden.
     * @since 1.4.0
     */
    @GetMapping("/admin/users/{id}")
    public ResponseEntity<List<UserBasicInfo>> getUser(@PathVariable int id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String email = principal.getName();
        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(email);
        if(currentUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UtenteEntity currentUser = currentUserOpt.get();
        if(currentUser.getAzienda().getId_azienda() ==  0 ) {//hardcoded to correct
            List<UserBasicInfo> users = parseUsers(customUserDetailsService.getAllByAzienda(aziendaService.getAziendaById(id).get()));
            return ResponseEntity.ok(users);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }


    /**
     * Record DTO interno per trasferimento dati utente completo.
     */
    public record UserDTO(int id_utente, String nome, String cognome, String email) {}
    
    /**
     * Record DTO interno per informazioni di base utente (Lightweight).
     */
    public record UserBasicInfo(int id, String nome, String cognome, String email) {}
}
