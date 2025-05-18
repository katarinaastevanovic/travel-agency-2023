package com.ftn.PrviMavenVebProjekat.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.ftn.PrviMavenVebProjekat.controller.UserController;
import com.ftn.PrviMavenVebProjekat.model.User;



public class InitHttpSessionListener implements HttpSessionListener{

	
	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println("Inicijalizacija sesisje HttpSessionListener...");
//		
//		//pri kreiranju sesije inicijalizujemo je ili radimo neke dodatne aktivnosti
		List<User> loggedInUser = new ArrayList<User>();
		
		HttpSession session  = arg0.getSession();
		System.out.println("session id korisnika je "+session.getId());
		session.setAttribute(UserController.LOGGED_IN_USER, loggedInUser);
		
		
//		
		System.out.println("Uspeh HttpSessionListener!");
	}
	
	/** kod koji se izvrsava po brisanju sesije */
	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("Brisanje sesisje HttpSessionListener...");
		
		System.out.println("Uspeh HttpSessionListener!");
	}
}
