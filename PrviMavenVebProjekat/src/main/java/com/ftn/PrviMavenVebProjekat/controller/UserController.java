package com.ftn.PrviMavenVebProjekat.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.sql.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.PrviMavenVebProjekat.model.Role;
import com.ftn.PrviMavenVebProjekat.model.User;
import com.ftn.PrviMavenVebProjekat.service.UserService;
@Controller
@RequestMapping("/users")
	public class UserController implements ServletContextAware {

		public static final String USER_KEY = "users";
		public static final String LOGGED_IN_USER = "LOGGED_IN_USER";
		
		@Autowired
		private ServletContext servletContext;
		private  String bURL; 
		
		@Autowired
		private UserService userService;
		
		
		@PostConstruct
		public void init() {	
			bURL = servletContext.getContextPath()+"/";
		  
		}
		@Override
		public void setServletContext(ServletContext servletContext) {
			this.servletContext = servletContext;
		}

	@GetMapping
	public ModelAndView index(@RequestParam(value = "sortField", required = false) String sortField,
							  @RequestParam(value = "sortOrder", required = false) String sortOrder,
							  @RequestParam(value = "role", required = false) String role,
							  @RequestParam(value = "keyword", required = false) String keyword) throws IOException {
		List<User> users;

		if (keyword != null && !keyword.isEmpty()) {
			users = userService.searchByUsername(keyword);
		} else if (role != null && !role.isEmpty()) {
			users = userService.findAllByRole(role);
		} else if (sortField != null && !sortField.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
			if (sortField.equals("role")) {
				sortField = "uloga";
			}
			users = userService.findAll(sortField, sortOrder);
		} else {
			users = userService.findAll();
		}

		ModelAndView result = new ModelAndView("users");
		result.addObject("users", users);
		result.addObject("selectedSortField", sortField);
		result.addObject("selectedSortOrder", sortOrder);
		result.addObject("selectedRole", role);
		result.addObject("keyword", keyword);
		return result;
	}

	@GetMapping("/search")
	public String searchUsers(@RequestParam("keyword") String keyword, Model model) {
		List<User> users = userService.searchByUsername(keyword);
		model.addAttribute("users", users);
		return "fragments/userTable :: userTable";
	}
	@PostMapping("/login")
	public String loginKorisnik(@RequestParam String identifier, @RequestParam String password, Model model,
								HttpServletRequest request, HttpSession session) throws IOException {

		User user = null;

		// Provera da li je uneti podatak email ili username
		if (identifier.contains("@")) {
			user = userService.findOneByEmail(identifier);
		} else {
			user = userService.findOneByUsername(identifier);
		}

		session.invalidate();
		session = request.getSession(true);

		if (user != null && password.equals(user.getPassword()) && !user.getIsBlocked()) {
			session.setAttribute(LOGGED_IN_USER, user);
			return "redirect:/";
		} else {
			model.addAttribute("error", true);
			if (user == null) {
				model.addAttribute("errorMessage", "Neuspela prijava. Korisnik sa unetim podacima ne postoji.");
			} else if (user.getIsBlocked()) {
				model.addAttribute("errorMessage", "Neuspela prijava. Korisnički nalog je blokiran.");
			} else {
				model.addAttribute("errorMessage", "Neuspela prijava. Proverite korisničko ime ili email i lozinku.");
			}
			return "login";
		}
	}

	       @GetMapping("/logout")
	        public String logout(HttpSession session) {
	            session.invalidate();
	            return "redirect:/";
	        }
	       
	       @GetMapping("/loginForm")
	       public String login() {
	    	   return "login";
	       }
		@GetMapping(value="/add")
	
		public String create(HttpServletResponse response)throws IOException {
			return "addUser";
		}
		
		/** obrada podataka forme za unos novog entiteta, post zahtev */
		// POST: knjige/add
		@SuppressWarnings("unused")
		@PostMapping(value="/add")
		public String create(@RequestParam(required=true) String username, @RequestParam(required=true) String password,
							 @RequestParam(required=true) String name, @RequestParam(required=true) String lastName,
							 @RequestParam(required=true) String email, @RequestParam(required=true) Date dateOfBirth,
							 @RequestParam(required=true) String address, @RequestParam(required=true) String number,
							 HttpServletResponse response, Model model, HttpSession session) throws IOException {

			// Provera postojanja korisnika sa istim emailom
			if (userService.existsByEmail(email)) {
				model.addAttribute("errorMessage", "This email is already taken.");
				return "addUser";
			}

			// Provera postojanja korisnika sa istim username-om
			if (userService.existsByUsername(username)) {
				model.addAttribute("errorMessage", "This username is already taken.");
				return "addUser";
			}

			LocalDateTime dateOfReg = LocalDateTime.now();
			User user = new User(username, password, name, lastName, email, dateOfBirth, address, number, dateOfReg, Role.PASSENGER, false);
			userService.save(user);

			// Redirect to login page after successful registration
			User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER);
			if (loggedInUser != null && loggedInUser.getRole() == Role.ADMIN) {
				return "redirect:/users";
			}

			return "redirect:/users/loginForm";
		}

	/** obrada podataka forme za izmenu postojećeg entiteta, post zahtev */
		// POST: knjige/edit
	@SuppressWarnings("unused")
	@PostMapping(value="/edit")
	public void edit(@ModelAttribute User userEdited, HttpServletResponse response,HttpServletRequest request, HttpSession session) throws IOException {
		userService.update(userEdited);

		// Ažuriraj korisnika u sesiji ako je ulogovan korisnik taj koji je upravo izmenjen
		User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER);
		if (loggedInUser != null && loggedInUser.getId().equals(userEdited.getId())) {
			session.setAttribute(LOGGED_IN_USER, userEdited);
		}

		// Proveri rolu ulogovanog korisnika za redirect
		if (loggedInUser.getRole() == Role.ADMIN) {
			response.sendRedirect(bURL + "users");
		} else {
			response.sendRedirect(bURL + "users/profile");
		}
	}



	/** obrada podataka forme za za brisanje postojećeg entiteta, post zahtev */
		// POST: knjige/delete
		@PostMapping(value="/delete")
		public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {
			//Destinacije destinacije = (Destinacije) memorijaAplikacije.get(DestinacijeController.DESTINACIJE_KEY);
			User deleted = userService.delete(id);
			response.sendRedirect(bURL + "users");
			
		}
		@PostMapping(value="/block")
		public void block(@RequestParam Long id, HttpServletResponse response) throws IOException {
			//Destinacije destinacije = (Destinacije) memorijaAplikacije.get(DestinacijeController.DESTINACIJE_KEY);
			User blocked = userService.block(id);
			response.sendRedirect(bURL + "users");
			
		}
		@PostMapping(value="/unblock")
		public void unblock(@RequestParam Long id, HttpServletResponse response) throws IOException {
			//Destinacije destinacije = (Destinacije) memorijaAplikacije.get(DestinacijeController.DESTINACIJE_KEY);
			User unblocked = userService.unblock(id);
			response.sendRedirect(bURL + "users");
			
		}
		
		/** pribavnjanje HTML stanice za prikaz određenog entiteta , get zahtev */
		// GET: knjige/details?id=1
		@GetMapping(value="/details")
		@ResponseBody
		public ModelAndView details(@RequestParam Long id) throws IOException{	
				User user = userService.findOneById(id);
			
				ModelAndView view = new ModelAndView("editUser");
				view.addObject("user",user);
				return view;
		}
		@GetMapping(value="/profile")
	    public String showProfilePage()throws IOException {
			
			return "profile";
	    }




	}