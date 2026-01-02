package com.modulink.Model.Azienda;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service layer per la gestione della logica di business relativa all'entità {@link AziendaEntity}.
 * <p>
 * Questa classe agisce come intermediario tra il Controller e il Repository,
 * incapsulando le regole di gestione dei dati aziendali, come la generazione
 * manuale dell'ID e le verifiche di univocità sui campi sensibili.
 *
 * @author Modulink Team
 * @version 1.2
 */
@Service
public class AziendaService {
    private final AziendaRepository aziendaRepository;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param aziendaRepository Repository per l'accesso ai dati dell'azienda.
     */
    public AziendaService(AziendaRepository aziendaRepository) {
        this.aziendaRepository = aziendaRepository;
    }

    /**
     * Recupera i dettagli di un'azienda tramite la Partita IVA.
     * <p>
     * Utilizza il repository per cercare l'entità. Se l'azienda non viene trovata,
     * gestisce l'{@link Optional} restituendo {@code null}.
     *
     * @param piva La Partita IVA da cercare.
     * @return L'istanza di {@link AziendaEntity} se trovata, altrimenti {@code null}.
     */
    public AziendaEntity getAziendaByPIVA(String piva) {
        Optional<AziendaEntity> azienda = aziendaRepository.findByPiva(piva);
        return azienda.orElse(null);
    }

    /**
     * Persiste una nuova azienda nel database.
     * <p>
     * Il metodo implementa una logica custom per la generazione dell'ID:
     * calcola l'ID massimo attuale e lo incrementa di 1.
     * L'operazione è marcata come {@link Transactional} per garantire l'integrità dei dati.
     *
     * @param azienda L'entità azienda con i dati da salvare (escluso l'ID).
     * @return L'entità salvata, completa di ID assegnato.
     */
    @Transactional
    public AziendaEntity registraAzienda(AziendaEntity azienda) {
        return aziendaRepository.save(azienda);
    }

    /**
     * Verifica l'esistenza di un'azienda associata al numero di telefono specificato.
     * <p>
     * Metodo di utilità per validare l'univocità del contatto telefonico
     * durante la fase di registrazione o modifica anagrafica.
     *
     * @param telefono Il numero di telefono da verificare.
     * @return {@code true} se esiste già un'azienda con questo telefono, {@code false} altrimenti.
     */
    public boolean findByTelefono(String telefono) {
        return aziendaRepository.findByTelefono(telefono).isPresent();
    }
}