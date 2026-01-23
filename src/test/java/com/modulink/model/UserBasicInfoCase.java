package com.modulink.model;

import com.modulink.Controller.UserModules.GDU.UserRestApi.UserBasicInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserBasicInfoCase {

    @Test
    public void TestRecord() {
        UserBasicInfo info = new UserBasicInfo(10, "Luigi", "Verdi", "luigi@example.com");

        assertEquals(10, info.id());
        assertEquals("Luigi", info.nome());
        assertEquals("Verdi", info.cognome());
        assertEquals("luigi@example.com", info.email());
    }
}
