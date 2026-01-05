package com.modulink;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Alert {

    /**
     * Generates the URL parameters for an error message.
     * @param message The message to display.
     * @return A string in the format "error=true&message=encoded_message"
     */
    public static String error(String message) {
        return "?error=true&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
    }

    /**
     * Generates the URL parameters for a success message.
     * @param message The message to display.
     * @return A string in the format "success=true&message=encoded_message"
     */
    public static String success(String message) {
        return "?success=true&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
    }
}