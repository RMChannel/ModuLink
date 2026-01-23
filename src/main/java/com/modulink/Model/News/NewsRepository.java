package com.modulink.Model.News;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaccia di persistenza per l'entità {@link NewsEntity}.
 * <p>
 * Estende {@link JpaRepository} ereditando tutte le funzionalità standard per l'accesso ai dati (CRUD),
 * la paginazione e l'ordinamento, senza necessità di implementazione manuale.
 * </p>
 * <p>
 * Viene gestita automaticamente dal container Spring Data JPA al momento dell'avvio dell'applicazione.
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.1
 * @since 1.0.0
 */
public interface NewsRepository extends JpaRepository<NewsEntity, Integer> {
}
