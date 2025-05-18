package com.ftn.PrviMavenVebProjekat.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.ftn.PrviMavenVebProjekat.dao.AccUnitDAO;
import com.ftn.PrviMavenVebProjekat.dao.impl.TravelDateDAOImpl;
import com.ftn.PrviMavenVebProjekat.model.TravelDate;
import com.ftn.PrviMavenVebProjekat.model.TypeOfTravel;
import com.ftn.PrviMavenVebProjekat.model.User;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.PrviMavenVebProjekat.model.Travel;
import com.ftn.PrviMavenVebProjekat.service.DestinacijaService;
import com.ftn.PrviMavenVebProjekat.service.TravelService;

@Controller
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	private final ResourceLoader resourceLoader;

	@Autowired
	private ServletContext servletContext;
	private String bURL;

	@Autowired
	private DestinacijaService destinacijaService;

	@Autowired
	private TravelService travelService;

	@Autowired
	private TravelDateDAOImpl travelDateDAO;

	public MainController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Autowired
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@PostConstruct
	public void init() {
		bURL = servletContext.getContextPath() + "/";
	}

	@GetMapping("/")
	public ModelAndView index(
			@RequestParam(value = "typeOfAccommodationUnit", required = false) String typeOfAccommodationUnit,
			@RequestParam(value = "typeOfTravel", required = false) String typeOfTravel,
			@RequestParam(value = "typeOfVehicle", required = false) String typeOfVehicle,
			@RequestParam(value = "destinationCity", required = false) String destinationCity,
			@RequestParam(value = "priceFrom", required = false) String priceMin,
			@RequestParam(value = "priceTo", required = false) String priceMax,
			@RequestParam(value = "dateFrom", required = false) String dateFrom,
			@RequestParam(value = "dateTo", required = false) String dateTo,
			@RequestParam(value = "minNights", required = false) Integer minNights,
			@RequestParam(value = "maxNights", required = false) Integer maxNights,
			@RequestParam(value = "minFreeSeats", required = false) Integer minFreeSeats,
			@RequestParam(value = "travelId", required = false) Long travelId,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			HttpSession session,Model model) {

		// Dohvati korisnika iz sesije
		User loggedInUser = (User) session.getAttribute("LOGGED_IN_USER");
		boolean isPassenger = loggedInUser != null && loggedInUser.isPassenger();

		// Dohvati sve relevantne putovanje na osnovu korisnika
		List<Travel> travels = isPassenger
				? travelService.findAllByParamsForPassenger(typeOfAccommodationUnit, typeOfTravel, typeOfVehicle, destinationCity, priceMin, priceMax, dateFrom, dateTo, minNights, maxNights, minFreeSeats, travelId)
				: travelService.findAllByParams(typeOfAccommodationUnit, typeOfTravel, typeOfVehicle, destinationCity, priceMin, priceMax, dateFrom, dateTo, minNights, maxNights, minFreeSeats, travelId);

		// Sortiranje prema odabranim parametrima
		if (sortBy != null && !sortBy.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
			Comparator<Travel> comparator = null;
			switch (sortBy) {
				case "price":
					comparator = Comparator.comparing(Travel::getPrice);
					break;
				case "dateFrom":
					comparator = Comparator.comparing(t -> t.getTravelDates().get(0).getArrivalDate());
					break;
				case "dateTo":
					comparator = Comparator.comparing(t -> t.getTravelDates().get(0).getDepartureDate());
					break;
				case "destinationCity":
					comparator = Comparator.comparing(t -> t.getDestinacija().getGrad());
					break;
				case "typeOfAccommodationUnit":
					comparator = Comparator.comparing(t -> t.getAccUnit().getTypeOfAccommodationUnit());
					break;
				case "typeOfTravel":
					comparator = Comparator.comparing(Travel::getTypeOfTravel);
					break;
				case "typeOfVehicle":
					comparator = Comparator.comparing(t -> t.getVehicle().getName());
					break;
			}
			if (comparator != null) {
				if ("desc".equals(sortOrder)) {
					comparator = comparator.reversed();
				}
				travels.sort(comparator);
			}
		}
		List<TypeOfTravel> typesOfTravel = List.of(TypeOfTravel.values());

		ModelAndView modelAndView = new ModelAndView("main");
		modelAndView.addObject("travels", travels);
		modelAndView.addObject("typesOfTravel", typesOfTravel);
		List<TravelDate> travelDates = travelDateDAO.findAll();
		model.addAttribute("travelDates", travelDates);

		return modelAndView;
	}


	@GetMapping("/details")
	@ResponseBody
	public ModelAndView details(Long id, Model model) {
		Travel travel = travelService.findOne(id);

		// Formatiranje datuma
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		for (TravelDate travelDate : travel.getTravelDates()) {
			travelDate.setFormattedArrivalDate(travelDate.getArrivalDate().format(formatter));
			travelDate.setFormattedDepartureDate(travelDate.getDepartureDate().format(formatter));
		}

		// Dodato čitanje slike iz destinacije
		logger.info("Travel ID: {}, Image: {}", travel.getId(), travel.getDestinacija().getSlika());
		ModelAndView modelAndView = new ModelAndView("travelDetails");
		modelAndView.addObject("putovanje", travel);
		List<TravelDate> travelDates = travelDateDAO.findAll();
		model.addAttribute("travelDates", travelDates);
		return modelAndView;
	}

	@GetMapping("/images/{filename:.+}")
	public ResponseEntity<Resource> getImage(@PathVariable String filename) {
		try {
			Resource resource = resourceLoader.getResource("classpath:/static/images/" + filename);
			if (resource.exists()) {
				return ResponseEntity.ok().body(resource);
			} else {
				logger.error("Image {} not found in classpath:/static/images/", filename);
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			logger.error("Error loading image {}", filename, e);
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/specialDates/add")
	public String addSpecialDate(@RequestParam(required = false) List<Long> travelDateIds,
								 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
								 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
								 @RequestParam(required = false) Integer discount,
								 @RequestParam String filterOptions,
								 @RequestParam(required = false) TypeOfTravel typeOfTravel,
								 Model model) {

		logger.info("Received travelDateIds: {}", travelDateIds);
		logger.info("Received startDate: {}", startDate);
		if (endDate != null) {
			logger.info("Received endDate: {}", endDate);
		}
		if (discount != null) {
			logger.info("Received discount: {}", discount);
		}
		logger.info("Received typeOfTravel: {}", typeOfTravel);

		List<TravelDate> travelDatesToUpdate = new ArrayList<>();
		String updateType = "ALL"; // Default

		// Determine which update type to use
		if (travelDateIds != null && !travelDateIds.isEmpty()) {
			travelDatesToUpdate = travelDateDAO.findByTravelIds(travelDateIds);
			updateType = "ID";
			logger.info("Updating travel dates by IDs: {}", travelDateIds);
		} else if (typeOfTravel != null) {
			travelDatesToUpdate = travelDateDAO.findByTypeOfTravel(typeOfTravel);
			updateType = "TYPE";
			logger.info("Updating travel dates by TypeOfTravel: {}", typeOfTravel);
		} else {
			travelDatesToUpdate = travelDateDAO.findAll();
			updateType = "ALL";
			logger.info("Updating all travel dates");
		}

		// Check travel dates to update
		if (!travelDatesToUpdate.isEmpty()) {
			for (TravelDate travelDate : travelDatesToUpdate) {
				logger.info("Updating TravelDate ID: {}", travelDate.getId());
				if (discount != null && discount > 0) {
					travelDate.setDiscount(discount);
					travelDate.setDiscountStartDate(startDate.atStartOfDay());

					if (endDate != null) {
						travelDate.setDiscountEndDate(endDate.atStartOfDay());
					} else {
						travelDate.setDiscountEndDate(startDate.atStartOfDay());
					}

					// Print current travel date info
					logger.info("Before update - TravelDate ID: {}, Discount: {}, Start Date: {}, End Date: {}, Price: {}",
							travelDate.getId(), travelDate.getDiscount(), travelDate.getDiscountStartDate(), travelDate.getDiscountEndDate(), travelDate.getDiscountPrice());

					// Update the travel date
					travelDateDAO.update(travelDate, updateType, travelDateIds, typeOfTravel);

					// Print after update
					logger.info("After update - TravelDate ID: {}, Discount: {}, Start Date: {}, End Date: {}, Price: {}",
							travelDate.getId(), travelDate.getDiscount(), travelDate.getDiscountStartDate(), travelDate.getDiscountEndDate(), travelDate.getDiscountPrice());
				}
			}
		} else {
			logger.warn("No travel dates found to update based on provided criteria.");
		}

		List<Travel> travelList = travelService.findAll();
		model.addAttribute("travels", travelList);
		List<TravelDate> travelDates = travelDateDAO.findAll();
		model.addAttribute("travelDates", travelDates);
		logger.info("Travel Dates fetched: {}", travelDates);

		model.addAttribute("successMessage", "Special dates updated successfully!");
		return "main :: fragmentTravels";
	}


}