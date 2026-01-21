package com.modulink.Controller.Register;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test di integrazione per il processo di registrazione (RegisterController).
 * Simula le richieste del browser per verificare la validazione dei dati.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test Case TC1_GDU1: Verifica che la registrazione fallisca se il nome è vuoto.
     * Input: Nome="" (Vuoto)
     * Output atteso: Errore di validazione sul campo nome.
     */
    @Test
    @Order(1)
    public void testTC1_GDU1_NomeVuoto() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione (richiesto dallo Step 2)
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "") // Valore di test TC1_GDU1
                        .param("cognome", "Rossi")
                        .param("email", "m.rossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123.")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk()) // L'app risponde 200 OK ricaricando la pagina del form
                .andExpect(view().name("register/RegistraUtente")) // Rimane sulla pagina di registrazione
                .andExpect(model().hasErrors()) // Confermiamo che ci sono errori di validazione
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "nome")) // L'errore è sul campo nome
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "nome", "Size"));
    }


    /**
     * Test Case TC2_GDU1: Verifica che la registrazione fallisca se il nome è troppo corto.
     * Input: Nome="M" (lunghezza 1)
     * Output atteso: Errore di validazione sul campo nome.
     */
    @Test
    @Order(2)
    public void testTC2_GDU1_NomeTroppoCorto() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione (richiesto dallo Step 2)
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "M") // Valore di test TC2_GDU1
                        .param("cognome", "Rossi")
                        .param("email", "m.rossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123.")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk()) // L'app risponde 200 OK ricaricando la pagina del form
                .andExpect(view().name("register/RegistraUtente")) // Rimane sulla pagina di registrazione
                .andExpect(model().hasErrors()) // Confermiamo che ci sono errori di validazione
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "nome")) // L'errore è sul campo nome
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "nome", "Size"));
    }

    /**
     * Test Case TC3_GDU1: Verifica che la registrazione fallisca se il nome è troppo lungo.
     * Input: Nome="AlessandroRobertoMariangelaDanieleArjelAleksandreGiovanni" (lunghezza 57)
     * Output atteso: Errore di validazione sul campo nome.
     */
    @Test
    @Order(3)
    public void testTC3_GDU1_NomeTroppoLungo() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione (richiesto dallo Step 2)
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "AlessandroRobertoMariangelaDanieleArjelAleksandreGiovanni") // Valore di test TC3_GDU1
                        .param("cognome", "Rossi")
                        .param("email", "m.rossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123.")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk()) // L'app risponde 200 OK ricaricando la pagina del form
                .andExpect(view().name("register/RegistraUtente")) // Rimane sulla pagina di registrazione
                .andExpect(model().hasErrors()) // Confermiamo che ci sono errori di validazione
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "nome")) // L'errore è sul campo nome
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "nome", "Size"));
    }

    /**
     * Test Case TC4_GDU1: Verifica che la registrazione fallisca se il cognome è vuoto.
     * Input: Cognome="" (Vuoto)
     * Output atteso: Errore di validazione sul campo cognome.
     */
    @Test
    @Order(4)
    public void testTC4_GDU1_CognomeVuoto() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione (richiesto dallo Step 2)
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "") // Valore di test TC4_GDU1
                        .param("email", "m.rossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123.")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk()) // L'app risponde 200 OK ricaricando la pagina del form
                .andExpect(view().name("register/RegistraUtente")) // Rimane sulla pagina di registrazione
                .andExpect(model().hasErrors()) // Confermiamo che ci sono errori di validazione
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "cognome")) // L'errore è sul campo cognome
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "cognome", "Size"));
    }

    /**
     * Test Case TC5_GDU1: Verifica che la registrazione fallisca se il cognome è troppo corto.
     * Input: Cognome="R" (Lunghezza 1)
     * Output atteso: Errore di validazione sul campo cognome.
     */
    @Test
    @Order(5)
    public void testTC5_GDU1_CognomeTroppoCorto() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione (richiesto dallo Step 2)
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "R") // Valore di test TC5_GDU1
                        .param("email", "m.rossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123.")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk()) // L'app risponde 200 OK ricaricando la pagina del form
                .andExpect(view().name("register/RegistraUtente")) // Rimane sulla pagina di registrazione
                .andExpect(model().hasErrors()) // Confermiamo che ci sono errori di validazione
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "cognome")) // L'errore è sul campo cognome
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "cognome", "Size"));
    }

    /**
     * Test Case TC6_GDU1: Verifica che la registrazione fallisca se il cognome è troppo lungo.
     * Input: Cognome="RossiCitoBuziCarpentieriChikviladzeCianelliPellegrino" (Lunghezza 53)
     * Output atteso: Errore di validazione sul campo cognome.
     */
    @Test
    @Order(6)
    public void testTC6_GDU1_CognomeTroppoLungo() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione (richiesto dallo Step 2)
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "RossiCitoBuziCarpentieriChikviladzeCianelliPellegrino") // Valore di test TC6_GDU1
                        .param("email", "m.rossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123.")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk()) // L'app risponde 200 OK ricaricando la pagina del form
                .andExpect(view().name("register/RegistraUtente")) // Rimane sulla pagina di registrazione
                .andExpect(model().hasErrors()) // Confermiamo che ci sono errori di validazione
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "cognome")) // L'errore è sul campo cognome
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "cognome", "Size"));
    }

    /**
     * Test Case TC7_GDU1: Verifica che la registrazione fallisca se l'email è vuota.
     * Input: Email="" (Vuoto)
     * Output atteso: Errore di validazione sul campo email.
     */
    @Test
    @Order(7)
    public void testTC7_GDU1_EmailVuota() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione (richiesto dallo Step 2)
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "") // Valore di test TC7_GDU1
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123.")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "email"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "email", "Pattern"));
    }

    /**
     * Test Case TC8_GDU1: Verifica che la registrazione fallisca se l'email è troppo corta.
     * Input: Email="M" (Lunghezza 1)
     * Output atteso: Errore di validazione sul campo email.
     */
    @Test
    @Order(8)
    public void testTC8_GDU1_EmailTroppoCorta() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione (richiesto dallo Step 2)
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "M") // Valore di test TC8_GDU1
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "email"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "email", "Pattern"));
    }

    /**
     * Test Case TC9_GDU1: Verifica che la registrazione fallisca se l'email è troppo lunga.
     * Input: Email="mario.rossi.con.un.nome.veramente.molto.molto.lungo@gmail.com" (Lunghezza 60)
     * Output atteso: Errore di validazione sul campo email.
     */
    @Test
    @Order(9)
    public void testTC9_GDU1_EmailTroppoLunga() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "mario.rossi.con.un.nome.veramente.molto.molto.lungo@gmail.com") // Valore di test TC9_GDU1
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123.")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "email"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "email", "Size"));
    }

    /**
     * Test Case TC10_GDU1: Verifica che la registrazione fallisca se l'email esiste già.
     * Input: Email="robbencito@gmail.com" (Già esistente nel database di test)
     * Output atteso: Errore di validazione sul campo email (mail.found).
     */
    @Test
    @Order(10)
    public void testTC10_GDU1_EmailGiaEsistente() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "robbencito@gmail.com") // Valore di test TC10_GDU1
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "email"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "email", "mail.found"));
    }

    /**
     * Test Case TC11_GDU1: Verifica che la registrazione fallisca se il formato dell'email non è valido.
     * Input: Email="MarioRossi.gmail.it" (Manca @)
     * Output atteso: Errore di validazione sul campo email (Pattern).
     */
    @Test
    @Order(11)
    public void testTC11_GDU1_EmailFormatoNonValido() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossi.gmail.it") // Valore di test TC11_GDU1
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "email"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "email", "Pattern"));
    }

    /**
     * Test Case TC12_GDU1: Verifica che la registrazione fallisca se il formato del telefono non è valido.
     * Input: Telefono="33337698" (Troppo corto / formato errato)
     * Output atteso: Errore di validazione sul campo telefonoutente (Pattern).
     */
    @Test
    @Order(12)
    public void testTC12_GDU1_TelefonoFormatoNonValido() throws Exception {
        // 1. Prepariamo il file immagine simulato
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // 2. Prepariamo l'oggetto Azienda in sessione
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        // 3. Eseguiamo la richiesta POST simulando il browser
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossi@gmail.com")
                        .param("telefonoutente", "33337698") // Valore di test TC12_GDU1
                        .param("password", "Password123")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // 4. Verifichiamo le aspettative
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "telefonoutente"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "telefonoutente", "Pattern"));
    }

    /**
     * Test Case TC13_GDU1: Verifica che la registrazione fallisca se la password è vuota.
     * Input: Password="" (Vuoto)
     * Output atteso: Errore di validazione sul campo password.
     */
    @Test
    @Order(13)
    public void testTC13_GDU1_PasswordVuota() throws Exception {
        MockMultipartFile immagine = new MockMultipartFile("immagineProfilo", "Foto.jpg", "image/jpeg", "fake image content".getBytes());
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "") // Valore di test TC13_GDU1
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "password"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "password", "Size"));
    }

    /**
     * Test Case TC14_GDU1: Verifica che la registrazione fallisca se la password è troppo corta.
     * Input: Password="P" (Lunghezza 1)
     * Output atteso: Errore di validazione sul campo password.
     */
    @Test
    @Order(14)
    public void testTC14_GDU1_PasswordTroppoCorta() throws Exception {
        MockMultipartFile immagine = new MockMultipartFile("immagineProfilo", "Foto.jpg", "image/jpeg", "fake image content".getBytes());
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "P") // Valore di test TC14_GDU1
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "password"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "password", "Size"));
    }

    /**
     * Test Case TC15_GDU1: Verifica che la registrazione fallisca se la password è troppo lunga.
     * Input: Password con più di 50 caratteri
     * Output atteso: Errore di validazione sul campo password.
     */
    @Test
    @Order(15)
    public void testTC15_GDU1_PasswordTroppoLunga() throws Exception {
        MockMultipartFile immagine = new MockMultipartFile("immagineProfilo", "Foto.jpg", "image/jpeg", "fake image content".getBytes());
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        String passwordLunga = "Password".repeat(10); // 80 caratteri

        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", passwordLunga) // Valore di test TC15_GDU1
                        .param("confermaPassword", passwordLunga)
                        .sessionAttr("registerAziendaForm", aziendaForm))
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "password"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "password", "Size"));
    }

    /**
     * Test Case TC16_GDU1: Verifica che la registrazione fallisca se la conferma password è vuota.
     * Input: Conferma Password="" (Vuoto)
     * Output atteso: Errore di validazione sul campo confermaPassword.
     */
    @Test
    @Order(16)
    public void testTC16_GDU1_ConfermaPasswordVuota() throws Exception {
        MockMultipartFile immagine = new MockMultipartFile("immagineProfilo", "Foto.jpg", "image/jpeg", "fake image content".getBytes());
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123")
                        .param("confermaPassword", "") // Valore di test TC16_GDU1
                        .sessionAttr("registerAziendaForm", aziendaForm))
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "confermaPassword"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "confermaPassword", "Size"));
    }

    /**
     * Test Case TC17_GDU1: Verifica che la registrazione fallisca se la conferma password è troppo corta.
     * Input: Conferma Password="P" (Lunghezza 1)
     * Output atteso: Errore di validazione sul campo confermaPassword.
     */
    @Test
    @Order(17)
    public void testTC17_GDU1_ConfermaPasswordTroppoCorta() throws Exception {
        MockMultipartFile immagine = new MockMultipartFile("immagineProfilo", "Foto.jpg", "image/jpeg", "fake image content".getBytes());
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123")
                        .param("confermaPassword", "P") // Valore di test TC17_GDU1
                        .sessionAttr("registerAziendaForm", aziendaForm))
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "confermaPassword"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "confermaPassword", "Size"));
    }

    /**
     * Test Case TC18_GDU1: Verifica che la registrazione fallisca se la conferma password è troppo lunga.
     * Input: Conferma Password con più di 50 caratteri
     * Output atteso: Errore di validazione sul campo confermaPassword.
     */
    @Test
    @Order(18)
    public void testTC18_GDU1_ConfermaPasswordTroppoLunga() throws Exception {
        MockMultipartFile immagine = new MockMultipartFile("immagineProfilo", "Foto.jpg", "image/jpeg", "fake image content".getBytes());
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        String passwordLunga = "Password".repeat(10); // 80 caratteri

        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123")
                        .param("confermaPassword", passwordLunga) // Valore di test TC18_GDU1
                        .sessionAttr("registerAziendaForm", aziendaForm))
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "confermaPassword"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "confermaPassword", "Size"));
    }

    /**
     * Test Case TC19_GDU1: Verifica che la registrazione fallisca se le password non coincidono.
     * Input: Password="Password123", Conferma Password="Password12"
     * Output atteso: Errore di validazione (password mismatch).
     */
    @Test
    @Order(19)
    public void testTC19_GDU1_PasswordNonCoincidono() throws Exception {
        MockMultipartFile immagine = new MockMultipartFile("immagineProfilo", "Foto.jpg", "image/jpeg", "fake image content".getBytes());
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123")
                        .param("confermaPassword", "Password12") // Valore di test TC19_GDU1 (diverso)
                        .sessionAttr("registerAziendaForm", aziendaForm))
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "confermaPassword"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "confermaPassword", "error.password"));
    }

    /**
     * Test Case TC20_GDU1: Verifica che la registrazione fallisca se il file caricato non è un'immagine.
     * Input: Immagine="RAD.pdf" (File PDF)
     * Output atteso: Errore di validazione sul campo immagineProfilo.
     */
    @Test
    @Order(20)
    public void testTC20_GDU1_FileNonImmagine() throws Exception {
        // Prepariamo un file PDF simulato
        MockMultipartFile filePdf = new MockMultipartFile(
                "immagineProfilo",
                "RAD.pdf",
                "application/pdf",
                "%PDF-1.5...".getBytes()
        );

        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678901");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        mockMvc.perform(multipart("/register-utente")
                        .file(filePdf)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                .andExpect(status().isOk())
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "immagineProfilo"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "immagineProfilo", "error.immagineProfilo"));
    }

    /**
     * Test Case TC21_GDU1: Verifica che la registrazione fallisca se l'immagine supera i 12MB.
     * Input: Immagine="Foto4k.jpg" (20MB)
     * Output atteso: Errore di validazione (simulato) sul campo immagineProfilo.
     */
    @Test
    @Order(21)
    public void testTC21_GDU1_FileTroppoGrande() throws Exception {
        // Prepariamo un file grande (13MB > 12MB limit)
        // Nota: Creiamo un array di byte vuoto per evitare OutOfMemory in ambienti limitati, ma sufficiente per il controllo size.
        byte[] content = new byte[1024 * 1024 * 13]; 
        MockMultipartFile fileGrande = new MockMultipartFile(
                "immagineProfilo",
                "Foto4k.jpg",
                "image/jpeg",
                content
        );

        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink S.R.L.");
        aziendaForm.setPiva("12345678902");
        aziendaForm.setIndirizzo("Via Roma 1");
        aziendaForm.setCitta("Roma");
        aziendaForm.setCap("00100");
        aziendaForm.setTelefono("061234567");

        mockMvc.perform(multipart("/register-utente")
                        .file(fileGrande)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossi@gmail.com")
                        .param("telefonoutente", "3333769853")
                        .param("password", "Password123")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                .andExpect(status().isOk()) // Aspettiamo 200 OK perché l'ExceptionHandler restituisce la vista
                .andExpect(view().name("register/RegistraUtente"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registerUtenteForm", "immagineProfilo"))
                .andExpect(model().attributeHasFieldErrorCode("registerUtenteForm", "immagineProfilo", "size.exceeded"));
    }

    /**
     * Test Case TC22_GDU1: Verifica che la registrazione avvenga con successo con dati validi.
     * Input: Dati validi (Email e Telefono unici)
     * Output atteso: Redirect alla pagina di login con messaggio di successo.
     */
    @Test
    @Order(22)
    public void testTC22_GDU1_RegistrazioneSuccesso() throws Exception {
        // Immagine valida piccola
        MockMultipartFile immagine = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                "small image".getBytes()
        );

        // Dati azienda (P.IVA e Telefono devono essere unici se controllati, usiamo dati dummy unici per questo test)
        RegisterAziendaForm aziendaForm = new RegisterAziendaForm();
        aziendaForm.setNomeAzienda("ModuLink Success S.R.L.");
        aziendaForm.setPiva("99999999999"); // PIVA unica per questo test
        aziendaForm.setIndirizzo("Via Successo 1");
        aziendaForm.setCitta("Milano");
        aziendaForm.setCap("20100");
        aziendaForm.setTelefono("029999999"); // Telefono azienda unico

        // Eseguiamo la richiesta POST
        mockMvc.perform(multipart("/register-utente")
                        .file(immagine)
                        .param("nome", "Mario")
                        .param("cognome", "Rossi")
                        .param("email", "MarioRossiSuccess@gmail.com") // Email unica
                        .param("telefonoutente", "3339999999") // Telefono utente unico
                        .param("password", "Password123")
                        .param("confermaPassword", "Password123")
                        .sessionAttr("registerAziendaForm", aziendaForm))
                // Verifichiamo il successo (redirect)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/login?success*"));
    }
}
