package com.modulink.Model.Relazioni.Attivazione;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entità di persistenza <strong>Attivazione</strong>, rappresenta il contratto di utilizzo di un modulo software da parte di un'azienda.
 * <p>
 * Questa classe implementa una relazione Molti-a-Molti tra {@link ModuloEntity} (il prodotto software) e {@link AziendaEntity} (il cliente/tenant).
 * Ogni istanza di questa classe certifica che una specifica azienda ha acquistato o attivato uno specifico modulo.
 * </p>
 * <p>
 * L'entità utilizza una chiave primaria composta definita nella classe {@link AttivazioneID},
 * garantendo che una stessa azienda non possa attivare lo stesso modulo più volte.
 * </p>
 *
 * @see AttivazioneID
 * @see ModuloEntity
 * @see AziendaEntity
 * @author Modulink Team
 * @version 2.3.0
 * @since 1.0.0
 */
@Entity
@IdClass(AttivazioneID.class)
@Table(name = "attivazione", schema = "modulink")
public class AttivazioneEntity {

    /**
     * Riferimento al modulo software attivato.
     * <p>
     * Parte della chiave primaria composta. La cancellazione del modulo comporterebbe
     * la cancellazione a cascata dell'attivazione.
     * </p>
     *
     * @since 1.0.0
     */
    @Id
    @ManyToOne
    @JoinColumn(
            name = "id_modulo",
            foreignKey = @ForeignKey(name = "fk_modulo_azienda")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ModuloEntity modulo;

    /**
     * Riferimento all'azienda che ha attivato il modulo.
     * <p>
     * Parte della chiave primaria composta. Identifica il tenant proprietario della licenza d'uso.
     * </p>
     *
     * @since 1.0.0
     */
    @Id
    @ManyToOne
    @JoinColumn(
            name = "id_azienda",
            foreignKey = @ForeignKey(name = "fk_azienda_modulo")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AziendaEntity azienda;

    /**
     * Costruttore predefinito richiesto dalle specifiche JPA.
     *
     * @since 1.0.0
     */
    public AttivazioneEntity() {}

    /**
     * Costruttore parametrico per creare una nuova attivazione.
     *
     * @param modulo  Il modulo da attivare.
     * @param azienda L'azienda che attiva il modulo.
     * @since 1.0.0
     */
    public AttivazioneEntity(ModuloEntity modulo, AziendaEntity azienda) {
        this.modulo = modulo;
        this.azienda = azienda;
    }

    /**
     * Recupera il modulo associato a questa attivazione.
     *
     * @return L'entità modulo.
     * @since 1.0.0
     */
    public ModuloEntity getModulo() {
        return modulo;
    }

    /**
     * Imposta il modulo per questa attivazione.
     *
     * @param modulo Il nuovo modulo.
     * @since 1.0.0
     */
    public void setModulo(ModuloEntity modulo) {
        this.modulo = modulo;
    }

    /**
     * Recupera l'azienda associata a questa attivazione.
     *
     * @return L'entità azienda.
     * @since 1.0.0
     */
    public AziendaEntity getAzienda() {
        return azienda;
    }

    /**
     * Imposta l'azienda per questa attivazione.
     *
     * @param azienda La nuova azienda.
     * @since 1.0.0
     */
    public void setAzienda(AziendaEntity azienda) {
        this.azienda = azienda;
    }
}
