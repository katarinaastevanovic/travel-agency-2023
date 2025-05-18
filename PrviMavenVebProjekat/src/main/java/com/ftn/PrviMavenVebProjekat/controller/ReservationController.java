package com.ftn.PrviMavenVebProjekat.controller;

import com.ftn.PrviMavenVebProjekat.dao.AccUnitDAO;
import com.ftn.PrviMavenVebProjekat.dao.TravelDAO;
import com.ftn.PrviMavenVebProjekat.dao.impl.ShoppingCartDAOImpl;
import com.ftn.PrviMavenVebProjekat.model.Reservation;
import com.ftn.PrviMavenVebProjekat.model.ReservationStatus;
import com.ftn.PrviMavenVebProjekat.model.Travel;
import com.ftn.PrviMavenVebProjekat.model.User;
import com.ftn.PrviMavenVebProjekat.service.ReservationService;
import com.ftn.PrviMavenVebProjekat.service.TravelService;
import com.ftn.PrviMavenVebProjekat.service.impl.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;


    @Autowired
    private ShoppingCartDAOImpl shoppingCartDAO;

    @Autowired
    private TravelService travelService;

    @Autowired
    TravelDAO travelDAO;

    @GetMapping("/requests")
    public String getReservationRequests(Model model) {
        System.out.println("Entered getReservationRequests method");

        List<Reservation> requests = reservationService.findReservationsByStatus("REQUEST");
        System.out.println("Number of requests found: " + requests.size());

        if (requests.isEmpty()) {
            System.out.println("No reservation requests found.");
        } else {
            for (Reservation request : requests) {
                System.out.println("Request ID: " + request.getId() + ", User ID: " + request.getUserId() + ", Status: " + request.getStatus());
            }
        }

        model.addAttribute("requests", requests);
        return "reservationRequests";
    }

    @PostMapping("/reject")
    public String rejectReservation(@RequestParam("reservationId") Long reservationId) {
        // Prvo, pronađite rezervaciju koju treba odbiti
        Reservation reservation = reservationService.findReservationById(reservationId);

        // Dobijte broj poena iz rezervacije
        int points = reservation.getPoints();
        Long userId = reservation.getUserId();

        // Ažurirajte broj poena u tabeli loyalty_card
        reservationService.returnPointsToLoyaltyCard(userId, points);

        // Obrišite rezervaciju
        reservationService.deleteReservationById(reservationId);

        return "redirect:/reservations/requests";
    }
    @PostMapping("/approve")
    public String approveReservation(@RequestParam("reservationId") Long reservationId,
                                     @RequestParam("travelDateId") Long travelDateId,
                                     @RequestParam("numberOfPassengers") int numberOfPassengers) {
        // Pronađite rezervaciju
        Reservation reservation = reservationService.findReservationById(reservationId);
        System.out.println("Reservation final price: " + reservation.getFinalPrice());

        // Ažurirajte status rezervacije na APPROVED
        reservationService.updateReservationStatus(reservationId, ReservationStatus.APPROVED);

        // Smanjite broj slobodnih mesta i kreveta u TravelDate
        reservationService.updateFreeSeatsAndBeds(travelDateId, numberOfPassengers);

        // Dodajte bodove na loyalty karticu na osnovu cene rezervacije
        reservationService.addPointsToLoyaltyCard(reservation.getUserId(), reservation.getFinalPrice());

        return "redirect:/reservations/requests";
    }

    @GetMapping("/userPurchases")
    public String getUserPurchases(Model model, @SessionAttribute("LOGGED_IN_USER") User loggedInUser) {
        List<Reservation> purchases = reservationService.findReservationsByUserIdAndStatus(loggedInUser.getId(), ReservationStatus.APPROVED);

        // Sortiranje po datumu kreiranja opadajuće
        purchases.sort((r1, r2) -> r2.getCreationDate().compareTo(r1.getCreationDate()));

        // Kreiranje mape koja povezuje rezervacije sa gradovima destinacija
        Map<Long, String> purchaseCities = new HashMap<>();
        Map<Long, Double> totalPrices = new HashMap<>(); // Dodato

        for (Reservation reservation : purchases) {
            String city = shoppingCartDAO.findDestinationCityByTravelId(reservation.getTravelId());
            purchaseCities.put(reservation.getId(), city);

            // Izračunaj ukupnu cenu za svaku rezervaciju koristeći finalPrice
            double totalPrice = reservation.getFinalPrice() * reservation.getNumberOfPassengers();
            totalPrices.put(reservation.getId(), totalPrice);
        }

        model.addAttribute("purchases", purchases);
        model.addAttribute("purchaseCities", purchaseCities);
        model.addAttribute("totalPrices", totalPrices); // Dodato
        return "userPurchases";
    }


}
