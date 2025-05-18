package com.ftn.PrviMavenVebProjekat.model;

import java.time.LocalDateTime;

public class CartItem {
    private Long travelId;
    private String destinationCity;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private String vehicleName;
    private String accommodationName;
    private int numberOfNights;
    private double unitPrice;
    private int numberOfPassengers;
    private double totalPrice;

    private Long travelDateId;

    private int usedPoints;

    // Konstruktori, getteri i setteri

    public CartItem(Long travelId, String destinationCity, LocalDateTime departureDate, LocalDateTime returnDate,
                    String vehicleName, String accommodationName, int numberOfNights, double unitPrice, int numberOfPassengers) {
        this.travelId = travelId;
        this.destinationCity = destinationCity;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.vehicleName = vehicleName;
        this.accommodationName = accommodationName;
        this.numberOfNights = numberOfNights;
        this.unitPrice = unitPrice;
        this.numberOfPassengers = numberOfPassengers;
        this.totalPrice = unitPrice * numberOfNights * numberOfPassengers;
    }

    public CartItem(Long travelId, String destinationCity, LocalDateTime departureDate, LocalDateTime returnDate,
                    String vehicleName, String accommodationName, int numberOfNights, double unitPrice, int numberOfPassengers,
                    Long travelDateId) {
        this.travelId = travelId;
        this.destinationCity = destinationCity;
        this.departureDate = departureDate;
        this.returnDate = returnDate;  // Ovdje sada koristi arrivalDate kao returnDate
        this.vehicleName = vehicleName;
        this.accommodationName = accommodationName;
        this.numberOfNights = numberOfNights;
        this.unitPrice = unitPrice;
        this.numberOfPassengers = numberOfPassengers;
        this.totalPrice = unitPrice * numberOfNights * numberOfPassengers;
        this.travelDateId = travelDateId;
    }

    public CartItem(Long travelId, String destinationCity, LocalDateTime departureDate, LocalDateTime returnDate, String vehicleName, String accommodationName, int numberOfNights, double unitPrice, int numberOfPassengers, double totalPrice, Long travelDateId, int usedPoints) {
        this.travelId = travelId;
        this.destinationCity = destinationCity;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.vehicleName = vehicleName;
        this.accommodationName = accommodationName;
        this.numberOfNights = numberOfNights;
        this.unitPrice = unitPrice;
        this.numberOfPassengers = numberOfPassengers;
        this.totalPrice = totalPrice;
        this.travelDateId = travelDateId;
        this.usedPoints = usedPoints;
    }


    // Metoda za ponovno izračunavanje ukupne cene
    public void updateNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
        this.totalPrice = calculateTotalPrice();
    }

    private double calculateTotalPrice() {
        return this.unitPrice * this.numberOfPassengers * this.numberOfNights;
    }

    public Long getTravelId() {
        return travelId;
    }

    public void setTravelId(Long travelId) {
        this.travelId = travelId;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
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

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
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

    public Long getTravelDateId() {
        return travelDateId;
    }

    public void setTravelDateId(Long travelDateId) {
        this.travelDateId = travelDateId;
    }

    public CartItem(Long travelId, String destinationCity, LocalDateTime departureDate, LocalDateTime returnDate, String vehicleName, String accommodationName, int numberOfNights, double unitPrice, int numberOfPassengers, double totalPrice, Long travelDateId) {
        this.travelId = travelId;
        this.destinationCity = destinationCity;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.vehicleName = vehicleName;
        this.accommodationName = accommodationName;
        this.numberOfNights = numberOfNights;
        this.unitPrice = unitPrice;
        this.numberOfPassengers = numberOfPassengers;
        this.totalPrice = totalPrice;
        this.travelDateId = travelDateId;
    }


    public int getUsedPoints() {
        return usedPoints;
    }



    public void setUsedPoints(int usedPoints) {
        this.usedPoints = usedPoints;
    }
}
