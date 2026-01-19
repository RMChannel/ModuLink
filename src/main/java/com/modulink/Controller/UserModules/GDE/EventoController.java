package com.modulink.Controller.UserModules.GDE;
import com.modulink.Controller.UserModules.GDU.UserRestApi.UserDTO;
import com.modulink.Controller.ModuloController;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Eventi.*;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneService;
import com.modulink.Model.Relazioni.Partecipazione.PartecipazioneService;
import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Task.TaskService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UserNotFoundException;
import com.modulink.Model.Utente.UserRepository;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller REST per la gestione avanzata degli eventi e dell'integrazione calendario nel modulo <strong>GDE</strong>.
 * <p>
 * Fornisce un set completo di API per la manipolazione degli eventi aziendali, la gestione dei partecipanti
 * e l'esposizione sincronizzata dei task assegnati. Implementa logiche di validazione stringenti sui periodi temporali
 * e garantisce l'integrità dei dati tra Utenti, Aziende ed Eventi.
 * </p>
 *
 * @author Modulink Team
 * @version 2.1.5
 * @since 1.2.0
 */
@Controller
@RequestMapping("/dashboard/calendar/api")
public class EventoController extends ModuloController {

    private final EventoService eventoService;
    private final EventoRepository eventoRepository;
    private final PartecipazioneService partecipazioneService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final AssegnazioneService assegnazioneService;


    /**
     * Inizializza il controller iniettando i servizi necessari per l'interazione con eventi, task e utenze.
     *
     * @param eventoService            Servizio eventi.
     * @param eventoRepository         Repository eventi.
     * @param partecipazioneService    Servizio per la gestione degli inviti.
     * @param customUserDetailsService Servizio utenti.
     * @param userRepository           Repository utenti.
     * @param moduloService            Servizio moduli.
     * @param assegnazioneService      Servizio assegnazioni task.
     * @since 1.2.0
     */
    public EventoController(EventoService eventoService, EventoRepository eventoRepository, PartecipazioneService partecipazioneService, CustomUserDetailsService customUserDetailsService, UserRepository userRepository, ModuloService moduloService, AssegnazioneService assegnazioneService) {
        super(moduloService, 4);
        this.eventoService = eventoService;
        this.eventoRepository = eventoRepository;
        this.partecipazioneService = partecipazioneService;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
        this.assegnazioneService = assegnazioneService;
    }

    /**
     * Recupera l'elenco di tutti gli eventi a cui partecipa l'utente corrente.
     *
     * @param principal Identità dell'utente autenticato.
     * @return {@link ResponseEntity} contenente una lista di {@link EventoDTO} o stato FORBIDDEN.
     * @since 1.2.0
     */
    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<?> getEventi(Principal principal){
        if (principal==null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(principal.getName());
        if(!isAccessibleModulo(currentUserOpt)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (currentUserOpt.isEmpty()) return ResponseEntity.status(403).build();
        UtenteEntity currentUser = currentUserOpt.get();
        AziendaEntity azienda = currentUser.getAzienda();

        List<EventoEntity> eventi = eventoService.findAllByUtente(currentUser);

        return  new ResponseEntity<>(parse(eventi), HttpStatus.OK);
    }


    /**
     * Recupera i task assegnati all'utente corrente, mappandoli come eventi di calendario.
     * <p>
     * Questo endpoint permette di visualizzare i task operativi direttamente nel calendario,
     * impostando come orario l'intera giornata della scadenza.
     * </p>
     *
     * @param principal Identità dell'utente.
     * @return {@link ResponseEntity} con la lista di task trasformati in DTO.
     * @since 1.3.0
     */
    @GetMapping("/getet")
    public ResponseEntity<?> getEventiAndTask(Principal principal){
        if (principal==null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(principal.getName());
        if(!isAccessibleModulo(currentUserOpt)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (currentUserOpt.isEmpty()) return ResponseEntity.status(403).build();
        UtenteEntity currentUser = currentUserOpt.get();
        AziendaEntity azienda = currentUser.getAzienda();

        List<TaskEntity> tasks = assegnazioneService.getTaskAssegnate(currentUser);
        List<EventoDTO> tasksP = parseTask(tasks);
        return new ResponseEntity<>(tasksP, HttpStatus.OK);
    }



    /**
     * Gestisce la creazione di un nuovo evento aziendale.
     * <p>
     * Implementa validazioni sulla lunghezza dei campi, sull'obbligatorietà della data di inizio
     * e impedisce la creazione di eventi in date passate. Associa automaticamente il creatore
     * come partecipante e processa la lista degli invitati.
     * </p>
     *
     * @param request   DTO contenente i dati dell'evento e i partecipanti.
     * @param principal Identità dell'utente creatore.
     * @return JSON con lo stato dell'operazione e l'ID dell'evento creato.
     * @since 1.2.0
     */
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createEvento(@RequestBody CreateEventoRequest request, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(principal.getName());
        if (currentUserOpt.isEmpty()) return ResponseEntity.status(403).build();

        // VALIDAZIONE

        if(request.nome().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\": \"Il nome dell'evento non può essere vuoto.\"}");
        }
        if(request.nome().length()>200 || request.nome().length()<2) {
            return ResponseEntity.badRequest().body("{\"error\": \"Il nome dev'essere compreso tra i 2 e i 200 caratteri.\"}");
        }
        if(request.luogo() != null && request.luogo().length()>200) {
            return ResponseEntity.badRequest().body("{\"error\": \"Lunghezza Luogo supera 200 caratteri\"}");
        }
        if (request.inizio() == null) {
            return ResponseEntity.badRequest().body("{\"error\": \"Campo data vuoto\"}");
        }
        if (request.inizio().isBefore(LocalDateTime.of(1970, 1, 1, 0, 0))) {
            return ResponseEntity.badRequest().body("{\"error\": \"Data inizio antecedente alla data limite\"}");
        }
        if (request.inizio().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("{\"error\": \"Non puoi creare eventi nel passato.\"}");
        }
        if (request.fine() != null && request.fine().isBefore(request.inizio())) {
            return ResponseEntity.badRequest().body("{\"error\": \"Data fine antecedente alla data inizio\"}");
        }

        UtenteEntity currentUser = currentUserOpt.get();
        AziendaEntity azienda = currentUser.getAzienda();

        EventoEntity evento = new EventoEntity(
                azienda,
                request.nome(),
                request.luogo(),
                request.inizio(),
                request.fine()
        );
        evento.setCreatore(currentUser);

        evento=eventoService.create(evento);
        partecipazioneService.Invita(evento, currentUser);

        if (request.partecipanti() != null && !request.partecipanti().isEmpty()) {
            List<UtenteEntity> colleagues = customUserDetailsService.getAllByAziendaExcludingUser(azienda,currentUser.getId_utente());
            for (Integer userId : request.partecipanti()) {
                EventoEntity finalEvento = evento;
                colleagues.stream()
                        .filter(u -> u.getId_utente() == userId)
                        .findFirst()
                        .ifPresent(u -> partecipazioneService.Invita(finalEvento, u));
            }
        }

        return ResponseEntity.ok().body("{\"status\": \"success\", \"id\": " + evento.getId_evento() + "}");
    }

    /**
     * Aggiorna un evento esistente.
     * <p>
     * Oltre ai dati base, gestisce la sincronizzazione dei partecipanti aggiungendo i nuovi
     * e rimuovendo quelli deselezionati. Garantisce che il creatore non possa essere rimosso.
     * </p>
     *
     * @param request   DTO con i dati aggiornati dell'evento.
     * @param principal Identità dell'utente loggato.
     * @return JSON di conferma aggiornamento.
     * @since 1.2.0
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateEvento(@RequestBody UpdateEventoRequest request, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(principal.getName());
        if (currentUserOpt.isEmpty() || !isAccessibleModulo(currentUserOpt)) return ResponseEntity.status(403).build();

        UtenteEntity currentUser = currentUserOpt.get();
        EventoEntity evento = eventoRepository.getById(new EventoID(request.id, currentUser.getAzienda().getId_azienda()));

        // VALIDAZIONE
        if(request.nome().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\": \"Il nome dell'evento non può essere vuoto.\"}");
        }
        if(request.nome().length()>200 || request.nome().length()<2) {
            return ResponseEntity.badRequest().body("{\"error\": \"Il nome dev'essere compreso tra i 2 e i 200 caratteri.\"}");
        }
        if(request.luogo() != null && request.luogo().length()>200) {
            return ResponseEntity.badRequest().body("{\"error\": \"Lunghezza Luogo supera 200 caratteri\"}");
        }
        if (request.inizio() == null) {
            return ResponseEntity.badRequest().body("{\"error\": \"Campo data vuoto\"}");
        }
        if (request.inizio().isBefore(LocalDateTime.of(1970, 1, 1, 0, 0))) {
            return ResponseEntity.badRequest().body("{\"error\": \"Data inizio antecedente alla data limite\"}");
        }
        if (request.fine() != null && request.fine().isBefore(request.inizio())) {
            return ResponseEntity.badRequest().body("{\"error\": \"Data fine antecedente alla data inizio\"}");
        }
        
        // VALIDAZIONE CREATORE
        if(request.partecipanti != null && !request.partecipanti.contains(evento.getCreatore().getId_utente())) {
             return ResponseEntity.badRequest().body("{\"error\": \"Impossibile rimuovere il creatore dall'evento.\"}");
        }

        evento.setData_fine(request.fine);
        evento.setData_ora_inizio(request.inizio);
        evento.setNome(request.nome);
        evento.setLuogo(request.luogo);

        eventoService.update(evento); // Assicurati di salvare le modifiche all'evento stesso

        List<UtenteEntity> attualiPartecipanti = partecipazioneService.getUtenteEntitiesByEvento(evento);
        List<UtenteEntity> daRimuovere = new ArrayList<>();

        // Identifica chi rimuovere (CORREZIONE ConcurrentModificationException)
        for (UtenteEntity utente : attualiPartecipanti) {
            if (!request.partecipanti.contains(utente.getId_utente())) {
                daRimuovere.add(utente);
            }
        }

        // Esegui rimozioni
        for (UtenteEntity u : daRimuovere) {
            partecipazioneService.RimuoviInvito(evento, u);
        }

        // Identifica chi aggiungere
        List<Integer> idsAttuali = attualiPartecipanti.stream().map(UtenteEntity::getId_utente).toList();

        for (Integer userId : request.partecipanti()) {
            // Se non è tra quelli attuali (e non è stato appena rimosso, logica implicita)
            if (!idsAttuali.contains(userId)) {
                try {
                    UtenteEntity utente = customUserDetailsService.getByID(userId, currentUser.getAzienda().getId_azienda());
                    partecipazioneService.Invita(evento, utente);
                } catch (UserNotFoundException e) {
                    // Ignora o logga
                }
            }
        }
        return ResponseEntity.ok().body("{\"status\": \"updated\"}");
    }

    /**
     * Rimuove un evento dal database.
     *
     * @param request   DTO contenente l'ID dell'evento da eliminare.
     * @param principal Identità dell'utente loggato.
     * @return JSON di conferma cancellazione.
     * @since 1.2.0
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteEvento(@RequestBody DeleteEventoRequest request, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(principal.getName());
        if (currentUserOpt.isEmpty()) return ResponseEntity.status(403).build();

        AziendaEntity azienda = currentUserOpt.get().getAzienda();
        EventoID eventoID = new EventoID(request.id(), azienda.getId_azienda());

        if (eventoRepository.existsById(eventoID)) {
            eventoRepository.deleteById(eventoID);
            return ResponseEntity.ok().body("{\"status\": \"deleted\"}");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Recupera la lista degli utenti aziendali o dei partecipanti a un evento.
     * <p>
     * Se il parametro 'id' è nullo, restituisce tutti i colleghi dell'azienda per popolare
     * componenti di autocomplete. Se valorizzato, restituisce i soli partecipanti a quell'evento.
     * </p>
     *
     * @param id        ID opzionale di un evento specifico.
     * @param principal Identità utente.
     * @return Lista di {@link UserDTO}.
     * @since 1.4.0
     */
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam(required = false) Integer id, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(principal.getName());
        if (currentUserOpt.isEmpty() || !isAccessibleModulo(currentUserOpt)) return ResponseEntity.status(403).build();

        List<UtenteEntity> resultUsers;

        if (id == null) {
            // Caso 1: Ricerca utenti per autocomplete (Tutti gli utenti azienda)
            resultUsers = userRepository.getAllByAziendaIs(currentUserOpt.get().getAzienda());
        } else {
            // Caso 2: Partecipanti di un evento specifico
            try {
                EventoEntity evento = eventoService.findById(new EventoID(id, currentUserOpt.get().getAzienda().getId_azienda()));
                resultUsers = partecipazioneService.getUtenteEntitiesByEvento(evento);
            } catch (EventoNotFound e) {
                return ResponseEntity.status(404).build();
            }
        }

        // Mapping a DTO per evitare loop infiniti JSON
        List<UserDTO> dtos = resultUsers.stream()
                .map(u -> new UserDTO(u.getId_utente(), u.getNome(), u.getCognome(), u.getEmail()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Recupera tutti gli eventi associati all'azienda del richiedente.
     *
     * @param principal Identità utente.
     * @return Lista di tutti gli eventi aziendali in formato DTO.
     * @since 1.5.0
     */
    @GetMapping("/all")
    public ResponseEntity<?> getEventiAzienda(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        Optional<UtenteEntity> currentUserOpt = customUserDetailsService.findByEmail(principal.getName());
        if (currentUserOpt.isEmpty() || !isAccessibleModulo(currentUserOpt)) return ResponseEntity.status(403).build();
        AziendaEntity azienda = currentUserOpt.get().getAzienda();
        List<EventoEntity> eventi = eventoRepository.findByAzienda(azienda);
        List<EventoDTO> dtos = parse(eventi);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Utility per la conversione massiva di entità Evento in DTO.
     *
     * @param eventi Lista di entità.
     * @return Lista di DTO.
     */
    public static List<EventoDTO> parse(List<EventoEntity> eventi) {
        return eventi.stream()
                .map(e -> new EventoDTO(e.getId_evento(), e.getNome(), e.getLuogo(), e.getData_ora_inizio(), e.getData_fine()))
                .collect(Collectors.toList());
    }

    /**
     * Utility per la conversione di Task in oggetti compatibili con il calendario.
     * Filtra i task già completati.
     *
     * @param tasks Lista di entità Task.
     * @return Lista di DTO eventi.
     */
    public static List<EventoDTO> parseTask(List<TaskEntity> tasks) {
        for(TaskEntity task : tasks) if(task.isCompletato()) tasks.remove(task);
        return tasks.stream()
                .map(e->new EventoDTO(e.getId_task(), e.getTitolo(), "Ufficio", e.getDataCreazione().atStartOfDay(), e.getScadenza().atTime(LocalTime.MAX)))
                .collect(Collectors.toList());
    }

    @Override
    public void disinstallaModulo(AziendaEntity azienda) {
        return;
    }

    /**
     * Data Transfer Object per la rappresentazione sintetica di un evento nel calendario.
     */
    public record EventoDTO(int id_evento, String nome, String luogo, LocalDateTime data_ora_inizio, LocalDateTime data_fine) {}
    
    /**
     * Oggetto di richiesta per l'aggiornamento di un evento.
     */
    public record UpdateEventoRequest(int id, String nome, String luogo, LocalDateTime inizio, LocalDateTime fine, List<Integer> partecipanti){}
    
    /**
     * Oggetto di richiesta per la creazione di un nuovo evento.
     */
    public record CreateEventoRequest(String nome, String luogo, LocalDateTime inizio, LocalDateTime fine, List<Integer> partecipanti) {}
    
    /**
     * Oggetto di richiesta per la cancellazione di un evento.
     */
    public record DeleteEventoRequest(int id) {}
}