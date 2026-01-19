package com.modulink.Model.News;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Layer per la gestione della logica di business relativa alle Notizie.
 * <p>
 * Questa classe funge da intermediario tra il controller e il repository {@link NewsRepository},
 * esponendo metodi transazionali per la creazione, lettura e cancellazione delle news.
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.2
 * @since 1.0.0
 */
@Service
public class NewsService {

    /**
     * Il repository iniettato per l'accesso ai dati.
     *
     * @since 1.0.0
     */
    private final NewsRepository newsRepository;

    /**
     * Costruttore per Dependency Injection.
     *
     * @param newsRepository L'istanza del repository.
     * @since 1.0.0
     */
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    /**
     * Recupera l'elenco completo di tutte le notizie presenti nel sistema.
     * <p>
     * Utilizza il metodo standard {@link org.springframework.data.jpa.repository.JpaRepository#findAll()}.
     * Non applica paginazione, quindi attenzione a potenziali problemi di performance con dataset molto ampi.
     * </p>
     *
     * @return Lista di tutte le entità {@link NewsEntity}.
     * @since 1.0.0
     */
    public List<NewsEntity> findAll() {
        return newsRepository.findAll();
    }

    /**
     * Salva o aggiorna una notizia nel database.
     * <p>
     * L'operazione è annotata come {@link Transactional} (di default readOnly=false) per garantire l'atomicità
     * del commit sul database. Se l'entità possiede già un ID esistente, verrà eseguito un update (merge),
     * altrimenti un insert (persist).
     * </p>
     *
     * @param news L'entità notizia da persistere.
     * @since 1.0.0
     */
    @Transactional
    public void save(NewsEntity news) {
        newsRepository.save(news);
    }

    /**
     * Elimina una notizia dal sistema dato il suo ID.
     * <p>
     * Operazione transazionale distruttiva (Hard Delete).
     * </p>
     *
     * @param id L'identificativo univoco della notizia da rimuovere.
     * @since 1.0.0
     */
    @Transactional
    public void delete(int id) {
        newsRepository.deleteById(id);
    }
}
