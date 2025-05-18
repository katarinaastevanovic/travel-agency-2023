package com.ftn.PrviMavenVebProjekat.service;

import com.ftn.PrviMavenVebProjekat.model.LoyaltyCard;
import com.ftn.PrviMavenVebProjekat.model.LoyaltyCardRequest;

import java.util.List;

public interface LoyaltyCardService {
    LoyaltyCard findByUserId(Long userId);
    void save(LoyaltyCard loyaltyCard);

    List<LoyaltyCardRequest> getAllLoyaltyCardRequests();

}
