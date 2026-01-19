package com.modulink.Model.Relazioni.Associazione;

import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Utente.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository JPA per la gestione della persistenza dell'entità {@link AssociazioneEntity}.
 * <p>
 * L'interfaccia fornisce l'accesso ai dati per la tabella di unione tra Utenti e Ruoli, implementando
 * i metodi necessari per la manipolazione di relazioni in un contesto di chiave primaria composta.
 * Estende {@link JpaRepository} utilizzando {@link AssocazioneID} come identificatore.
 * </p>
 *
 * @see AssociazioneEntity
 * @see AssocazioneID
 * @see JpaRepository
 * @author Modulink Team
 * @version 2.0.4
 * @since 1.0.0
 */
public interface AssociazioneRepository extends JpaRepository<AssociazioneEntity, AssocazioneID> {

    /**
     * Rimuove tutte le associazioni ruolo correlate a uno specifico utente.
     * Utilizzato durante la disattivazione o cancellazione di un profilo utente.
     *
     * @param utente L'entità {@link UtenteEntity} destinataria dell'operazione.
     * @since 1.0.0
     */
    void removeAllByUtente(UtenteEntity utente);

    /**
     * Rimuove tutte le associazioni che coinvolgono un determinato ruolo.
     * Fondamentale per la pulizia dei dati prima dell'eliminazione di un ruolo aziendale.
     *
     * @param ruolo L'entità {@link RuoloEntity} da scollegare.
     * @since 1.0.0
     */
    void removeAllByRuolo(RuoloEntity ruolo);

    /**
     * Recupera la lista di tutte le associazioni attive per un dato ruolo.
     * Permette di identificare quali utenti ricoprono una specifica mansione.
     *
     * @param ruolo Il ruolo di riferimento.
     * @return Una lista di {@link AssociazioneEntity}.
     * @since 1.1.0
     */
    List<AssociazioneEntity> findByRuolo(RuoloEntity ruolo);
}