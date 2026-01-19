package com.modulink.Controller;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Utente.UtenteEntity;

import java.util.Optional;

/**
 * Classe astratta di base per tutti i Controller di modulo all'interno dell'ecosistema ModuLink.
 * <p>
 * Questa classe definisce il contratto fondamentale e le funzionalità condivise per la gestione dei moduli software.
 * Fornisce meccanismi centralizzati per:
 * <ul>
 *     <li>Identificazione univoca del modulo tramite {@code id}.</li>
 *     <li>Verifica granulare dei permessi di accesso basata sulla relazione tra Utente, Ruolo e Modulo.</li>
 *     <li>Definizione di una procedura di cleanup (disinstallazione) obbligatoria per ogni implementazione.</li>
 * </ul>
 *
 *
 * @author Modulink Team
 * @version 1.5.2
 * @since 1.0.0
 */
public abstract class ModuloController {
    /**
     * Identificativo univoco del modulo gestito dal controller.
     */
    private int id;

    /**
     * Servizio per la logica di business e il controllo accessi dei moduli.
     */
    private final ModuloService moduloService;

    /**
     * Costruttore per inizializzare il controller con il servizio moduli e l'identificativo specifico.
     *
     * @param moduloService Il servizio di gestione moduli.
     * @param id            L'ID univoco del modulo (definito nel database).
     * @since 1.0.0
     */
    public ModuloController(ModuloService moduloService, int id) {
        this.moduloService = moduloService;
        this.id = id;
    }

    /**
     * Verifica se l'utente specificato ha i permessi necessari per accedere alle risorse di questo modulo.
     * <p>
     * Il controllo delega al {@link ModuloService} la verifica della catena di autorizzazione:
     * 1. Il modulo deve essere attivato per l'azienda dell'utente.
     * 2. L'utente deve possedere un ruolo a cui è stata concessa la pertinenza sul modulo.
     * </p>
     *
     * @param user Un {@link Optional} contenente l'utente richiedente.
     * @return true se l'accesso è consentito, false altrimenti.
     * @since 1.0.0
     */
    protected boolean isAccessibleModulo(Optional<UtenteEntity> user) {
        if(user.isEmpty()) return false;
        UtenteEntity utente=user.get();
        return moduloService.isAccessibleModulo(this.id, utente);
    }

    /**
     * Restituisce l'ID del modulo gestito.
     *
     * @return L'identificativo numerico del modulo.
     * @since 1.0.0
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID del modulo gestito.
     *
     * @param id Il nuovo identificativo.
     * @since 1.0.0
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Metodo astratto che deve essere implementato per gestire la logica di pulizia dei dati.
     * <p>
     * Viene invocato dal sistema durante la disattivazione o disinstallazione del modulo per una specifica azienda.
     * Deve contenere la logica per rimuovere o archiviare in modo sicuro tutte le entità create dal modulo
     * nel contesto dell'azienda indicata.
     * </p>
     *
     * @param azienda L'entità {@link AziendaEntity} per cui il modulo viene rimosso.
     * @since 1.1.0
     */
    public abstract void disinstallaModulo(AziendaEntity azienda);
}
