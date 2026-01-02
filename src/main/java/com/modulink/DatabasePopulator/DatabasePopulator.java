package com.modulink.DatabasePopulator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabasePopulator {
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
