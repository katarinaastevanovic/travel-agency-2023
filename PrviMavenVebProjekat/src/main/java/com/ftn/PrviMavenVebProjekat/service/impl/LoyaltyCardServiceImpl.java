package com.ftn.PrviMavenVebProjekat.service.impl;

import com.ftn.PrviMavenVebProjekat.dao.LoyaltyCardDAO;
import com.ftn.PrviMavenVebProjekat.dao.LoyaltyCardRequestDAO;
import com.ftn.PrviMavenVebProjekat.model.LoyaltyCard;
import com.ftn.PrviMavenVebProjekat.model.LoyaltyCardRequest;
import com.ftn.PrviMavenVebProjekat.service.LoyaltyCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoyaltyCardServiceImpl implements LoyaltyCardService {

    @Autowired
    private LoyaltyCardDAO loyaltyCardDAO;

    @Autowired
    private LoyaltyCardRequestDAO loyaltyCardRequestDAO;

    @Override
    public LoyaltyCard findByUserId(Long userId) {
        return loyaltyCardDAO.findByUserId(userId);
    }

    @Override
    public void save(LoyaltyCard loyaltyCard) {
        loyaltyCardDAO.save(loyaltyCard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoyaltyCardRequest> getAllLoyaltyCardRequests() {
        return loyaltyCardRequestDAO.findAll();
    }





}
