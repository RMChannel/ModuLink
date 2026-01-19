package com.modulink.Model.Prodotto;

import com.modulink.Model.Azienda.AziendaEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer responsabile della logica di business relativa alla gestione del magazzino prodotti.
 * <p>
 * Questa classe orchestra le operazioni CRUD sui prodotti, implementando regole specifiche
 * per il multi-tenancy, come la generazione sequenziale degli ID per singola azienda
 * e la validazione dell'appartenenza delle risorse.
 * </p>
 *
 * @author Modulink Team
 * @version 1.6.2
 * @since 1.2.0
 */
@Service
public class ProdottoService {

    /**
     * Repository per l'accesso ai dati persistenti.
     *
     * @since 1.2.0
     */
    private final ProdottoRepository prodottoRepository;

    /**
     * Costruttore per Dependency Injection.
     *
     * @param prodottoRepository L'istanza iniettata del repository prodotti.
     * @since 1.2.0
     */
    public ProdottoService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }

    /**
     * Crea e persiste un nuovo prodotto, calcolando automaticamente il prossimo ID disponibile.
     * <p>
     * Questo metodo implementa una logica di autoincremento personalizzata per tenant.
     * Esegue una query per trovare l'ID massimo corrente per l'azienda specificata e assegna
     * al nuovo prodotto il valore <code>MAX(ID) + 1</code>. Se non esistono prodotti, l'ID parte da 0.
     * L'operazione è atomica grazie all'annotazione {@link Transactional}.
     * </p>
     *
     * @param prodotto L'entità prodotto da salvare (con ID ancora non assegnato).
     * @since 1.2.0
     */
    @Transactional
    public void save(ProdottoEntity prodotto) {
        ProdottoEntity pMax=prodottoRepository.findMaxByAzienda(prodotto.getAzienda());
        if(pMax==null) prodotto.setId_prodotto(0);
        else prodotto.setId_prodotto(pMax.getId_prodotto()+1);
        prodottoRepository.save(prodotto);
    }

    /**
     * Elimina un prodotto specifico dal sistema.
     * <p>
     * Verifica preventivamente l'esistenza del prodotto tramite la chiave composta (ID + Azienda).
     * Se il prodotto non viene trovato, viene sollevata un'eccezione controllata.
     * </p>
     *
     * @param idProdotto L'ID locale del prodotto da eliminare.
     * @param azienda    L'azienda proprietaria (necessaria per completare la chiave primaria).
     * @throws ProdottoNotFoundException Se il prodotto specificato non esiste per l'azienda data.
     * @since 1.2.0
     */
    @Transactional
    public void delete(int idProdotto, AziendaEntity azienda) throws ProdottoNotFoundException {
        Optional<ProdottoEntity> prodotto = prodottoRepository.findById(new ProdottoID(idProdotto, azienda.getId_azienda()));
        if(prodotto.isEmpty()) throw new ProdottoNotFoundException();
        prodottoRepository.delete(prodotto.get());
    }

    /**
     * Aggiorna i dati di un prodotto esistente.
     * <p>
     * Utilizza {@link ProdottoRepository#save(Object)} che, in presenza di una chiave primaria valorizzata,
     * esegue un <code>merge</code> dell'entità nel contesto di persistenza.
     * </p>
     *
     * @param prodotto L'entità prodotto con i dati aggiornati.
     * @since 1.2.0
     */
    @Transactional
    public void updateProdotto(ProdottoEntity prodotto) {
        prodottoRepository.save(prodotto);
    }

    /**
     * Recupera l'inventario completo dei prodotti di un'azienda.
     *
     * @param azienda L'azienda di riferimento.
     * @return Lista di tutte le entità prodotto associate.
     * @since 1.2.0
     */
    public List<ProdottoEntity> findAllByAzienda(AziendaEntity azienda) {
        return prodottoRepository.findAllByAzienda(azienda);
    }

    /**
     * Recupera l'elenco delle categorie merceologiche definite da un'azienda.
     *
     * @param azienda L'azienda di riferimento.
     * @return Lista di stringhe univoche rappresentanti le categorie.
     * @since 1.3.0
     */
    public List<String> findAllCategoriesByAzienda(AziendaEntity azienda) {
        return prodottoRepository.findAllCategoriesByAzienda(azienda);
    }

    /**
     * Cerca un singolo prodotto tramite la sua chiave primaria composta.
     *
     * @param id L'oggetto {@link ProdottoID} contenente ID locale e ID azienda.
     * @return L'entità trovata.
     * @throws ProdottoNotFoundException Se nessun record corrisponde alla chiave fornita.
     * @since 1.2.0
     */
    public ProdottoEntity findById(ProdottoID id) throws ProdottoNotFoundException {
        Optional<ProdottoEntity> prodotto = prodottoRepository.findById(id);
        if(prodotto.isEmpty()) throw new ProdottoNotFoundException();
        return prodotto.get();
    }

    /**
     * Rimuove tutti i prodotti associati a un'azienda.
     * <p>
     * Esegue una cancellazione massiva e forza il flush del contesto di persistenza
     * per rendere effettive le modifiche immediatamente.
     * </p>
     *
     * @param azienda L'azienda target della pulizia.
     * @since 1.4.0
     */
    @Transactional
    public void deleteAllByAzienda(AziendaEntity azienda) {
        prodottoRepository.deleteAllByAzienda(azienda);
        prodottoRepository.flush();
    }
}
