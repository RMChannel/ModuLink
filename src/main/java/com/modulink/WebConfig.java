package com.modulink;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe di configurazione per Spring Web MVC.
 * <p>
 * Personalizza il comportamento di default di Spring MVC, in particolare per quanto riguarda
 * la gestione delle risorse statiche esterne al classpath (es. loghi caricati dagli utenti).
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.0
 * @since 1.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Mappa percorsi URL virtuali a directory fisiche sul server.
     * <p>
     * Questo metodo espone due directory locali come risorse web accessibili:
     * <ul>
     *     <li><code>/azienda-logos/**</code> -> mappa alla cartella fisica "azienda-logos"</li>
     *     <li><code>/user-logos/**</code> -> mappa alla cartella fisica "user-logos"</li>
     * </ul>
     * Utilizza percorsi assoluti per garantire il funzionamento indipendentemente dalla directory di lavoro.
     *
     *
     * @param registry Il registro degli handler delle risorse.
     * @since 1.0.0
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("azienda-logos");
        String uploadPath = uploadDir.toAbsolutePath().toUri().toString();
        if (!uploadPath.endsWith("/")) {
            uploadPath += "/";
        }
        registry.addResourceHandler("/azienda-logos/**")
                .addResourceLocations(uploadPath);
        uploadDir = Paths.get("user-logos");
        uploadPath = uploadDir.toAbsolutePath().toUri().toString();
        if (!uploadPath.endsWith("/")) {
            uploadPath += "/";
        }
        registry.addResourceHandler("/user-logos/**")
                .addResourceLocations(uploadPath);
    }
}
