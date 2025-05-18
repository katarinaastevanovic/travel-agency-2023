package com.ftn.PrviMavenVebProjekat.service;

import com.ftn.PrviMavenVebProjekat.model.LoyaltyCard;
import com.ftn.PrviMavenVebProjekat.model.LoyaltyCardRequest;

import java.util.List;

public interface LoyaltyCardRequestService {
    void save(LoyaltyCardRequest request);
    LoyaltyCardRequest findById(Long id);
    void delete(Long id);

    void approveRequest(Long requestId);
    void rejectRequest(Long requestId);
    void addRequest(LoyaltyCardRequest request);

    LoyaltyCardRequest findByUserId(Long userId);

     List<LoyaltyCardRequest> getAllLoyaltyCardRequests();


}
