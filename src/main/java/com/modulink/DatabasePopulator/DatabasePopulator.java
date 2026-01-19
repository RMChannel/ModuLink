package com.modulink.DatabasePopulator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Componente di configurazione dedicato al bootstrap e al seeding del database del sistema Modulink.
 * <p>
 * Questa classe gestisce l'esecuzione di procedure di popolamento dati all'avvio dell'applicazione.
 * Attraverso l'uso di un bean {@link CommandLineRunner}, permette di iniettare dati di sistema essenziali
 * (come moduli core, ruoli predefiniti o configurazioni di test) prima che l'applicazione inizi
 * a servire le richieste HTTP.
 * </p>
 * <p>
 * Il processo è governato da una proprietà di configurazione che ne permette l'attivazione selettiva,
 * fondamentale per distinguere tra ambienti di sviluppo, testing e produzione.
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.4
 * @since 1.0.0
 */
@Configuration
public class DatabasePopulator {
    /**
     * Inizializza il database eseguendo la logica definita in {@link DataInitializerService}.
     * <p>
     * Il metodo viene invocato automaticamente da Spring Boot dopo che l'ApplicationContext è stato caricato.
     * L'esecuzione è condizionata dal valore della proprietà <code>activate.databasepop</code> definita
     * nei file <code>application.properties</code> o nelle variabili d'ambiente.
     * </p>
     *
     * @param dataInitializerService Il servizio che contiene la logica procedurale di inserimento dati.
     * @param activate               Flag booleano iniettato via {@link Value} per abilitare/disabilitare il populator.
     * @return Un'istanza di {@link CommandLineRunner} pronta per l'esecuzione.
     * @since 1.0.0
     */
    @Bean
    CommandLineRunner initDatabase(DataInitializerService dataInitializerService, @Value("${activate.databasepop}") boolean activate) {
        return args -> {
            if(activate) {
                dataInitializerService.runInitialization();
                System.out.println("Database popolato con successo");
            }
        };
    }
}
