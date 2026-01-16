package com.modulink;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=80")
class RunningTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    void applicationShouldRespondOnPort80() {
        assertThat(port).isEqualTo(80);

        // Effettua una chiamata GET alla root (o a /actuator/health se preferisci)
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:80/", String.class);

        // Verifica che la risposta non sia un errore di connessione (es. 200 OK o 404 Not Found ma raggiungibile)
        // Se la porta fosse chiusa, questo test fallirebbe con un'eccezione I/O prima di arrivare qui
        assertThat(response.getStatusCode().isError()).isFalse();

        System.out.println("✅ Successo: L'applicazione risponde sulla porta " + port);
    }
}