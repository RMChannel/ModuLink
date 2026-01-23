package com.modulink.Model.SupportForm;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Layer responsabile della logica di business relativa alla gestione delle richieste di supporto.
 * <p>
 * Questa classe agisce come intermediario tra i controller e il repository {@link SupportFormRepository},
 * incapsulando le operazioni di creazione, lettura e cancellazione dei ticket di assistenza.
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.0
 * @since 1.0.0
 */
@Service
public class SupportFormService {

    /**
     * Il repository per l'accesso ai dati persistenti.
     *
     * @since 1.0.0
     */
    private final SupportFormRepository supportFormRepository;

    /**
     * Costruttore per Dependency Injection.
     *
     * @param supportFormRepository L'istanza del repository da iniettare.
     * @since 1.0.0
     */
    public SupportFormService(SupportFormRepository supportFormRepository) {
        this.supportFormRepository = supportFormRepository;
    }

    /**
     * Persiste una nuova richiesta di supporto o aggiorna una esistente.
     * <p>
     * Delega l'operazione al metodo {@link org.springframework.data.repository.CrudRepository#save(Object)}.
     * Se l'entità ha un ID nullo o 0, verrà creato un nuovo record; altrimenti verrà aggiornato il record corrispondente.
     * </p>
     *
     * @param supportFormEntity L'entità del ticket da salvare.
     * @since 1.0.0
     */
    public void save(SupportFormEntity supportFormEntity) {
        supportFormRepository.save(supportFormEntity);
    }

    /**
     * Elimina un ticket di supporto dal sistema.
     * <p>
     * L'operazione è definitiva (Hard Delete).
     * </p>
     *
     * @param id L'identificativo univoco del ticket da rimuovere.
     * @since 1.0.0
     */
    public void delete(int id) {
        supportFormRepository.deleteById(id);
    }

    /**
     * Recupera l'elenco completo di tutte le richieste di supporto registrate.
     * <p>
     * Utilizzato tipicamente nel pannello di amministrazione per visualizzare la coda dei ticket.
     * </p>
     *
     * @return Lista di entità {@link SupportFormEntity}.
     * @since 1.0.0
     */
    public List<SupportFormEntity> findAll() {
        return supportFormRepository.findAll();
    }
}
