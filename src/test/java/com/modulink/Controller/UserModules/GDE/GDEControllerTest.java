package com.modulink.Controller.UserModules.GDE;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Eventi.EventoRepository;
import com.modulink.Model.Eventi.EventoService;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneService;
import com.modulink.Model.Relazioni.Partecipazione.PartecipazioneService;
import com.modulink.Model.Task.TaskService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UserRepository;
import com.modulink.Model.Utente.UtenteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "activate.databasepop=false")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GDEControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventoService eventoService;

    @MockitoBean
    private EventoRepository eventoRepository;

    @MockitoBean
    private PartecipazioneService partecipazioneService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ModuloService moduloService;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private AssegnazioneService assegnazioneService;

    private UtenteEntity testUtente;

    @BeforeEach
    void setUp() {
        AziendaEntity azienda = new AziendaEntity();
        azienda.setId_azienda(1);

        testUtente = new UtenteEntity();
        testUtente.setEmail("robbencito@gmail.com");
        testUtente.setAzienda(azienda);
        testUtente.setId_utente(1);
        testUtente.setNome("Roberto");
        testUtente.setCognome("Cito");

        UserDetails userDetails = User.withUsername("robbencito@gmail.com")
                .password("password")
                .roles("USER")
                .build();

        when(customUserDetailsService.loadUserByUsername("robbencito@gmail.com")).thenReturn(userDetails);
        when(customUserDetailsService.findByEmail("robbencito@gmail.com")).thenReturn(Optional.of(testUtente));
        when(moduloService.isAccessibleModulo(eq(4), any(UtenteEntity.class))).thenReturn(true);
        when(moduloService.findModuliByUtente(any(UtenteEntity.class))).thenReturn(new ArrayList<>());
    }

    /**
     * Test Case TC1_GDE1: Verifica che la creazione di un evento fallisca se il nome è vuoto.
     * Input: Nome="" (Vuoto), Luogo="Sala Riunioni", Inizio="2026-03-20T10:00:00", Fine="2026-03-20T12:00:00"
     * Output atteso: Errore "Il nome dell'evento non può essere vuoto.".
     */
    @Test
    @Order(1)
    public void TC1_GDE1_NomeVuoto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/calendar/api/create")
                        .with(user("robbencito@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\", \"luogo\":\"Sala Riunioni\", \"inizio\":\"2026-03-20T10:00:00\", \"fine\":\"2026-03-20T12:00:00\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Il nome dell'evento non può essere vuoto."));
    }

    /**
     * Test Case TC2_GDE1: Verifica che la creazione di un evento fallisca se il nome è minore di 2 caratteri.
     * Input: Nome="E" (<2), Luogo="Sala Riunioni", Inizio="2026-03-20T10:00:00", Fine="2026-03-20T12:00:00"
     * Output atteso: Errore "Il nome dev'essere compreso tra i 2 e i 200 caratteri.".
     */
    @Test
    @Order(2)
    public void TC2_GDE1_NomeCorto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/calendar/api/create")
                        .with(user("robbencito@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"E\", \"luogo\":\"Sala Riunioni\", \"inizio\":\"2026-03-20T10:00:00\", \"fine\":\"2026-03-20T12:00:00\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Il nome dev'essere compreso tra i 2 e i 200 caratteri."));
    }

    /**
     * Test Case TC3_GDE1: Verifica che la creazione di un evento fallisca se il nome supera 200 caratteri.
     * Input: Nome="A...A" (>200), Luogo="Sala Riunioni", Inizio="2026-03-20T10:00:00", Fine="2026-03-20T12:00:00"
     * Output atteso: Errore "Il nome dev'essere compreso tra i 2 e i 200 caratteri.".
     */
    @Test
    @Order(3)
    public void TC3_GDE1_NomeLungo() throws Exception {
        String nomeLungo = "A".repeat(201);
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/calendar/api/create")
                        .with(user("robbencito@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"" + nomeLungo + "\", \"luogo\":\"Sala Riunioni\", \"inizio\":\"2026-03-20T10:00:00\", \"fine\":\"2026-03-20T12:00:00\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Il nome dev'essere compreso tra i 2 e i 200 caratteri."));
    }

    /**
     * Test Case TC4_GDE1: Verifica che la creazione di un evento fallisca se il luogo supera 200 caratteri.
     * Input: Nome="Evento Mensile", Luogo="A...A" (>200), Inizio="2026-03-20T10:00:00", Fine="2026-03-20T12:00:00"
     * Output atteso: Errore "Lunghezza Luogo supera 200 caratteri".
     */
    @Test
    @Order(4)
    public void TC4_GDE1_LuogoLungo() throws Exception {
        String luogoLungo = "A".repeat(201);
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/calendar/api/create")
                        .with(user("robbencito@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Evento Mensile\", \"luogo\":\"" + luogoLungo + "\", \"inizio\":\"2026-03-20T10:00:00\", \"fine\":\"2026-03-20T12:00:00\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Lunghezza Luogo supera 200 caratteri"));
    }

    /**
     * Test Case TC5_GDE1: Verifica che la creazione di un evento fallisca se la data di inizio è vuota.
     * Input: Nome="Evento Mensile", Luogo="Sala Riunioni", Inizio=null, Fine="2025-12-20T12:00:00"
     * Output atteso: Errore "Campo data vuoto".
     */
    @Test
    @Order(5)
    public void TC5_GDE1_DataInizioVuota() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/calendar/api/create")
                        .with(user("robbencito@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Evento Mensile\", \"luogo\":\"Sala Riunioni\", \"inizio\":null, \"fine\":\"2025-12-20T12:00:00\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Campo data vuoto"));
    }

    /**
     * Test Case TC6_GDE1: Verifica che la creazione di un evento fallisca se la data di inizio è antecedente alla data limite (1970-01-01).
     * Input: Nome="Evento Mensile", Luogo="Sala Riunioni", Inizio="1969-12-31T23:59:59", Fine="2026-03-20T12:00:00"
     * Output atteso: Errore "Data inizio antecedente alla data limite".
     */
    @Test
    @Order(6)
    public void TC6_GDE1_DataInizioAntecedenteLimite() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/calendar/api/create")
                        .with(user("robbencito@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Evento Mensile\", \"luogo\":\"Sala Riunioni\", \"inizio\":\"1969-12-31T23:59:59\", \"fine\":\"2026-03-20T12:00:00\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Data inizio antecedente alla data limite"));
    }

    /**
     * Test Case TC7_GDE1: Verifica che la creazione di un evento fallisca se la data di fine è antecedente alla data di inizio.
     * Input: Nome="Evento Mensile", Luogo="Sala Riunioni", Inizio="2026-03-20T10:00:00", Fine="2026-03-19T12:00:00"
     * Output atteso: Errore "Data fine antecedente alla data inizio".
     */
    @Test
    @Order(7)
    public void TC7_GDE1_DataFineAntecedenteInizio() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/calendar/api/create")
                        .with(user("robbencito@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Evento Mensile\", \"luogo\":\"Sala Riunioni\", \"inizio\":\"2026-03-20T10:00:00\", \"fine\":\"2026-03-19T12:00:00\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Data fine antecedente alla data inizio"));
    }

    /**
     * Test Case TC8_GDE1: Verifica che la creazione di un evento abbia successo con tutti i campi validi.
     * Input: Nome="Evento Mensile", Luogo="Sala Riunioni", Inizio="2026-03-20T10:00:00", Fine="2026-03-20T12:00:00"
     * Output atteso: Successo.
     */
    @Test
    @Order(8)
    public void TC8_GDE1_EventoCompleto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/calendar/api/create")
                        .with(user("robbencito@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Evento Mensile\", \"luogo\":\"Sala Riunioni\", \"inizio\":\"2026-03-20T10:00:00\", \"fine\":\"2026-03-20T12:00:00\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    /**
     * Test Case TC9_GDE1: Verifica che la creazione di un evento abbia successo senza luogo e senza data fine.
     * Input: Nome="Evento Mensile", Luogo=null, Inizio="2026-03-20T10:00:00", Fine=null
     * Output atteso: Successo.
     */
    @Test
    @Order(9)
    public void TC9_GDE1_EventoMinimo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/calendar/api/create")
                        .with(user("robbencito@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Evento Mensile\", \"luogo\":null, \"inizio\":\"2026-03-20T10:00:00\", \"fine\":null}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    /**
     * Test Case TC10_GDE1: Verifica che la creazione di un evento abbia successo con luogo ma senza data fine.
     * Input: Nome="Evento Mensile", Luogo="Sala Riunioni", Inizio="2026-03-20T10:00:00", Fine=null
     * Output atteso: Successo.
     */
    @Test
    @Order(10)
    public void TC10_GDE1_MistoConLuogoNoFine() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/calendar/api/create")
                        .with(user("robbencito@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Evento Mensile\", \"luogo\":\"Sala Riunioni\", \"inizio\":\"2026-03-20T10:00:00\", \"fine\":null}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    /**
     * Test Case TC11_GDE1: Verifica che la creazione di un evento abbia successo senza luogo ma con data fine.
     * Input: Nome="Evento Mensile", Luogo=null, Inizio="2026-03-20T10:00:00", Fine="2026-03-20T10:00:00"
     * Output atteso: Successo.
     */
    @Test
    @Order(11)
    public void TC11_GDE1_MistoNoLuogoConFine() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/calendar/api/create")
                        .with(user("robbencito@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Evento Mensile\", \"luogo\":null, \"inizio\":\"2026-03-20T10:00:00\", \"fine\":\"2026-03-20T10:00:00\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }
}