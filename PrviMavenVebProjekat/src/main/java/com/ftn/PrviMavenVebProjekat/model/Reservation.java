package com.ftn.PrviMavenVebProjekat.model;

import java.time.LocalDateTime;

public class Reservation {

    private Long id;
    private Long travelId;

    private Long travelDateId;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private double finalPrice;
    private LocalDateTime creationDate;
    private ReservationStatus status;

    private int numberOfPassengers;

    private Long userId;


    private int points;



    public Reservation(Long id, Long travelId, Long travelDateId, LocalDateTime departureDate, LocalDateTime returnDate, double finalPrice, LocalDateTime creationDate, ReservationStatus status, int numberOfPassengers, Long userId, int points) {
        this.id = id;
        this.travelId = travelId;
        this.travelDateId = travelDateId;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.finalPrice = finalPrice;
        this.creationDate = creationDate;
        this.status = status;
        this.numberOfPassengers = numberOfPassengers;
        this.userId = userId;
        this.points = points;
    }






    public Reservation(Long id, Long travelId, LocalDateTime departureDate, LocalDateTime returnDate,
                       double finalPrice, LocalDateTime creationDate, ReservationStatus status,
                       int numberOfPassengers) {
        this.id = id;
        this.travelId = travelId;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.finalPrice = finalPrice;
        this.creationDate = creationDate;
        this.status = status;
        this.numberOfPassengers = numberOfPassengers;
    }




    public Reservation(Long id, Long travelId, LocalDateTime departureDate,
                       LocalDateTime returnDate, double finalPrice, LocalDateTime creationDate,
                       ReservationStatus status, int numberOfPassengers, Long userId) {
        this.id = id;
        this.travelId = travelId;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.finalPrice = finalPrice;
        this.creationDate = creationDate;
        this.status = status;
        this.numberOfPassengers = numberOfPassengers;
        this.userId = userId;
    }

    public Reservation(Long id, Long travelId, Long travelDateId, LocalDateTime departureDate,
                       LocalDateTime returnDate, double finalPrice, LocalDateTime creationDate,
                       ReservationStatus status, int numberOfPassengers, Long userId) {
        this.id = id;
        this.travelId = travelId;
        this.travelDateId = travelDateId;  // Novo polje
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.finalPrice = finalPrice;
        this.creationDate = creationDate;
        this.status = status;
        this.numberOfPassengers = numberOfPassengers;
        this.userId = userId;
    }

    public Reservation() {

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTravelId() {
        return travelId;
    }

    public void setTravelId(Long travelId) {
        this.travelId = travelId;
    }

    public Long getTravelDateId() {
        return travelDateId;
    }

    public void setTravelDateId(Long travelDateId) {
        this.travelDateId = travelDateId;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }




}



