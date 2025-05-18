package com.ftn.PrviMavenVebProjekat.service;

import com.ftn.PrviMavenVebProjekat.model.Reservation;
import com.ftn.PrviMavenVebProjekat.model.ReservationStatus;

import java.util.List;

public interface ReservationService {
    List<Reservation> findReservationsByStatus(String status);

    void deleteReservationById(Long reservationId);

    void updateReservationStatus(Long reservationId, ReservationStatus status);

    List<Reservation> findReservationsByUserIdAndStatus(Long id, ReservationStatus reservationStatus);

    int findRemainingSeatsByTravelDateId(Long travelDateId);

    Reservation findReservationById(Long reservationId);

    void returnPointsToLoyaltyCard(Long userId, int points);

    void updateFreeSeatsAndBeds(Long travelDateId, int numberOfPassengers);

    void addPointsToLoyaltyCard(Long userId, double finalPrice);

}
