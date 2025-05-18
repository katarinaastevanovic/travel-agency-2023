package com.ftn.PrviMavenVebProjekat.controller;

import com.ftn.PrviMavenVebProjekat.model.*;
import com.ftn.PrviMavenVebProjekat.service.ShoppingCartService;
import com.ftn.PrviMavenVebProjekat.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private TravelService travelService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public String addToCart(@RequestParam Long travelId, @RequestParam Long travelDateId,
                            @RequestParam int numberOfPassengers, HttpSession session, Model model) {

        Travel travel = travelService.findOne(travelId);

        if (travel == null) {
            model.addAttribute("errorMessage", "Putovanje nije pronađeno.");
            return "redirect:/error"; // ili odgovarajuća stranica za grešku
        }

        TravelDate selectedDate = travel.getTravelDates().stream()
                .filter(td -> td.getId().equals(travelDateId))
                .findFirst()
                .orElse(null);

        if (selectedDate == null) {
            model.addAttribute("errorMessage", "Izabrani datum putovanja nije pronađen.");
            return "redirect:/error"; // ili odgovarajuća stranica za grešku
        }

        // Provera dostupnosti mesta i kreveta
        int availableSeats = selectedDate.getNumberOfFreeSeats();
        int availableBeds = selectedDate.getNumberOfFreeBeds();

        if (numberOfPassengers > availableSeats || numberOfPassengers > availableBeds) {
            model.addAttribute("errorMessage", "Broj putnika premašuje dostupna mesta ili krevete.");
            model.addAttribute("putovanje", travel); // Prosleđivanje potrebnih podataka za ponovni prikaz stranice
            return "travel/details";
        }

        if (numberOfPassengers < 1 || numberOfPassengers > 100) {
            model.addAttribute("errorMessage", "Uneli ste nevalidan broj putnika.");
            model.addAttribute("putovanje", travel); // Prosleđivanje potrebnih podataka za ponovni prikaz stranice
            return "travel/details";
        }

        CartItem cartItem = new CartItem(travelId, travel.getDestinacija().getGrad(),
                selectedDate.getArrivalDate(),
                selectedDate.getDepartureDate(),
                travel.getVehicle().getName(),
                travel.getAccUnit().getTypeOfAccommodationUnit().name(),
                selectedDate.getNumberOfNights(),
                travel.getPrice(), numberOfPassengers, travelDateId);

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        cart.add(cartItem);
        session.setAttribute("cart", cart);

        return "redirect:/cart/view";
    }

    @GetMapping("/view")
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        double totalPrice = 0;

        if (cart != null) {
            for (CartItem item : cart) {
                totalPrice += item.getTotalPrice();
            }
        } else {
            cart = new ArrayList<>();
        }

        User user = (User) session.getAttribute("LOGGED_IN_USER");
        int loyaltyPoints = 0;

        if (user != null) {
            loyaltyPoints = travelService.getLoyaltyPoints(user.getId());
        }

        model.addAttribute("cartItems", cart);
        model.addAttribute("totalPrice", totalPrice);  // Prosleđivanje ukupnog iznosa u model
        model.addAttribute("loyaltyPoints", loyaltyPoints); // Prosleđivanje broja bodova u model
        return "cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long travelId, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart != null) {
            cart.removeIf(item -> item.getTravelId().equals(travelId));
            session.setAttribute("cart", cart);
        }

        return "redirect:/cart/view";
    }


    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart/view";
    }

    @PostMapping("/updatePassengers")
    public String updatePassengers(@RequestParam Long travelId, @RequestParam int numberOfPassengers, HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart != null) {
            for (CartItem item : cart) {
                if (item.getTravelId().equals(travelId)) {
                    // Proveri dostupna mesta i krevete za ovaj datum putovanja
                    int availableSeats = travelService.getAvailableSeats(item.getTravelDateId());
                    int availableBeds = travelService.getAvailableBeds(item.getTravelDateId());

                    if (numberOfPassengers > availableSeats || numberOfPassengers > availableBeds) {
                        model.addAttribute("errorMessage", "Number of passengers exceeds available seats or beds.");
                        return "redirect:/cart/view";
                    }

                    // Ako je sve u redu, ažuriraj broj putnika
                    item.updateNumberOfPassengers(numberOfPassengers);
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }

        return "redirect:/cart/view";
    }
    @PostMapping("/sendAllRequests")
    public String sendAllRequests(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        User user = (User) session.getAttribute("LOGGED_IN_USER");

        if (cart != null && !cart.isEmpty()) {
            int totalPointsUsed = 0;
            for (CartItem item : cart) {
                if (item.getNumberOfPassengers() <= 0) {
                    model.addAttribute("errorMessage", "Broj putnika za svako putovanje mora biti veći od 0.");
                    model.addAttribute("cartItems", cart);
                    model.addAttribute("totalPrice", calculateTotalPrice(cart));
                    model.addAttribute("loyaltyPoints", travelService.getLoyaltyPoints(user.getId()));
                    return "cart/view";
                }
                totalPointsUsed += item.getUsedPoints();
            }

            int loyaltyPoints = travelService.getLoyaltyPoints(user.getId());
            if (totalPointsUsed > loyaltyPoints) {
                model.addAttribute("errorMessage", "Nemate dovoljno poena na loyalty kartici. Pokušali ste da iskoristite " + totalPointsUsed + " poena, a imate samo " + loyaltyPoints + ".");
                model.addAttribute("cartItems", cart);
                model.addAttribute("totalPrice", calculateTotalPrice(cart));
                model.addAttribute("loyaltyPoints", loyaltyPoints);
                return "cart/view";
            }

            for (CartItem item : cart) {
                Reservation reservation = new Reservation();
                reservation.setTravelId(item.getTravelId());
                reservation.setDepartureDate(item.getDepartureDate());
                reservation.setReturnDate(item.getReturnDate());
                reservation.setFinalPrice(item.getTotalPrice());
                reservation.setCreationDate(LocalDateTime.now());
                reservation.setStatus(ReservationStatus.REQUEST);
                reservation.setNumberOfPassengers(item.getNumberOfPassengers());
                reservation.setUserId(user.getId());
                reservation.setTravelDateId(item.getTravelDateId());
                reservation.setPoints(item.getUsedPoints());

                shoppingCartService.saveReservation(reservation);

                // Oduzimanje bodova sa loyalty kartice nakon što se rezervacija sačuva
                shoppingCartService.deductPointsFromLoyaltyCard(user.getId(), item.getUsedPoints());
            }

            cart.clear();
            session.setAttribute("cart", cart);
            return "redirect:/cart/view";
        } else {
            model.addAttribute("errorMessage", "Vaša korpa je prazna.");
            return "cart/view";
        }
    }


    private double calculateTotalPrice(List<CartItem> cart) {
        double totalPrice = 0;

        for (CartItem item : cart) {
            double unitPrice = item.getUnitPrice();
            int usedPoints = item.getUsedPoints();

            // Izračunavanje popusta na osnovu bodova (1 bod = 5% popusta)
            double discount = usedPoints * 0.05 * unitPrice;

            // Izračunavanje cene za ovaj item nakon popusta
            double itemTotalPrice = unitPrice * item.getNumberOfPassengers() - discount;

            // Dodavanje cene ovog itema ukupnoj ceni
            totalPrice += itemTotalPrice;
        }

        return totalPrice;
    }


    @PostMapping("/updatePoints")
    public ModelAndView updatePoints(@RequestParam Long travelId, @RequestParam int points, HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        User user = (User) session.getAttribute("LOGGED_IN_USER");

        ModelAndView modelAndView = new ModelAndView("cart");

        if (cart != null && user != null) {
            int loyaltyPoints = travelService.getLoyaltyPoints(user.getId());

            // Proveri da li korisnik pokušava da iskoristi više od 10 bodova
            int totalPointsInCart = cart.stream().mapToInt(CartItem::getUsedPoints).sum();
            if (totalPointsInCart + points > 10) {
                modelAndView.addObject("errorMessage", "You can't use more than 10 points per purchase.");
                modelAndView.addObject("cartItems", cart);
                modelAndView.addObject("loyaltyPoints", loyaltyPoints);
                double totalPrice = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();
                modelAndView.addObject("totalPrice", totalPrice);
                return modelAndView;
            }

            if (points > loyaltyPoints) {
                modelAndView.addObject("errorMessage", "Nemate dovoljno bodova na loyalty kartici.");
                return modelAndView;
            }

            for (CartItem item : cart) {
                if (item.getTravelId().equals(travelId)) {
                    item.setUsedPoints(points);
                    double discount = points * 0.05 * item.getUnitPrice();  // 1 bod = 5% popusta
                    item.setTotalPrice(item.getTotalPrice() - discount);
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }

        modelAndView.addObject("cartItems", cart);
        modelAndView.addObject("loyaltyPoints", travelService.getLoyaltyPoints(user.getId()));
        double totalPrice = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();
        modelAndView.addObject("totalPrice", totalPrice);

        return modelAndView;
    }

}
