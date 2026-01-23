package com.modulink.model;

import com.modulink.Model.SupportForm.SupportFormEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupportFormEntityCase {
    @Test
    public void testGetterSetterSupportFormEntity() {
        SupportFormEntity SF=new SupportFormEntity("Test","Test","Test");
        assertEquals("Test",SF.getEmail());
        assertEquals("Test",SF.getCategory());
        assertEquals("Test",SF.getMessage());

        SF.setEmail("Test2");
        assertEquals("Test2",SF.getEmail());
        SF.setCategory("Test2");
        assertEquals("Test2",SF.getCategory());
        SF.setMessage("Test2");
        assertEquals("Test2",SF.getMessage());
    }
}
