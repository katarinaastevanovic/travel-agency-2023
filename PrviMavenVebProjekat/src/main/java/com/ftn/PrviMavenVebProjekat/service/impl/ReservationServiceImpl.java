package com.ftn.PrviMavenVebProjekat.service.impl;

import com.ftn.PrviMavenVebProjekat.dao.ShoppingCartDAO;
import com.ftn.PrviMavenVebProjekat.dao.TravelDAO;
import com.ftn.PrviMavenVebProjekat.model.Reservation;
import com.ftn.PrviMavenVebProjekat.model.ReservationStatus;
import com.ftn.PrviMavenVebProjekat.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ShoppingCartDAO shoppingCartDAO;

    @Autowired
    private TravelDAO travelDAO;

    @Override
    public List<Reservation> findReservationsByStatus(String status) {
        return shoppingCartDAO.findReservationsByStatus(status);
    }
    @Override
    public void deleteReservationById(Long reservationId) {
        shoppingCartDAO.deleteReservationById(reservationId);
    }

    @Override
    public void updateReservationStatus(Long reservationId, ReservationStatus status) {
        shoppingCartDAO.updateReservationStatus(reservationId, status);
    }

    @Override
    public List<Reservation> findReservationsByUserIdAndStatus(Long userId, ReservationStatus status) {
        return shoppingCartDAO.findReservationsByUserIdAndStatus(userId, status);
    }

    @Override
    public int findRemainingSeatsByTravelDateId(Long travelDateId) {
        return travelDAO.findRemainingSeatsByTravelDateId(travelDateId);
    }

    @Override
    public Reservation findReservationById(Long reservationId) {
        return shoppingCartDAO.findReservationById(reservationId);
    }

    @Override
    public void returnPointsToLoyaltyCard(Long userId, int points) {
        shoppingCartDAO.updateLoyaltyCardPoints(userId, points);
    }

    @Override
    public void updateFreeSeatsAndBeds(Long travelDateId, int numberOfPassengers) {
        shoppingCartDAO.updateFreeSeatsAndBeds(travelDateId, numberOfPassengers);
    }
    @Override
    public void addPointsToLoyaltyCard(Long userId, double finalPrice) {
        int points = (int) (finalPrice / 100); // 1 bod na svakih 100 potrošenih
        System.out.println("Dodajem " + points + " bodova korisniku sa ID-jem: " + userId);
        shoppingCartDAO.addPointsToLoyaltyCard(userId, points);
    }


}
