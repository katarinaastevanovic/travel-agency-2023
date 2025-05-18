package com.ftn.PrviMavenVebProjekat.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.PrviMavenVebProjekat.model.Destinacija;
import com.ftn.PrviMavenVebProjekat.service.DestinacijaService;

@Controller
@RequestMapping(value="/destinacije")
public class DestinacijeController implements ServletContextAware {


	@Autowired
	private ServletContext servletContext;
	private String bURL;

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

	@GetMapping
	public ModelAndView index() throws IOException {
		List<Destinacija> destinacije = destinacijaService.findAll();
		ModelAndView result = new ModelAndView("destinacije");
		result.addObject("destinacije", destinacije);
		return result;
	}

	@GetMapping(value="/add")
	public String create(HttpServletResponse response) throws IOException {
		return "dodajDestinaciju";
	}

	@PostMapping(value="/add")
	public void create(@RequestParam String grad, @RequestParam String drzava,
					   @RequestParam String kontinent, @RequestParam("slika") MultipartFile slika,
					   HttpServletResponse response) throws IOException {
		String fileName = slika.getOriginalFilename();
		Destinacija destinacija = new Destinacija(grad, drzava, kontinent, fileName);
		destinacijaService.save(destinacija);
		response.sendRedirect(bURL + "destinacije");
	}

	@PostMapping(value="/edit")
	public void edit(@RequestParam("id") Long id,
					 @RequestParam("grad") String grad,
					 @RequestParam("drzava") String drzava,
					 @RequestParam("kontinent") String kontinent,
					 @RequestParam("novaSlika") MultipartFile novaSlika,
					 HttpServletResponse response) throws IOException {
		Destinacija destinacija = destinacijaService.findOne(id);
		if (destinacija != null) {
			destinacija.setGrad(grad);
			destinacija.setDrzava(drzava);
			destinacija.setKontinent(kontinent);

			if (!novaSlika.isEmpty()) {
				String fileName = novaSlika.getOriginalFilename();
				if (fileName != null && !fileName.isEmpty()) {
					// Definiši putanju za čuvanje slike
					String imagePath = "static/images/" + fileName;
					File imageFile = new File(servletContext.getRealPath("/") + imagePath);

					// Napravi direktorijum ako ne postoji
					imageFile.getParentFile().mkdirs();

					// Sačuvaj sliku
					novaSlika.transferTo(imageFile);
					destinacija.setSlika(fileName);
				}
			}

			destinacijaService.update(destinacija);
		}
		response.sendRedirect(bURL + "destinacije");
	}



	@PostMapping(value="/delete")
	public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {
		Destinacija destinacija = destinacijaService.findOne(id);
		if (destinacija != null) {
			destinacija.setAktivnost(false); // Postavljanje aktivnost na false
			destinacijaService.update(destinacija); // Ažuriranje destinacije
		}
		response.sendRedirect(bURL + "destinacije");
	}

	@GetMapping(value="/details")
	@ResponseBody
	public ModelAndView details(@RequestParam Long id) throws IOException {
		Destinacija destinacija = destinacijaService.findOne(id);
		ModelAndView view = new ModelAndView("editujDestinaciju");
		view.addObject("destinacija", destinacija);
		return view;
	}

}
