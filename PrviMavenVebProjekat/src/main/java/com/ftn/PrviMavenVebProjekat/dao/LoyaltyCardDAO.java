package com.ftn.PrviMavenVebProjekat.dao;

import com.ftn.PrviMavenVebProjekat.model.LoyaltyCard;

import java.util.List;

public interface LoyaltyCardDAO {
    void save(LoyaltyCard loyaltyCard);
    LoyaltyCard findById(Long id);
    int delete(Long id);
    List<LoyaltyCard> findAll();

    LoyaltyCard findByUserId(Long userId);
}
