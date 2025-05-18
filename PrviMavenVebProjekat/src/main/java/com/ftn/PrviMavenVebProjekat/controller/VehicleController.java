package com.ftn.PrviMavenVebProjekat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;


import com.ftn.PrviMavenVebProjekat.service.DestinacijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.PrviMavenVebProjekat.dao.VehicleDAO;
import com.ftn.PrviMavenVebProjekat.model.Vehicle;
import com.ftn.PrviMavenVebProjekat.model.Destinacija;
import com.ftn.PrviMavenVebProjekat.model.TypeOfAccommodationUnit;
import com.ftn.PrviMavenVebProjekat.service.VehicleService;

@Controller
@RequestMapping(value="/vehicles")
public class VehicleController implements ServletContextAware {

	public static final String DESTINACIJE_KEY = "vehicles";
	
	@Autowired
	private ServletContext servletContext;
	private  String bURL; 
	
	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private DestinacijaService destinacijaService;
	

	
	@PostConstruct
	public void init() {	
		bURL = servletContext.getContextPath()+"/";
	}
	/** pristup ApplicationContext */
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	/** pribavnjanje HTML stanice za prikaz svih entiteta, get zahtev */
	// GET: knjige
	@GetMapping
	public ModelAndView index() throws IOException{	
		
		//Vehicles vehicles = (Vehicles) memorijaAplikacije.get(VehiclesController.DESTINACIJE_KEY);
		
		List<Vehicle> vehicles = vehicleService.findAll();
		ModelAndView result = new ModelAndView("vehicles");
		result.addObject("vehicles",vehicles);
		return result;
	}
	
	/** pribavnjanje HTML stanice za unos novog entiteta, get zahtev */
	// GET: knjige/dodaj
	@GetMapping(value="/add")
	public ModelAndView create() {
		ModelAndView modelAndView = new ModelAndView("addVehicle");
		List<Destinacija> destinacije = destinacijaService.findAll(); // Dobavljanje svih destinacija
		modelAndView.addObject("destinacije", destinacije);
		return modelAndView;
	}
	
	/** obrada podataka forme za unos novog entiteta, post zahtev */
	// POST: knjige/add
	@SuppressWarnings("unused")
	@PostMapping(value="/add")
	public void create( @RequestParam String name,
						@RequestParam int numberOfSeats,
						@RequestParam  Long finalDestinationId,
						@RequestParam String descriptions,
						HttpServletResponse response) throws IOException {

		Destinacija finalDestination = destinacijaService.findOne(finalDestinationId);
		if (finalDestination == null) {
			// Handle the case where the Destinacija is not found
			response.sendRedirect(bURL + "vehicles?error=Destination not found");
			return;
		}

		Vehicle vehicle = new Vehicle(name, numberOfSeats, finalDestination, descriptions);
		vehicle.setActive(true); // Set active to true before saving
		Vehicle saved = vehicleService.save(vehicle);

		response.sendRedirect(bURL + "vehicles");
	}


	/** obrada podataka forme za izmenu postojećeg entiteta, post zahtev */
	// POST: knjige/edit
	@SuppressWarnings("unused")
	@PostMapping(value="/edit")
	public void edit(@RequestParam("id") Long id,
					 @RequestParam("name") String name,
					 @RequestParam("numberOfSeats") int numberOfSeats,
					 @RequestParam("descriptions") String descriptions,
					 HttpServletResponse response) throws IOException {
		Vehicle vehicle = vehicleService.findOne(id);
		if (vehicle != null) {
			vehicle.setName(name);
			vehicle.setNumberOfSeats(numberOfSeats);
			vehicle.setDescriptions(descriptions);

			vehicleService.update(vehicle);
		}
		response.sendRedirect(bURL + "vehicles");
	}


	/** obrada podataka forme za za brisanje postojećeg entiteta, post zahtev */
	// POST: knjige/delete
	@PostMapping(value="/delete")
	public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {
		vehicleService.delete(id);
		response.sendRedirect(bURL + "vehicles");
	}
		
	
	/** pribavnjanje HTML stanice za prikaz određenog entiteta , get zahtev */
	// GET: knjige/details?id=1
	@GetMapping(value="/details")
	@ResponseBody
	public ModelAndView details(@RequestParam Long id) throws IOException{	
			Vehicle vehicle = vehicleService.findOne(id);
			ModelAndView view = new ModelAndView("editVehicle");
			view.addObject("vehicle",vehicle);
			return view;
	}
}
