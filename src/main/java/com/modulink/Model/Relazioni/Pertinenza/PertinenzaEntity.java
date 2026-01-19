package com.modulink.Model.Relazioni.Pertinenza;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entità di persistenza <strong>Pertinenza</strong>, che definisce i permessi di accesso ai moduli.
 * <p>
 * Questa entità rappresenta l'associazione tra un {@link RuoloEntity} e un {@link ModuloEntity} (tramite {@link AttivazioneEntity}).
 * Di fatto, stabilisce se un certo ruolo aziendale ha il permesso di accedere e utilizzare un determinato modulo acquistato dall'azienda.
 * </p>
 * <p>
 * La chiave primaria è composta, definita in {@link PertinenzaID}.
 * </p>
 *
 * @see PertinenzaID
 * @see RuoloEntity
 * @see AttivazioneEntity
 * @author Modulink Team
 * @version 2.4.1
 * @since 1.0.0
 */
@Entity
@IdClass(PertinenzaID.class)
@Table(name = "pertinenza", schema="modulink")
public class PertinenzaEntity {

    /**
     * ID del Ruolo a cui è assegnato il permesso.
     * Parte della chiave composta.
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "ID_Ruolo")
    public int id_ruolo;

    /**
     * ID del Modulo oggetto del permesso.
     * Parte della chiave composta.
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "ID_Modulo")
    public int id_modulo;

    /**
     * ID dell'Azienda (Tenant).
     * Parte della chiave composta per garantire l'integrità multi-tenant.
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "ID_Azienda")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public int id_azienda;

    /**
     * Relazione Many-To-One con il Ruolo.
     * Mappata in sola lettura.
     *
     * @since 1.0.0
     */
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_Ruolo", referencedColumnName = "ID_Ruolo", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RuoloEntity ruolo;

    /**
     * Relazione Many-To-One con l'Attivazione del modulo.
     * Collega il permesso al fatto che il modulo sia effettivamente attivo per l'azienda.
     * Mappata in sola lettura.
     *
     * @since 1.0.0
     */
    @ManyToOne

    @JoinColumns({
            @JoinColumn(name = "ID_Modulo", referencedColumnName = "ID_Modulo", insertable = false, updatable = false),
            @JoinColumn(name = "ID_Azienda", referencedColumnName = "ID_Azienda", insertable = false, updatable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AttivazioneEntity attivazione;

    /**
     * Costruttore vuoto richiesto da JPA.
     *
     * @since 1.0.0
     */
    public PertinenzaEntity(){}

    /**
     * Costruttore parametrico per creare un nuovo permesso.
     *
     * @param id_ruolo   ID del ruolo.
     * @param id_modulo  ID del modulo.
     * @param id_azienda ID dell'azienda.
     * @since 1.0.0
     */
    public PertinenzaEntity(int id_ruolo, int id_modulo, int id_azienda) {
        this.id_ruolo = id_ruolo;
        this.id_modulo = id_modulo;
        this.id_azienda = id_azienda;
    }

    /**
     * Recupera l'ID del ruolo.
     *
     * @return ID ruolo.
     * @since 1.0.0
     */
    public int getId_ruolo() {
        return id_ruolo;
    }

    /**
     * Imposta l'ID del ruolo.
     *
     * @param id_ruolo Nuovo ID ruolo.
     * @since 1.0.0
     */
    public void setId_ruolo(int id_ruolo) {
        this.id_ruolo = id_ruolo;
    }

    /**
     * Recupera l'ID del modulo.
     *
     * @return ID modulo.
     * @since 1.0.0
     */
    public int getId_modulo() {
        return id_modulo;
    }

    /**
     * Imposta l'ID del modulo.
     *
     * @param id_modulo Nuovo ID modulo.
     * @since 1.0.0
     */
    public void setId_modulo(int id_modulo) {
        this.id_modulo = id_modulo;
    }

    /**
     * Recupera l'ID dell'azienda.
     *
     * @return ID azienda.
     * @since 1.0.0
     */
    public int getId_azienda() {
        return id_azienda;
    }

    /**
     * Imposta l'ID dell'azienda.
     *
     * @param id_azienda Nuovo ID azienda.
     * @since 1.0.0
     */
    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    /**
     * Recupera l'entità Ruolo associata.
     *
     * @return Oggetto RuoloEntity.
     * @since 1.0.0
     */
    public RuoloEntity getRuolo() {
        return ruolo;
    }

    /**
     * Associa l'entità Ruolo.
     *
     * @param ruolo Oggetto RuoloEntity.
     * @since 1.0.0
     */
    public void setRuolo(RuoloEntity ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * Recupera l'entità Attivazione associata.
     *
     * @return Oggetto AttivazioneEntity.
     * @since 1.0.0
     */
    public AttivazioneEntity getAttivazione() {
        return attivazione;
    }

    /**
     * Associa l'entità Attivazione.
     *
     * @param attivazione Oggetto AttivazioneEntity.
     * @since 1.0.0
     */
    public void setAttivazione(AttivazioneEntity attivazione) {
        this.attivazione = attivazione;
    }

    /**
     * Helper method per recuperare direttamente l'azienda dall'attivazione.
     *
     * @return Oggetto AziendaEntity.
     * @since 1.0.0
     */
    public AziendaEntity getAzienda() {
        return attivazione.getAzienda();
    }

    /**
     * Helper method per recuperare direttamente il modulo dall'attivazione.
     *
     * @return Oggetto ModuloEntity.
     * @since 1.0.0
     */
    public ModuloEntity getModulo() {
        return attivazione.getModulo();
    }
}
