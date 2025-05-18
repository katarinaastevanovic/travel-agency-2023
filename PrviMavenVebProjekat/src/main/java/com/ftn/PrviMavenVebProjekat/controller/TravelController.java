package com.ftn.PrviMavenVebProjekat.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import com.ftn.PrviMavenVebProjekat.dao.impl.TravelDateDAOImpl;
import com.ftn.PrviMavenVebProjekat.model.*;
import com.ftn.PrviMavenVebProjekat.service.AccUnitService;
import com.ftn.PrviMavenVebProjekat.service.DestinacijaService;
import com.ftn.PrviMavenVebProjekat.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import com.ftn.PrviMavenVebProjekat.service.TravelService;

@Controller
@RequestMapping(value="/travels")
public class TravelController implements ServletContextAware{

	@Autowired
	private ServletContext servletContext;
	private String bURL;

	@Autowired
	private TravelService travelService;

	@Autowired
	private DestinacijaService destinacijaService;

	@Autowired
	private AccUnitService accUnitService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private TravelDateDAOImpl travelDateDAO;

	@PostConstruct
	public void init() {
		bURL = servletContext.getContextPath() + "/";
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@GetMapping
	public ModelAndView index( Model model) throws IOException {
		List<Travel> travels = travelService.findAll();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		for (Travel travel : travels) {
			for (TravelDate travelDate : travel.getTravelDates()) {
				travelDate.setFormattedArrivalDate(travelDate.getArrivalDate().format(formatter));
				travelDate.setFormattedDepartureDate(travelDate.getDepartureDate().format(formatter));
			}
		}

		ModelAndView modelAndView = new ModelAndView("travels");
		modelAndView.addObject("travels", travels);
		List<TravelDate> travelDates = travelDateDAO.findAll();
		model.addAttribute("travelDates", travelDates);
		return modelAndView;
	}

	@GetMapping("/add")
	public ModelAndView showAddTravelForm(@RequestParam(value = "destinacijaId", required = false) Long destinacijaId) {
		ModelAndView modelAndView = new ModelAndView("addTravel");

		List<Destinacija> destinations = destinacijaService.findAll();
		modelAndView.addObject("destinacije", destinations);

		if (destinacijaId != null) {
			List<Vehicle> vehicles = vehicleService.findByDestinacijaId(destinacijaId);
			List<AccUnit> accUnits = accUnitService.findByDestinacijaId(destinacijaId);

			modelAndView.addObject("vehicles", vehicles);
			modelAndView.addObject("accUnits", accUnits);
		} else {
			modelAndView.addObject("vehicles", List.of());
			modelAndView.addObject("accUnits", List.of());
		}

		modelAndView.addObject("typesOfTravel", TypeOfTravel.values());
		modelAndView.addObject("selectedDestinacijaId", destinacijaId);

		return modelAndView;
	}

	@PostMapping(value = "/add")
	public String addNewTravel(@RequestParam Long destinacijaId,
							   @RequestParam Long vehicleId,
							   @RequestParam Long accUnitId,
							   @RequestParam TypeOfTravel typeOfTravel,
							   @RequestParam List<String> arrivalDates,
							   @RequestParam List<String> departureDates,
							   @RequestParam Double cena) {
		Travel travel = new Travel();
		travel.setDestinacija(destinacijaService.findOne(destinacijaId));
		Vehicle vehicle = vehicleService.findOne(vehicleId);
		travel.setVehicle(vehicle);
		travel.setAccUnit(accUnitService.findOne(accUnitId));
		travel.setTypeOfTravel(typeOfTravel);
		travel.setPrice(cena);
		travel.setTravelDates(new ArrayList<>());

		travelService.save(travel);

		int numberOfFreeBeds = travelDateDAO.getNumberOfFreeBeds(travel.getId());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		for (int i = 0; i < arrivalDates.size(); i++) {
			LocalDate arrivalDate = LocalDate.parse(arrivalDates.get(i), formatter);
			LocalDate departureDate = LocalDate.parse(departureDates.get(i), formatter);

			TravelDate travelDate = new TravelDate();
			travelDate.setTravelId(travel.getId());
			travelDate.setArrivalDate(arrivalDate.atStartOfDay());
			travelDate.setDepartureDate(departureDate.atStartOfDay());
			travelDate.setNumberOfNights(calculateNumberOfNights(arrivalDate, departureDate));
			travelDate.setNumberOfFreeSeats(vehicle.getNumberOfSeats());
			travelDate.setNumberOfFreeBeds(numberOfFreeBeds); // Postavljanje broja slobodnih kreveta
			travelDateDAO.save(travelDate);

			travel.getTravelDates().add(travelDate);
		}

		return "redirect:/travels";
	}


	private int calculateNumberOfNights(LocalDate arrival, LocalDate departure) {
		return (int) java.time.Duration.between(arrival.atStartOfDay(), departure.atStartOfDay()).toDays();
	}

	@GetMapping("/image/{filename}")
	public ResponseEntity<Resource> getImage(@PathVariable String filename) {
		try {
			Path imagePath = Paths.get("path/to/images", filename);
			ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(imagePath));
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
					.body(resource);
		} catch (IOException e) {
			return ResponseEntity.notFound().build();
		}
	}


}
