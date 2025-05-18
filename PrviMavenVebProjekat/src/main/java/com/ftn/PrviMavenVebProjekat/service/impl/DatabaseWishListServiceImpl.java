package com.ftn.PrviMavenVebProjekat.service.impl;

import com.ftn.PrviMavenVebProjekat.dao.WishListDAO;
import com.ftn.PrviMavenVebProjekat.model.WishList;
import com.ftn.PrviMavenVebProjekat.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DatabaseWishListServiceImpl implements WishListService {

    @Autowired
    private WishListDAO wishListDAO;

    @Override
    @Transactional
    public void save(WishList wishList) {
        wishListDAO.save(wishList);
    }

    @Override
    @Transactional
    public List<WishList> findByUserId(Long userId) {
        return wishListDAO.findByUserId(userId);
    }

    @Override
    public void deleteById(Long wishListId) {
        wishListDAO.deleteById(wishListId);
    }
}
