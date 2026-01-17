package com.modulink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Punto di ingresso (Entry Point) principale dell'applicazione backend <strong>ModuLink</strong>.
 * <p>
 * Questa classe avvia il framework Spring Boot, che si occupa di:
 * <ul>
 * <li>Configurare automaticamente l'applicazione in base alle dipendenze presenti (Auto-configuration).</li>
 * <li>Scansionare il pacchetto corrente e i sotto-pacchetti alla ricerca di componenti Spring (@Component, @Service, @Repository, @Controller).</li>
 * <li>Avviare il container embedded (Tomcat) per servire l'applicazione web.</li>
 * </ul>
 * <p>
 * L'annotazione {@link SpringBootApplication} funge da meta-annotazione che include:
 * <ul>
 * <li>{@code @Configuration}: Indica che la classe può definire bean nel contesto dell'applicazione.</li>
 * <li>{@code @EnableAutoConfiguration}: Abilita il meccanismo di configurazione automatica di Spring Boot.</li>
 * <li>{@code @ComponentScan}: Abilita la scansione dei componenti nel package di base.</li>
 * </ul>
 *
 * @author Modulink Team
 * @version 1.0
 */

@SpringBootApplication
@EnableCaching
public class ModuLinkApplication {

    /**
     * Metodo principale che lancia l'applicazione Java.
     * <p>
     * Invoca {@link SpringApplication#run(Class, String...)} per inizializzare
     * l'ApplicationContext di Spring e avviare il ciclo di vita dell'applicazione.
     *
     * @param args Argomenti da riga di comando passati all'avvio (opzionali).
     */
    public static void main(String[] args) {
        SpringApplication.run(ModuLinkApplication.class, args);
    }

}