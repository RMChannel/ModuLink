package com.modulink.Controller.GDE;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Eventi.EventoID;
import com.modulink.Model.Eventi.EventoRepository;
import com.modulink.Model.Eventi.EventoService;
import com.modulink.Model.Relazioni.Partecipazione.PartecipazioneService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UserRepository;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard/calendar/api")
public class EventoController {

    private final EventoService eventoService;
    private final EventoRepository eventoRepository;
    private final PartecipazioneService partecipazioneService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    public EventoController(EventoService eventoService, EventoRepository eventoRepository, PartecipazioneService partecipazioneService, CustomUserDetailsService customUserDetailsService, UserRepository userRepository) {
        this.eventoService = eventoService;
        this.eventoRepository = eventoRepository;
        this.partecipazioneService = partecipazioneService;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createEvento(@RequestBody CreateEventoRequest request, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(principal.getName());
        if (currentUserOpt.isEmpty()) return ResponseEntity.status(403).build();

        UtenteEntity currentUser = currentUserOpt.get();
        AziendaEntity azienda = currentUser.getAzienda();

        // Genera ID (manuale per chiave composta)
        int newId = eventoRepository.findMaxIdByAzienda(azienda) + 1;

        EventoEntity evento = new EventoEntity(
                newId,
                azienda,
                request.nome(),
                request.luogo(),
                request.inizio(),
                request.fine(),
                currentUser
        );

        eventoService.create(evento);

        // Aggiungi il creatore come partecipante
        partecipazioneService.Invita(evento, currentUser);

        // Aggiungi partecipanti selezionati (se presenti)
        if (request.partecipanti() != null && !request.partecipanti().isEmpty()) {
            List<UtenteEntity> colleagues = userRepository.getAllByAziendaIs(azienda);
            for (Integer userId : request.partecipanti()) {
                 colleagues.stream()
                        .filter(u -> u.getId_utente() == userId)
                        .findFirst()
                        .ifPresent(u -> partecipazioneService.Invita(evento, u));
            }
        }

        return ResponseEntity.ok().body("{\"status\": \"success\", \"id\": " + newId + "}");
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteEvento(@RequestBody DeleteEventoRequest request, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(principal.getName());
        if (currentUserOpt.isEmpty()) return ResponseEntity.status(403).build();

        AziendaEntity azienda = currentUserOpt.get().getAzienda();

        // Costruisci l'ID composto
        EventoID eventoID = new EventoID(request.id(), azienda.getId_azienda());

        if (eventoRepository.existsById(eventoID)) {
            eventoRepository.deleteById(eventoID);
            return ResponseEntity.ok().body("{\"status\": \"deleted\"}");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public record CreateEventoRequest(String nome, String luogo, LocalDateTime inizio, LocalDateTime fine, List<Integer> partecipanti) {}
    public record DeleteEventoRequest(int id) {}
}
