package com.modulink.model;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Prodotto.ProdottoEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProdottoEntityCase {
    @Test
    public void testGetterSetterProdottoEntity(){
        AziendaEntity A = new AziendaEntity("Modulink", "12345678912", "Via Alfredo Capone 11", "Salerno", "84128", "3285630871", "");
        ProdottoEntity PE = new ProdottoEntity(A, "Camel Blu", 15, 7.90, "Tabacco", "Consumabile");
        assertThat(PE.getAzienda()).isEqualTo(A);
        assertEquals("Camel Blu", PE.getNome());
        assertEquals(15, PE.getQuantita());
        assertEquals(7.90, PE.getPrezzo());
        assertEquals("Tabacco", PE.getDescrizione());
        assertEquals("Consumabile", PE.getCategoria());

        AziendaEntity B = new AziendaEntity("Iphone", "12345678913", "Via Alfredo Capone 12", "Roma", "00120", "3471304385", "/Azienda-logos/foto.jpeg");
        PE.setAzienda(B);
        assertThat(PE.getAzienda()).isEqualTo(B);

        PE.setNome("Winston blu");
        assertEquals("Winston blu", PE.getNome());

        PE.setQuantita(20);
        assertEquals(20, PE.getQuantita());

        PE.setPrezzo(5.50);
        assertEquals(5.50, PE.getPrezzo());

        PE.setDescrizione("Sigarette");
        assertEquals("Sigarette", PE.getDescrizione());

        PE.setCategoria("Fumabile");
        assertEquals("Fumabile", PE.getCategoria());

    }
}
