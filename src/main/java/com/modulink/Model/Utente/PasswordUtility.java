package com.modulink.Model.Utente;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtility {

    // Hash della password in fase di registrazione e login
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Verifica della password in fase di login
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

