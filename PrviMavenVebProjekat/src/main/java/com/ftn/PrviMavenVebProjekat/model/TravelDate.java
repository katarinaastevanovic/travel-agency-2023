package com.ftn.PrviMavenVebProjekat.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TravelDate {
    private Long id;
    private Long travelId;
    private LocalDateTime arrivalDate;
    private LocalDateTime departureDate;
    private int numberOfNights;
    private String formattedArrivalDate;
    private String formattedDepartureDate;

    private int numberOfFreeSeats;

    private int numberOfFreeBeds;

    private int discount;

    private int discountPrice;

    private LocalDateTime discountStartDate;
    private LocalDateTime discountEndDate;

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public TravelDate(Long id, Long travelId, LocalDateTime arrivalDate, LocalDateTime departureDate, int numberOfNights, String formattedArrivalDate, String formattedDepartureDate, int numberOfFreeSeats, int numberOfFreeBeds, int discount, LocalDateTime discountStartDate, LocalDateTime discountEndDate, int discountPrice) {
        this.id = id;
        this.travelId = travelId;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.numberOfNights = numberOfNights;
        this.formattedArrivalDate = formattedArrivalDate;
        this.formattedDepartureDate = formattedDepartureDate;
        this.numberOfFreeSeats = numberOfFreeSeats;
        this.numberOfFreeBeds = numberOfFreeBeds;
        this.discount = discount;
        this.discountStartDate = discountStartDate;
        this.discountEndDate = discountEndDate;
        this.discountPrice = discountPrice;
    }



    public LocalDateTime getDiscountStartDate() {
        return discountStartDate;
    }

    public void setDiscountStartDate(LocalDateTime discountStartDate) {
        this.discountStartDate = discountStartDate;
    }

    public LocalDateTime getDiscountEndDate() {
        return discountEndDate;
    }

    public void setDiscountEndDate(LocalDateTime discountEndDate) {
        this.discountEndDate = discountEndDate;
    }

    public TravelDate(Long id, Long travelId, LocalDateTime arrivalDate, LocalDateTime departureDate, int numberOfNights, String formattedArrivalDate, String formattedDepartureDate, int numberOfFreeSeats, int numberOfFreeBeds, int discount, LocalDateTime discountStartDate, LocalDateTime discountEndDate) {
        this.id = id;
        this.travelId = travelId;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.numberOfNights = numberOfNights;
        this.formattedArrivalDate = formattedArrivalDate;
        this.formattedDepartureDate = formattedDepartureDate;
        this.numberOfFreeSeats = numberOfFreeSeats;
        this.numberOfFreeBeds = numberOfFreeBeds;
        this.discount = discount;
        this.discountStartDate = discountStartDate;
        this.discountEndDate = discountEndDate;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public TravelDate() {

    }
    public TravelDate(Long id, Long travelId, LocalDateTime arrivalDate, LocalDateTime departureDate, int numberOfNights, String formattedArrivalDate, String formattedDepartureDate, int numberOfFreeSeats, int numberOfFreeBeds, int discount) {
        this.id = id;
        this.travelId = travelId;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.numberOfNights = numberOfNights;
        this.formattedArrivalDate = formattedArrivalDate;
        this.formattedDepartureDate = formattedDepartureDate;
        this.numberOfFreeSeats = numberOfFreeSeats;
        this.numberOfFreeBeds = numberOfFreeBeds;
        this.discount = discount;
    }
// Getteri i setteri

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

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }
    public String getFormattedArrivalDate() {
        return formattedArrivalDate;
    }

    public void setFormattedArrivalDate(String formattedArrivalDate) {
        this.formattedArrivalDate = formattedArrivalDate;
    }

    public String getFormattedDepartureDate() {
        return formattedDepartureDate;
    }

    public void setFormattedDepartureDate(String formattedDepartureDate) {
        this.formattedDepartureDate = formattedDepartureDate;
    }
    public int getNumberOfFreeSeats() {
        return numberOfFreeSeats;
    }

    public void setNumberOfFreeSeats(int numberOfFreeSeats) {
        this.numberOfFreeSeats = numberOfFreeSeats;
    }

    public int getNumberOfFreeBeds() {
        return numberOfFreeBeds;
    }

    public void setNumberOfFreeBeds(int numberOfFreeBeds) {
        this.numberOfFreeBeds = numberOfFreeBeds;
    }

}
