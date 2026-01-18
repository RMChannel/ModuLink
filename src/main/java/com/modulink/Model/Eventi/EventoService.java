package com.modulink.Model.Eventi;
import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer per la gestione del ciclo di vita degli eventi aziendali.
 * <p>
 * Questa classe implementa la business logic per la creazione, il recupero e la manipolazione
 * delle entità {@link EventoEntity}. Gestisce le transazioni database tramite l'annotazione {@link Transactional},
 * assicurando che operazioni complesse siano atomiche.
 * </p>
 * <p>
 * Integra la gestione delle eccezioni custom come {@link EventoNotFound} per fornire feedback
 * semantici ai layer superiori.
 * </p>
 *
 * @author Modulink Team
 * @version 1.8.2
 * @since 1.2.0
 */
@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    /**
     * Costruttore per Dependency Injection.
     *
     * @param eventoRepository Repository per l'accesso ai dati degli eventi.
     * @since 1.2.0
     */
    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    /**
     * Persiste un nuovo evento nel database, gestendo la generazione manuale dell'ID.
     * <p>
     * Questo metodo calcola il prossimo identificativo locale disponibile per l'azienda
     * interrogando il repository per l'evento con ID massimo ({@link EventoRepository#findMaxIdByAzienda(AziendaEntity)}).
     * Se non esistono eventi, l'ID parte da 0.
     * </p>
     * <p>
     * L'operazione è transazionale per garantire che il calcolo e il salvataggio avvengano atomicamente.
     * </p>
     *
     * @param eventoEntity L'entità evento da salvare (senza ID impostato).
     * @return L'entità evento persistita, completa di ID assegnato.
     * @since 1.2.0
     */
    @Transactional
    public EventoEntity create(EventoEntity eventoEntity) {
        EventoEntity e=eventoRepository.findMaxIdByAzienda(eventoEntity.getAzienda());
        int maxId;
        if(e==null) maxId=0;
        else maxId=e.getId_evento()+1;
        eventoEntity.setId_evento(maxId);
        return eventoRepository.save(eventoEntity);
    }

    /**
     * Recupera la lista di eventi pertinenti a un utente specifico.
     * <p>
     * Aggrega sia gli eventi creati dall'utente che quelli a cui partecipa.
     * </p>
     *
     * @param utente L'utente per cui filtrare gli eventi.
     * @return Lista di eventi correlati.
     * @see EventoRepository#findAllByUtente(UtenteEntity)
     * @since 1.4.0
     */
    @Transactional
    public List<EventoEntity> findAllByUtente(UtenteEntity utente) {
        return eventoRepository.findAllByUtente(utente);
    }


    /**
     * Recupera tutti gli eventi associati a una determinata azienda.
     *
     * @param aziendaEntity L'azienda target.
     * @return Lista di eventi dell'azienda.
     * @since 1.2.0
     */
    @Transactional
    public List<EventoEntity> findByAzienda(AziendaEntity aziendaEntity) {
        return eventoRepository.findByAzienda(aziendaEntity);
    }

    /**
     * Cerca un evento tramite la sua chiave primaria composta.
     * <p>
     * Se l'evento non viene trovato, lancia un'eccezione runtime {@link EventoNotFound}.
     * </p>
     *
     * @param eventoID Oggetto chiave primaria composta (ID evento + ID azienda).
     * @return L'entità trovata.
     * @throws EventoNotFound Se l'evento non esiste nel DB.
     * @since 1.2.0
     */
    public EventoEntity findById(EventoID eventoID) throws EventoNotFound {
        Optional<EventoEntity> evento = eventoRepository.findById(eventoID);
        if (evento.isPresent()) {
            return evento.get();
        }else  {
            throw new EventoNotFound("Evento non trovato");
        }
    }

    /**
     * Aggiorna i dati di un evento esistente.
     * <p>
     * Funzionalmente equivalente a {@link #create(EventoEntity)}, ma semanticamente distinto
     * per chiarezza del codice client.
     * </p>
     *
     * @param evento L'entità con i dati aggiornati.
     * @since 1.3.0
     */
    public void update(EventoEntity evento) {
        eventoRepository.save(evento);
    }

    /**
     * Rimuove tutti gli eventi associati a un'azienda.
     * <p>
     * Esegue una cancellazione massiva e forza il flush del contesto di persistenza
     * per rendere immediate le modifiche nel DB. Include logging a console per audit (livello INFO).
     * </p>
     *
     * @param aziendaEntity L'azienda i cui eventi devono essere eliminati.
     * @since 1.5.0
     */
    @Transactional
    public void deleteAllByAzienda(AziendaEntity aziendaEntity) {
        eventoRepository.deleteAllByAzienda(aziendaEntity);
        eventoRepository.flush();
    }
}
