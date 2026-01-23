package com.modulink.model;

import com.modulink.Controller.UserModules.GDM.UpdateProdottoForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UpdateProdottoFormCase {

    @Test
    public void TestGetterAndSetter() {
        UpdateProdottoForm form = new UpdateProdottoForm();
        form.setIdProdotto(1);
        form.setNome("Updated Laptop");
        form.setQuantita(5);
        form.setPrezzo(899.99);
        form.setDescrizione("Updated description");
        form.setCategoria("Hardware");

        assertEquals(1, form.getIdProdotto());
        assertEquals("Updated Laptop", form.getNome());
        assertEquals(5, form.getQuantita());
        assertEquals(899.99, form.getPrezzo());
        assertEquals("Updated description", form.getDescrizione());
        assertEquals("Hardware", form.getCategoria());
    }

    @Test
    public void TestConstructor() {
        UpdateProdottoForm form = new UpdateProdottoForm(2, "Tablet", 20, 299.0, "New tablet", "Mobile");

        assertEquals(2, form.getIdProdotto());
        assertEquals("Tablet", form.getNome());
        assertEquals(20, form.getQuantita());
        assertEquals(299.0, form.getPrezzo());
        assertEquals("New tablet", form.getDescrizione());
        assertEquals("Mobile", form.getCategoria());
    }
}
