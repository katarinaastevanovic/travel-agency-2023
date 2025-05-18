package com.ftn.PrviMavenVebProjekat.service;

import com.ftn.PrviMavenVebProjekat.model.Reservation;
import com.ftn.PrviMavenVebProjekat.model.ShoppingCart;
import com.ftn.PrviMavenVebProjekat.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ShoppingCartService {
    void save(ShoppingCart shoppingCart);
    List<ShoppingCart> findByUserId(Long userId);

    List<ShoppingCart> findByUser(User loggedInUser);

    void saveReservation(Reservation reservation);

    @Transactional
    void deductPointsFromLoyaltyCard(Long userId, int points);
}
