package com.modulink.Model.SupportForm;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "support_form", schema = "modulink")
public class SupportFormEntity {
    @Id
    @GeneratedValue(generator = "support_form_seq")
    private int id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name="datetime", nullable = false)
    private LocalDateTime datetime;

    public SupportFormEntity() {}

    public SupportFormEntity(String email, String category, String message) {
        this.email = email;
        this.category = category;
        this.message = message;
        this.datetime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
