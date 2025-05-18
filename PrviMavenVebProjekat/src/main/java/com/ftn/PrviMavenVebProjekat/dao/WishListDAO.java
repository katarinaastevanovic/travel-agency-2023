package com.ftn.PrviMavenVebProjekat.dao;

import com.ftn.PrviMavenVebProjekat.model.WishList;

import java.util.List;

public interface WishListDAO {
     void save(WishList wishList);

    List<WishList> findByUserId(Long userId);

    void deleteById(Long id);

}
