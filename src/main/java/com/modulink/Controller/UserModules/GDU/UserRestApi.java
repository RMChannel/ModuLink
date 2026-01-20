package com.modulink.Controller.UserModules.GDU;

import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import com.modulink.Model.Utente.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard/gdu/api")
public class UserRestApi {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    public UserRestApi(CustomUserDetailsService customUserDetailsService, UserRepository userRepository) {
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
    }

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
        List<UtenteEntity> colleagues = userRepository.getAllByAziendaIs(currentUser.getAzienda());
        


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


    //SimpleParser
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


    // DTO Record interno per pulizia e immutabilità
    public record UserDTO(int id_utente, String nome, String cognome) {}
    public record UserBasicInfo(int id, String nome, String cognome, String email) {}
}
