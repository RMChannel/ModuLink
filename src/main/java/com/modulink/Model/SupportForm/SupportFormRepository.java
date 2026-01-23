package com.modulink.Model.SupportForm;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaccia DAO per la gestione della persistenza delle richieste di supporto.
 * <p>
 * Estende {@link JpaRepository} per fornire funzionalità CRUD standard (Create, Read, Update, Delete)
 * e di paginazione sull'entità {@link SupportFormEntity}.
 * L'implementazione concreta è generata a runtime dal framework Spring Data JPA.
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.1
 * @since 1.0.0
 */
public interface SupportFormRepository extends JpaRepository<SupportFormEntity, Integer> {
}
