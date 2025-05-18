package com.ftn.PrviMavenVebProjekat.model;

public class WishList {
    private Long id;
    private Long userId;
    private Long travelId;
    private Long travelDateId;
    private int numberOfPassengers;
    private double totalPrice;

    // Prazan konstruktor
    public WishList() {
    }

    // Konstruktor sa svim argumentima
    public WishList(Long id, Long userId, Long travelId, Long travelDateId, int numberOfPassengers, double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.travelId = travelId;
        this.travelDateId = travelDateId;
        this.numberOfPassengers = numberOfPassengers;
        this.totalPrice = totalPrice;
    }

    // Getteri i setteri


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

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
