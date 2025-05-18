package com.ftn.PrviMavenVebProjekat.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.PrviMavenVebProjekat.model.AccUnit;
import com.ftn.PrviMavenVebProjekat.model.Destinacija;
import com.ftn.PrviMavenVebProjekat.model.Services;
import com.ftn.PrviMavenVebProjekat.model.TypeOfAccommodationUnit;
import com.ftn.PrviMavenVebProjekat.service.AccUnitService;
import com.ftn.PrviMavenVebProjekat.service.DestinacijaService;

@Controller
@RequestMapping(value="/accUnits")
public class AccUnitController implements ServletContextAware {

	@Autowired
	private ServletContext servletContext;
	private String bURL;

	@Autowired
	private AccUnitService accUnitService;

	@Autowired
	private DestinacijaService destinacijaService;

	@PostConstruct
	public void init() {
		bURL = servletContext.getContextPath() + "/";
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Destinacija.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				Destinacija destinacija = destinacijaService.findOne(Long.valueOf(text));
				setValue(destinacija);
			}
		});
	}

	@GetMapping
	public ModelAndView index() throws IOException {
		List<AccUnit> accUnits = accUnitService.findAll();
		ModelAndView result = new ModelAndView("accUnits");
		result.addObject("accUnits", accUnits);
		return result;
	}

	@GetMapping(value = "/add")
	public ModelAndView create() {
		List<Destinacija> listaDestinacija = destinacijaService.findAll();
		ModelAndView result = new ModelAndView("addAccUnit");
		result.addObject("destinacije", listaDestinacija);
		result.addObject("typesOfAccommodationUnit", TypeOfAccommodationUnit.values());
		result.addObject("services", Services.values());
		return result;
	}

	@PostMapping(value = "/add")
	public void create(@RequestParam String namee,
					   @RequestParam TypeOfAccommodationUnit typeOfAccommodationUnit,
					   @RequestParam int capacity,
					   @RequestParam Long destinacija,
					   @RequestParam List<Services> services,
					   @RequestParam String description,
					   HttpServletResponse response) throws IOException {
		Destinacija destinacijaModel = destinacijaService.findOne(destinacija);
		String servicesString = services.stream().map(Enum::name).collect(Collectors.joining(","));
		AccUnit accUnit = new AccUnit(namee, typeOfAccommodationUnit, capacity,
				destinacijaModel, servicesString, description);
		AccUnit saved = accUnitService.save(accUnit);

		response.sendRedirect(bURL + "accUnits");
	}

	@PostMapping(value = "/edit")
	public void edit(@ModelAttribute AccUnit destinacijaEdited, HttpServletResponse response) throws IOException {
		accUnitService.update(destinacijaEdited);
		response.sendRedirect(bURL + "accUnits");
	}
	@GetMapping("/edit")
	public String editAccUnitForm(Model model, @RequestParam Long id) {
		AccUnit accUnit = accUnitService.findOne(id);
		List<String> availableServices = getAvailableServices();
		List<Services> selectedServices = accUnit.getServicesList();

		System.out.println("Available Services: " + availableServices);
		System.out.println("Selected Services: " + selectedServices);

		List<Destinacija> listaDestinacija = destinacijaService.findAll();
		model.addAttribute("accUnit", accUnit);
		model.addAttribute("destinacije", listaDestinacija);
		model.addAttribute("typesOfAccommodationUnit", TypeOfAccommodationUnit.values());
		model.addAttribute("availableServices", availableServices);
		model.addAttribute("selectedServices", selectedServices);
		return "editAccUnit";
	}


	private List<String> getAvailableServices() {
		List<String> services = Arrays.stream(Services.values())
				.map(Services::name)
				.collect(Collectors.toList());
		System.out.println("Services: " + services);
		return services;
	}


	@PostMapping(value = "/delete")
	public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {
		accUnitService.delete(id);
		response.sendRedirect(bURL + "accUnits");
	}

	@GetMapping(value = "/details")
	public String details(@RequestParam Long id, Model model) {
		AccUnit accUnit = accUnitService.findOne(id);
		List<String> availableServices = getAvailableServices();
		List<Services> selectedServices = accUnit.getServicesList();

		System.out.println("Available Services: " + availableServices);
		System.out.println("Selected Services: " + selectedServices);

		List<Destinacija> listaDestinacija = destinacijaService.findAll();
		model.addAttribute("accUnit", accUnit);
		model.addAttribute("destinacije", listaDestinacija);
		model.addAttribute("typesOfAccommodationUnit", TypeOfAccommodationUnit.values());
		model.addAttribute("availableServices", availableServices);
		model.addAttribute("selectedServices", selectedServices);
		return "editAccUnit";
	}

}