package com.modulink.Model.Utente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UtenteEntity, String> {
    Optional<UtenteEntity> findByEmail(String email);
}

