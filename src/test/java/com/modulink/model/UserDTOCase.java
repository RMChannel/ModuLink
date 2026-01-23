package com.modulink.model;

import com.modulink.Controller.UserModules.GDU.UserRestApi.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserDTOCase {

    @Test
    public void TestRecord() {
        UserDTO dto = new UserDTO(1, "Mario", "Rossi", "mario@example.com");

        assertEquals(1, dto.id_utente());
        assertEquals("Mario", dto.nome());
        assertEquals("Rossi", dto.cognome());
        assertEquals("mario@example.com", dto.email());
    }
}
