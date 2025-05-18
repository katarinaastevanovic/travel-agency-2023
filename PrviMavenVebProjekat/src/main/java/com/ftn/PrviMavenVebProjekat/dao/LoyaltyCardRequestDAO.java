package com.ftn.PrviMavenVebProjekat.dao;

import com.ftn.PrviMavenVebProjekat.model.LoyaltyCard;
import com.ftn.PrviMavenVebProjekat.model.LoyaltyCardRequest;
import java.util.List;

public interface LoyaltyCardRequestDAO {

    int save(LoyaltyCardRequest request);

    int update(LoyaltyCardRequest request);

    int delete(Long id);

    LoyaltyCardRequest findById(Long id);

    List<LoyaltyCardRequest> findAll();

    List<Long> findUserIdsWithRequests();

    LoyaltyCardRequest findByUserId(Long userId);
}
