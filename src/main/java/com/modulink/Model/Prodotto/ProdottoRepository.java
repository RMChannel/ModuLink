package com.modulink.Model.Prodotto;

import com.modulink.Model.Azienda.AziendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interfaccia DAO per la gestione della persistenza dei prodotti.
 * <p>
 * Estende {@link JpaRepository} utilizzando la chiave composta {@link ProdottoID}.
 * Fornisce metodi per le operazioni CRUD standard e query personalizzate per la gestione multi-tenant,
 * filtrando i dati in base all'azienda di appartenenza.
 * </p>
 *
 * @see ProdottoEntity
 * @see ProdottoID
 * @author Modulink Team
 * @version 1.3.3
 * @since 1.2.0
 */
public interface ProdottoRepository extends JpaRepository<ProdottoEntity, ProdottoID> {

    /**
     * Recupera il prodotto con l'ID locale più alto per una specifica azienda.
     * <p>
     * Questa query è fondamentale per implementare la strategia di auto-incremento manuale "per tenant".
     * Seleziona l'entità prodotto dove l'ID corrisponde al valore massimo presente per quell'azienda.
     * </p>
     *
     * @param azienda L'azienda per cui calcolare il massimo ID.
     * @return L'entità {@link ProdottoEntity} con l'ID più alto, o <code>null</code> se nessun prodotto è presente.
     * @since 1.2.0
     */
    @Query("SELECT p FROM ProdottoEntity p WHERE p.azienda = :azienda AND p.id_prodotto = (SELECT MAX(p.id_prodotto) FROM ProdottoEntity p2 WHERE p2.azienda = :azienda)")
    ProdottoEntity findMaxByAzienda(@Param("azienda") AziendaEntity azienda);

    /**
     * Restituisce la lista completa dei prodotti appartenenti a un'azienda.
     * <p>
     * Metodo derivato dalla naming convention di Spring Data JPA.
     * Esegue una SELECT filtrata per la colonna Foreign Key <code>azienda</code>.
     * </p>
     *
     * @param azienda L'azienda proprietaria.
     * @return Lista di prodotti associati all'azienda.
     * @since 1.2.0
     */
    List<ProdottoEntity> findAllByAzienda(AziendaEntity azienda);

    /**
     * Estrae l'elenco univoco delle categorie merceologiche utilizzate da un'azienda.
     * <p>
     * Esegue una proiezione della colonna <code>categoria</code> applicando la clausola <code>DISTINCT</code>
     * per eliminare i duplicati. Utile per popolare filtri di ricerca o menu a tendina.
     * </p>
     *
     * @param azienda L'azienda di riferimento.
     * @return Lista di stringhe rappresentanti le categorie univoche.
     * @since 1.3.0
     */
    @Query("SELECT DISTINCT p.categoria FROM ProdottoEntity p WHERE p.azienda = :azienda")
    List<String> findAllCategoriesByAzienda(AziendaEntity azienda);

    /**
     * Elimina massivamente tutti i prodotti associati a un'azienda.
     * <p>
     * Utilizzato nelle procedure di disattivazione o cancellazione di un tenant.
     * L'operazione è irreversibile.
     * </p>
     *
     * @param azienda L'azienda i cui prodotti devono essere rimossi.
     * @since 1.4.0
     */
    void deleteAllByAzienda(AziendaEntity azienda);
}
