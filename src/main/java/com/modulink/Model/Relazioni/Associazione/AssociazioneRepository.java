package com.modulink.Model.Relazioni.Associazione;

import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interfaccia <strong>Repository</strong> per la gestione della persistenza dell'entità {@link AssociazioneEntity}.
 * <p>
 * Questa interfaccia estende {@link JpaRepository} di Spring Data JPA, fornendo automaticamente
 * l'implementazione dei metodi standard CRUD (Create, Read, Update, Delete) e di paginazione
 * per la tabella di associazione intermedia tra Utenti e Ruoli.
 * <p>
 * Essendo l'entità mappata su una architettura a <strong>Chiave Primaria Composta</strong>,
 * il repository è tipizzato su {@link AssocazioneID}.
 *
 * @see AssociazioneEntity
 * @see AssocazioneID
 * @see JpaRepository
 * @author Modulink Team
 * @version 1.0
 */
public interface AssociazioneRepository extends JpaRepository<AssociazioneEntity, AssocazioneID> {
    void removeAllByUtente(UtenteEntity utente);
}