package com.ftn.PrviMavenVebProjekat.service;

import com.ftn.PrviMavenVebProjekat.model.WishList;

import java.util.List;

public interface WishListService {
    void save(WishList wishList);

    List<WishList> findByUserId(Long userId);

    void deleteById(Long wishListId);
}
