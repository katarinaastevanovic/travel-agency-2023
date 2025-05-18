package com.ftn.PrviMavenVebProjekat.service.impl;

import com.ftn.PrviMavenVebProjekat.dao.LoyaltyCardDAO;
import com.ftn.PrviMavenVebProjekat.dao.LoyaltyCardRequestDAO;
import com.ftn.PrviMavenVebProjekat.model.LoyaltyCard;
import com.ftn.PrviMavenVebProjekat.model.LoyaltyCardRequest;
import com.ftn.PrviMavenVebProjekat.service.LoyaltyCardRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoyaltyCardRequestServiceImpl implements LoyaltyCardRequestService {

    @Autowired
    private LoyaltyCardRequestDAO loyaltyCardRequestDAO;

    @Autowired
    private LoyaltyCardDAO loyaltyCardDAO;

    @Override
    public void save(LoyaltyCardRequest request) {
        loyaltyCardRequestDAO.save(request);
    }

    @Override
    public LoyaltyCardRequest findById(Long id) {
        return loyaltyCardRequestDAO.findById(id);
    }

    @Override
    public void delete(Long id) {
        loyaltyCardRequestDAO.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoyaltyCardRequest> getAllLoyaltyCardRequests() {
        return loyaltyCardRequestDAO.findAll();
    }

    @Override
    @Transactional
    public void approveRequest(Long userId) {
        LoyaltyCardRequest request = loyaltyCardRequestDAO.findByUserId(userId);
        if (request != null) {
            // Debug log
            System.out.println("Attempting to approve request for userId: " + userId);

            LoyaltyCard loyaltyCard = new LoyaltyCard();
            if (userId != null) {
                loyaltyCard.setUserId(userId);
                loyaltyCard.setDiscount(0); // Ako imate podrazumevanu vrednost za popust, postavite je ovde
                loyaltyCard.setNumberOfPoints(5); // Postavite broj bodova na 5
                System.out.println("Setting userId and numberOfPoints for loyalty card: " + loyaltyCard.getUserId() + ", " + loyaltyCard.getNumberOfPoints());

                try {
                    loyaltyCardDAO.save(loyaltyCard);
                    loyaltyCardRequestDAO.delete(request.getId());
                    System.out.println("Successfully approved request for userId: " + userId);
                } catch (Exception e) {
                    System.out.println("Error approving request: " + e.getMessage());
                }
            } else {
                System.out.println("UserId is null for request");
            }
        } else {
            System.out.println("No request found for userId: " + userId);
        }
    }



    @Override
    @Transactional
    public void rejectRequest(Long userId) {
        try {
            LoyaltyCardRequest request = loyaltyCardRequestDAO.findByUserId(userId);
            if (request != null) {
                loyaltyCardRequestDAO.delete(request.getId());
                System.out.println("Request rejected for userId: " + userId); // Dijagnostička poruka
            } else {
                System.out.println("No request found with userId: " + userId); // Dijagnostička poruka
            }
        } catch (Exception e) {
            System.out.println("Error rejecting request: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @Override
    @Transactional
    public void addRequest(LoyaltyCardRequest request) {
        // Save the request to the database
        loyaltyCardRequestDAO.save(request);
    }

    @Override
    public LoyaltyCardRequest findByUserId(Long userId) {
        return loyaltyCardRequestDAO.findByUserId(userId);
    }

}
