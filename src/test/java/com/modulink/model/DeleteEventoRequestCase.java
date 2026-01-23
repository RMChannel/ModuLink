package com.modulink.model;

import com.modulink.Controller.UserModules.GDE.EventoController.DeleteEventoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DeleteEventoRequestCase {

    @Test
    public void TestRecord() {
        DeleteEventoRequest request = new DeleteEventoRequest(42);

        assertEquals(42, request.id());
    }
}
