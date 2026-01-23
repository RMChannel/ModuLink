package com.modulink.model;

import com.modulink.Controller.UserModules.GDM.NewProdottoForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NewProdottoFormCase {

    @Test
    public void TestGetterAndSetter() {
        NewProdottoForm form = new NewProdottoForm();
        form.setNome("Laptop");
        form.setQuantita(10);
        form.setPrezzo(999.99);
        form.setDescrizione("High-end gaming laptop");
        form.setCategoria("Electronics");

        assertEquals("Laptop", form.getNome());
        assertEquals(10, form.getQuantita());
        assertEquals(999.99, form.getPrezzo());
        assertEquals("High-end gaming laptop", form.getDescrizione());
        assertEquals("Electronics", form.getCategoria());
    }

    @Test
    public void TestConstructor() {
        NewProdottoForm form = new NewProdottoForm("Smartphone", 50, 499.0, "Latest model", "Mobile");

        assertEquals("Smartphone", form.getNome());
        assertEquals(50, form.getQuantita());
        assertEquals(499.0, form.getPrezzo());
        assertEquals("Latest model", form.getDescrizione());
        assertEquals("Mobile", form.getCategoria());
    }
}