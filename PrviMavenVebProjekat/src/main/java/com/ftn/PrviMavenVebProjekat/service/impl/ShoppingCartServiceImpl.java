package com.ftn.PrviMavenVebProjekat.service.impl;

import com.ftn.PrviMavenVebProjekat.dao.ShoppingCartDAO;
import com.ftn.PrviMavenVebProjekat.model.Reservation;
import com.ftn.PrviMavenVebProjekat.model.ShoppingCart;
import com.ftn.PrviMavenVebProjekat.model.User;

import com.ftn.PrviMavenVebProjekat.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartDAO shoppingCartDAO;


    @Override
    @Transactional
    public void save(ShoppingCart shoppingCart) {
        shoppingCartDAO.save(shoppingCart);
    }

    @Override
    public List<ShoppingCart> findByUserId(Long userId) {
        return shoppingCartDAO.findByUserId(userId);
    }

    @Override
    public List<ShoppingCart> findByUser(User user) {
        return shoppingCartDAO.findByUser(user); // Koristimo metodu iz repozitorijuma
    }

    @Override
    @Transactional
    public void saveReservation(Reservation reservation) {

        shoppingCartDAO.saveReservation(reservation);
    }

    @Override
    @Transactional
    public void deductPointsFromLoyaltyCard(Long userId, int points) {
        shoppingCartDAO.deductPointsFromLoyaltyCard(userId, points);
    }

}
