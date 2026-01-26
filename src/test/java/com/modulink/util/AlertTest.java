package com.modulink.util;

import com.modulink.Alert;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AlertTest {

    @Test
    void testError() {
        String message = "Credenziali non valide";
        String result = Alert.error(message);
        assertThat(result).isEqualTo("?error=true&message=Credenziali+non+valide");
    }

    @Test
    void testSuccess() {
        String message = "Operazione completata";
        String result = Alert.success(message);
        assertThat(result).isEqualTo("?success=true&message=Operazione+completata");
    }

    @Test
    void testSpecialCharacters() {
        String message = "a@b.c & more";
        // URLEncoder encodes space as +, @ is %40, & is %26
        String expectedError = "?error=true&message=a%40b.c+%26+more";
        String expectedSuccess = "?success=true&message=a%40b.c+%26+more";

        assertThat(Alert.error(message)).isEqualTo(expectedError);
        assertThat(Alert.success(message)).isEqualTo(expectedSuccess);
    }
    
    @Test
    void testConstructor() {
        // Just to cover the default constructor if strict coverage is needed, 
        // though it's a utility class.
        Alert alert = new Alert();
        assertThat(alert).isNotNull();
    }
}
