package com.ftn.PrviMavenVebProjekat.controller;

import com.ftn.PrviMavenVebProjekat.model.User;
import com.ftn.PrviMavenVebProjekat.model.WishList;
import com.ftn.PrviMavenVebProjekat.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/wishList")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @GetMapping("/wishLists")
    public String getWishLists(@SessionAttribute("LOGGED_IN_USER") User loggedInUser, Model model) {
        List<WishList> wishLists = wishListService.findByUserId(loggedInUser.getId());
        model.addAttribute("wishLists", wishLists);
        return "wishLists";
    }

    @PostMapping("/add")
    public String addToWishList(@RequestParam Long travelId,
                                @RequestParam Long travelDateId,
                                @RequestParam int numberOfPassengers,
                                @RequestParam double totalPrice,
                                HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return "redirect:/users/loginForm";
        }
        WishList wishList = new WishList(null, loggedInUser.getId(), travelId, travelDateId, numberOfPassengers, totalPrice);
        wishListService.save(wishList);
        return "redirect:/wishList/wishLists";
    }
    @PostMapping("/delete")
    public String deleteFromWishList(@RequestParam Long wishListId) {
        wishListService.deleteById(wishListId);
        return "redirect:/wishList/wishLists";
    }

}
