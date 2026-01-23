package com.modulink.Model.Relazioni.Partecipazione;

import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Eventi.EventoRepository;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Layer per la gestione della logica di business relativa alle partecipazioni agli eventi.
 * <p>
 * Permette di iscrivere utenti agli eventi, recuperare la lista dei partecipanti e annullare le iscrizioni.
 * </p>
 *
 * @author Modulink Team
 * @version 1.8.2
 * @since 1.3.0
 */
@Service
public class PartecipazioneService {
    private final PartecipazioneRepository partecipazioneRepository;


    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param partecipazioneRepository Repository partecipazioni.
     * @since 1.3.0
     */
    public PartecipazioneService(PartecipazioneRepository partecipazioneRepository) {
        this.partecipazioneRepository = partecipazioneRepository;
    }

    /**
     * Iscrive (invita) un utente a partecipare a un evento.
     * Crea un nuovo record di partecipazione nel database.
     *
     * @param eventoEntity L'evento a cui partecipare.
     * @param utenteEntity L'utente che partecipa.
     * @since 1.3.0
     */
    @Transactional
    public void Invita(EventoEntity eventoEntity, UtenteEntity utenteEntity){
        PartecipazioneEntity partecipazioneEntity = new PartecipazioneEntity(utenteEntity.getId_utente(), eventoEntity.getId_evento(), utenteEntity.getAzienda().getId_azienda());
        partecipazioneRepository.save(partecipazioneEntity);
    }

    /**
     * Recupera la lista di tutti gli utenti iscritti a un determinato evento.
     *
     * @param eventoEntity L'evento di cui cercare i partecipanti.
     * @return Lista di {@link UtenteEntity}.
     * @since 1.3.5
     */
    public List<UtenteEntity> getUtenteEntitiesByEvento(EventoEntity eventoEntity){
        List<UtenteEntity> utenteEntities = new ArrayList<>();
        partecipazioneRepository.getPartecipazioneEntitiesByEvento(eventoEntity).forEach(c->{
            utenteEntities.add(c.getUtente());
        });
        return utenteEntities;
    }

    /**
     * Annulla l'iscrizione (rimuove l'invito) di un utente a un evento.
     *
     * @param eventoEntity L'evento in questione.
     * @param utenteEntity L'utente da disiscrivere.
     * @since 1.3.0
     */
    public void RimuoviInvito(EventoEntity eventoEntity, UtenteEntity utenteEntity){
        partecipazioneRepository.removeByUtenteAndEvento(utenteEntity, eventoEntity);
    }

}