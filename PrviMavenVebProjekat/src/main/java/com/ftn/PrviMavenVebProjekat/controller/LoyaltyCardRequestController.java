package com.ftn.PrviMavenVebProjekat.controller;

import com.ftn.PrviMavenVebProjekat.model.User;
import com.ftn.PrviMavenVebProjekat.service.LoyaltyCardRequestService;
import com.ftn.PrviMavenVebProjekat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/loyaltyCardRequests")
public class LoyaltyCardRequestController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoyaltyCardRequestService loyaltyCardRequestService;

    @GetMapping
    public String getLoyaltyCardRequests(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser != null && loggedInUser.isAdmin()) {
            List<User> users = userService.findUsersWithLoyaltyCardRequests();
            model.addAttribute("users", users);
            return "loyaltyCardRequests";
        }
        return "redirect:/"; // Redirect to home if not admin
    }

    @PostMapping("/approve")
    public String approveLoyaltyCard(@RequestParam Long userId, Model model) {
        System.out.println("Attempting to approve request for userId: " + userId); // Dijagnostička poruka
        try {
            loyaltyCardRequestService.approveRequest(userId);
            model.addAttribute("message", "Request approved successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Error approving request: " + e.getMessage());
        }
        return "redirect:/loyaltyCardRequests";
    }

    @PostMapping("/reject")
    public String rejectLoyaltyCard(@RequestParam Long userId, Model model) {
        System.out.println("Attempting to reject request for userId: " + userId); // Dijagnostička poruka
        try {
            loyaltyCardRequestService.rejectRequest(userId);
            model.addAttribute("message", "Request rejected successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Error rejecting request: " + e.getMessage());
        }
        return "redirect:/loyaltyCardRequests";
    }
}



