package com.modulink.Controller.UserModules.GDM;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Prodotto.ProdottoService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
public class GDMControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdottoService prodottoService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private ModuloService moduloService;

    private UtenteEntity testUtente;

    @BeforeEach
    void setUp() {
        AziendaEntity azienda = new AziendaEntity();
        azienda.setId_azienda(1);

        testUtente = new UtenteEntity();
        testUtente.setEmail("test@modulink.com");
        testUtente.setAzienda(azienda);
        testUtente.setId_utente(1);
        testUtente.setNome("Test");
        testUtente.setCognome("User");

        UserDetails userDetails = User.withUsername("test@modulink.com")
                .password("password")
                .roles("USER")
                .build();

        when(customUserDetailsService.loadUserByUsername("test@modulink.com")).thenReturn(userDetails);
        when(customUserDetailsService.findByEmail("test@modulink.com")).thenReturn(Optional.of(testUtente));
        when(moduloService.isAccessibleModulo(eq(6), any(UtenteEntity.class))).thenReturn(true);
    }

    /**
     * Test Case TC1_GDM1: Verifica che la creazione di un prodotto fallisca se il nome è vuoto.
     * Input: Nome="" (Vuoto), Quantità=10, Prezzo=100.00, Descrizione=Standard, Categoria=Software
     * Output atteso: Errore di validazione sul campo nome.
     */
    @Test
    @Order(1)
    public void TC1_GDM1_NomeVuoto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gdm/add-product")
                        .with(user("test@modulink.com"))
                        .param("nome", "")
                        .param("quantita", "10")
                        .param("prezzo", "100.00")
                        .param("descrizione", "Standard")
                        .param("categoria", "Software"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gdm/GestioneProdotti"))
                .andExpect(model().attributeHasFieldErrors("newProdottoForm", "nome"));
    }

    /**
     * Test Case TC2_GDM1: Verifica che la creazione di un prodotto fallisca se il nome è minore di 2 caratteri.
     * Input: Nome="P" (<2), Quantità=10, Prezzo=100.00, Descrizione=Standard, Categoria=Software
     * Output atteso: Errore di validazione sul campo nome.
     */
    @Test
    @Order(2)
    public void TC2_GDM1_NomeCorto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gdm/add-product")
                        .with(user("test@modulink.com"))
                        .param("nome", "P")
                        .param("quantita", "10")
                        .param("prezzo", "100.00")
                        .param("descrizione", "Standard")
                        .param("categoria", "Software"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gdm/GestioneProdotti"))
                .andExpect(model().attributeHasFieldErrors("newProdottoForm", "nome"));
    }

    /**
     * Test Case TC3_GDM1: Verifica che la creazione di un prodotto fallisca se il nome supera 50 caratteri.
     * Input: Nome="Prodotto......1" (>50), Quantità=10, Prezzo=100.00, Descrizione=Standard, Categoria=Software
     * Output atteso: Errore di validazione sul campo nome.
     */
    @Test
    @Order(3)
    public void TC3_GDM1_NomeLungo() throws Exception {
        String nomeLungo = "A".repeat(51);
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gdm/add-product")
                        .with(user("test@modulink.com"))
                        .param("nome", nomeLungo)
                        .param("quantita", "10")
                        .param("prezzo", "100.00")
                        .param("descrizione", "Standard")
                        .param("categoria", "Software"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gdm/GestioneProdotti"))
                .andExpect(model().attributeHasFieldErrors("newProdottoForm", "nome"));
    }

    /**
     * Test Case TC4_GDM1: Verifica che la creazione di un prodotto fallisca se la quantità è negativa.
     * Input: Nome="Prodotto 1", Quantità=-10, Prezzo=100.00, Descrizione=Standard, Categoria=Software
     * Output atteso: Errore di validazione sul campo quantità.
     */
    @Test
    @Order(4)
    public void TC4_GDM1_QuantitaNegativa() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gdm/add-product")
                        .with(user("test@modulink.com"))
                        .param("nome", "Prodotto 1")
                        .param("quantita", "-10")
                        .param("prezzo", "100.00")
                        .param("descrizione", "Standard")
                        .param("categoria", "Software"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gdm/GestioneProdotti"))
                .andExpect(model().attributeHasFieldErrors("newProdottoForm", "quantita"));
    }

    /**
     * Test Case TC5_GDM1: Verifica che la creazione di un prodotto fallisca se la quantità è vuota.
     * Input: Nome="Prodotto 1", Quantità="" (Vuoto), Prezzo=100.00
     * Output atteso: Errore di validazione sul campo quantità (TypeMismatch).
     */
    @Test
    @Order(5)
    public void TC5_GDM1_QuantitaVuota() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gdm/add-product")
                        .with(user("test@modulink.com"))
                        .param("nome", "Prodotto 1")
                        .param("quantita", "")
                        .param("prezzo", "100.00")
                        .param("descrizione", "Standard")
                        .param("categoria", "Software"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gdm/GestioneProdotti"))
                .andExpect(model().attributeHasFieldErrors("newProdottoForm", "quantita"));
    }

    /**
     * Test Case TC6_GDM1: Verifica che la creazione di un prodotto fallisca se la quantità ha un formato non valido.
     * Input: Nome="Prodotto 1", Quantità="uno", Prezzo=100.00
     * Output atteso: Errore di validazione sul campo quantità (TypeMismatch).
     */
    @Test
    @Order(6)
    public void TC6_GDM1_QuantitaFormatoNonValido() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gdm/add-product")
                        .with(user("test@modulink.com"))
                        .param("nome", "Prodotto 1")
                        .param("quantita", "uno")
                        .param("prezzo", "100.00")
                        .param("descrizione", "Standard")
                        .param("categoria", "Software"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gdm/GestioneProdotti"))
                .andExpect(model().attributeHasFieldErrors("newProdottoForm", "quantita"));
    }

    /**
     * Test Case TC7_GDM1: Verifica che la creazione di un prodotto fallisca se il prezzo è negativo.
     * Input: Nome="Prodotto 1", Quantità=10, Prezzo=-100.00
     * Output atteso: Errore di validazione sul campo prezzo.
     */
    @Test
    @Order(7)
    public void TC7_GDM1_PrezzoNegativo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gdm/add-product")
                        .with(user("test@modulink.com"))
                        .param("nome", "Prodotto 1")
                        .param("quantita", "10")
                        .param("prezzo", "-100.00")
                        .param("descrizione", "Standard")
                        .param("categoria", "Software"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gdm/GestioneProdotti"))
                .andExpect(model().attributeHasFieldErrors("newProdottoForm", "prezzo"));
    }

    /**
     * Test Case TC8_GDM1: Verifica che la creazione di un prodotto fallisca se il prezzo ha un formato non valido.
     * Input: Nome="Prodotto 1", Quantità=10, Prezzo="cento"
     * Output atteso: Errore di validazione sul campo prezzo (TypeMismatch).
     */
    @Test
    @Order(8)
    public void TC8_GDM1_PrezzoFormatoNonValido() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gdm/add-product")
                        .with(user("test@modulink.com"))
                        .param("nome", "Prodotto 1")
                        .param("quantita", "10")
                        .param("prezzo", "cento")
                        .param("descrizione", "Standard")
                        .param("categoria", "Software"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gdm/GestioneProdotti"))
                .andExpect(model().attributeHasFieldErrors("newProdottoForm", "prezzo"));
    }

    /**
     * Test Case TC9_GDM1: Verifica che la creazione di un prodotto fallisca se la descrizione supera 255 caratteri.
     * Input: Nome="Prodotto 1", Quantità=10, Prezzo=100.00, Descrizione="Standard..." (>255), Categoria="Software"
     * Output atteso: Errore di validazione sul campo descrizione.
     */
    @Test
    @Order(9)
    public void TC9_GDM1_DescrizioneLunga() throws Exception {
        String descrizioneLunga = "A".repeat(256);
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gdm/add-product")
                        .with(user("test@modulink.com"))
                        .param("nome", "Prodotto 1")
                        .param("quantita", "10")
                        .param("prezzo", "100.00")
                        .param("descrizione", descrizioneLunga)
                        .param("categoria", "Software"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gdm/GestioneProdotti"))
                .andExpect(model().attributeHasFieldErrors("newProdottoForm", "descrizione"));
    }

    /**
     * Test Case TC10_GDM1: Verifica che la creazione di un prodotto fallisca se la categoria supera 255 caratteri.
     * Input: Nome="Prodotto 1", Quantità=10, Prezzo=100.00, Descrizione="Standard", Categoria="Software..." (>255)
     * Output atteso: Errore di validazione sul campo categoria.
     */
    @Test
    @Order(10)
    public void TC10_GDM1_CategoriaLunga() throws Exception {
        String categoriaLunga = "A".repeat(256);
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gdm/add-product")
                        .with(user("test@modulink.com"))
                        .param("nome", "Prodotto 1")
                        .param("quantita", "10")
                        .param("prezzo", "100.00")
                        .param("descrizione", "Standard")
                        .param("categoria", categoriaLunga)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gdm/GestioneProdotti"))
                .andExpect(model().attributeHasFieldErrors("newProdottoForm", "categoria"));
    }

    /**
     * Test Case TC11_GDM1: Verifica che la creazione di un prodotto vada a buon fine con dati validi.
     * Input: Nome="Prodotto 1", Quantità=10, Prezzo=100.00, Descrizione="Standard", Categoria="Software...integrato"
     * Output atteso: Prodotto aggiunto con successo (Redirect).
     */
    @Test
    @Order(11)
    public void TC11_GDM1_ProdottoValido() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gdm/add-product")
                        .with(user("test@modulink.com"))
                        .param("nome", "Prodotto 1")
                        .param("quantita", "10")
                        .param("prezzo", "100.00")
                        .param("descrizione", "Standard")
                        .param("categoria", "Software integrato"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/gdm?success=true&message=Prodotto+aggiunto+con+successo"));
    }
}