package com.modulink.Model.Azienda;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

/**
 * Service Layer incaricato dell'implementazione della logica di business per il dominio <strong>Azienda</strong>.
 * <p>
 * Questa classe orchestra le interazioni tra i controller REST/MVC e il layer di persistenza ({@link AziendaRepository}).
 * Gestisce i confini transazionali, l'aggregazione dei dati e l'applicazione di regole di validazione business-specific
 * (es. unicità P.IVA, formati contatti).
 * </p>
 * <p>
 * Le operazioni di modifica (scrittura/aggiornamento/cancellazione) sono annotate con {@link Transactional} per garantire
 * l'atomicità e la coerenza (ACID properties), delegando al container Spring la gestione del rollback in caso di eccezioni runtime.
 * </p>
 *
 * @author Modulink Team
 * @version 1.6.0
 * @since 1.0.0
 */
@Service
public class AziendaService {

    private final AziendaRepository aziendaRepository;

    /**
     * Costruttore per Dependency Injection (Constructor Injection).
     * <p>
     * Spring IoC Container inietta automaticamente l'istanza singleton di {@link AziendaRepository}.
     * Questo approccio favorisce l'immutabilità e facilita il testing unitario tramite mock.
     * </p>
     *
     * @param aziendaRepository L'interfaccia DAO per l'accesso ai dati.
     * @since 1.0.0
     */
    public AziendaService(AziendaRepository aziendaRepository) {
        this.aziendaRepository = aziendaRepository;
    }

    /**
     * Recupera un'entità azienda in base alla Partita IVA.
     * <p>
     * Esegue una lookup ottimizzata tramite indice univoco.
     * Il metodo effettua l'unwrapping dell'{@link Optional} restituito dal repository.
     * </p>
     *
     * @param piva La stringa contenente la P.IVA target.
     * @return L'istanza di {@link AziendaEntity} se presente, altrimenti <code>null</code>.
     * @see AziendaRepository#findByPiva(String)
     * @since 1.0.0
     */
    public AziendaEntity getAziendaByPIVA(String piva) {
        Optional<AziendaEntity> azienda = aziendaRepository.findByPiva(piva);
        return azienda.orElse(null);
    }

    /**
     * Persiste una nuova anagrafica aziendale nel sistema.
     * <p>
     * L'operazione è transazionale: in caso di violazione di vincoli (es. duplicate key exception su P.IVA)
     * l'intera transazione viene annullata.
     * L'ID viene generato automaticamente dal database (IDENTITY strategy) al momento del flush.
     * </p>
     *
     * @param azienda L'oggetto DTO/Entity popolato con i dati di registrazione.
     * @return L'entità persistita (attached state) aggiornata con l'ID generato.
     * @since 1.0.0
     */
    @Transactional
    public AziendaEntity registraAzienda(AziendaEntity azienda) {
        return aziendaRepository.save(azienda);
    }

    /**
     * Verifica l'esistenza di un vincolo di unicità sul numero di telefono.
     * <p>
     * Utilizzato tipicamente dai validator dei form di registrazione per fornire feedback immediato all'utente (fail-fast).
     * </p>
     *
     * @param telefono Il recapito telefonico da controllare.
     * @return <code>true</code> se il telefono è già presente in DB, <code>false</code> altrimenti.
     * @since 1.2.5
     */
    public boolean findByTelefono(String telefono) {
        return aziendaRepository.findByTelefono(telefono).isPresent();
    }

    /**
     * Recupera l'elenco completo di tutte le aziende registrate nel sistema.
     * <p>
     * <strong>Attenzione:</strong> Questo metodo può generare carichi elevati su database popolati.
     * Considerare l'uso di paginazione per dataset estesi.
     * </p>
     *
     * @return Una {@link List} contenente tutte le entità azienda.
     * @since 1.1.0
     */
    public List<AziendaEntity> getAllAziende() {
        return aziendaRepository.findAll();
    }

    /**
     * Lookup di un'azienda tramite la sua chiave primaria (ID).
     *
     * @param id L'identificativo intero dell'azienda.
     * @return Un {@link Optional} popolato se l'ID è valido, vuoto altrimenti.
     * @since 1.0.0
     */
    public Optional<AziendaEntity> getAziendaById(int id) {
        return aziendaRepository.findById(id);
    }

    /**
     * Esegue l'aggiornamento (UPDATE) dei dati aziendali.
     * <p>
     * Il metodo utilizza {@link AziendaRepository#save(Object)} che, in contesto JPA,
     * esegue un <code>merge</code> se l'entità possiede un ID esistente.
     * Richiede un contesto transazionale attivo.
     * </p>
     *
     * @param azienda L'entità con i dati modificati.
     * @return L'entità aggiornata e sincronizzata col DB.
     * @since 1.4.0
     */
    @Transactional
    public AziendaEntity updateAzienda(AziendaEntity azienda) {
        return aziendaRepository.save(azienda);
    }

    /**
     * Rimuove fisicamente (HARD DELETE) un'azienda dal sistema.
     * <p>
     * Questa operazione è irreversibile e potrebbe scatenare cancellazioni a cascata
     * se configurate a livello di database (FK constraints con ON DELETE CASCADE).
     * </p>
     *
     * @param id L'ID dell'azienda da eliminare.
     * @throws org.springframework.dao.EmptyResultDataAccessException se l'entità non esiste.
     * @since 1.5.0
     */
    @Transactional
    public void deleteAzienda(int id) {
        aziendaRepository.deleteById(id);
    }
}