package com.ftn.PrviMavenVebProjekat.controller;

import com.ftn.PrviMavenVebProjekat.model.Reservation;
import com.ftn.PrviMavenVebProjekat.model.Travel;
import com.ftn.PrviMavenVebProjekat.model.Vehicle;
import com.ftn.PrviMavenVebProjekat.service.ReservationService;
import com.ftn.PrviMavenVebProjekat.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TravelService travelService;



    @GetMapping
    public String getReservationReports(
            @RequestParam(value = "sortColumn", required = false) String sortColumn,
            @RequestParam(value = "sortOrder", required = false) String sortOrder,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            Model model) {

        List<Map<String, Object>> reports = travelService.findTravelReports(sortColumn, sortOrder, fromDate, toDate);

        // Računanje ukupnog broja prodati putovanja i ukupne cene
        int totalSoldTrips = reports.size();
        BigDecimal totalPriceSum = reports.stream()
                .map(report -> (BigDecimal) report.get("totalPrice"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("reservations", reports);
        model.addAttribute("totalSoldTrips", totalSoldTrips);
        model.addAttribute("totalPriceSum", totalPriceSum);

        return "reports";
    }
}
