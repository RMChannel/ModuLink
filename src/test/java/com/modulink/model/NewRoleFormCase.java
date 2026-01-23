package com.modulink.model;

import com.modulink.Controller.UserModules.GRU.NewRoleForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NewRoleFormCase {

    @Test
    public void TestGetterAndSetter() {
        NewRoleForm form = new NewRoleForm();
        form.setNome("Developer");
        form.setColore("#00FF00");
        form.setDescrizione("Software development role");
        form.setIdRuolo(10);

        assertEquals("Developer", form.getNome());
        assertEquals("#00FF00", form.getColore());
        assertEquals("Software development role", form.getDescrizione());
        assertEquals(10, form.getIdRuolo());
    }

    @Test
    public void TestConstructor() {
        NewRoleForm form = new NewRoleForm("Manager", "#FF0000", "Team management");

        assertEquals("Manager", form.getNome());
        assertEquals("#FF0000", form.getColore());
        assertEquals("Team management", form.getDescrizione());
    }
}
