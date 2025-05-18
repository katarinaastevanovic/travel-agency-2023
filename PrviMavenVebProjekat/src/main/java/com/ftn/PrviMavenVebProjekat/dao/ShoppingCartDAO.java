package com.ftn.PrviMavenVebProjekat.dao;

import com.ftn.PrviMavenVebProjekat.model.Reservation;
import com.ftn.PrviMavenVebProjekat.model.ReservationStatus;
import com.ftn.PrviMavenVebProjekat.model.ShoppingCart;
import com.ftn.PrviMavenVebProjekat.model.User;

import java.util.List;

public interface ShoppingCartDAO {
    void save(ShoppingCart shoppingCart);
    List<ShoppingCart> findByUserId(Long userId);

    List<ShoppingCart> findByUser(User user);

    void saveReservation(Reservation reservation);

    List<Reservation> findReservationsByStatus(String status);

    void deleteReservationById(Long reservationId);

    void updateReservationStatus(Long reservationId, ReservationStatus status);

    void updateFreeSeatsAndBeds(Long travelDateId, int numberOfPassengers);

    List<Reservation> findReservationsByUserIdAndStatus(Long userId, ReservationStatus status);

    String findDestinationCityByTravelId(Long travelId);

    Reservation findReservationById(Long reservationId);


    void updateLoyaltyCardPoints(Long userId, int points);

    void addPointsToLoyaltyCard(Long userId, int points);

    void deductPointsFromLoyaltyCard(Long userId, int points);
}
