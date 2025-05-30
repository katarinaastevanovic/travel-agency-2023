package com.ftn.PrviMavenVebProjekat.model;

import java.time.LocalDateTime;

public class LoyaltyCardRequest {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime requestDate;

    // Constructors
    public LoyaltyCardRequest() {}

    public LoyaltyCardRequest(Long id, Long userId, String firstName, String lastName, String email, LocalDateTime requestDate) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.requestDate = requestDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}

