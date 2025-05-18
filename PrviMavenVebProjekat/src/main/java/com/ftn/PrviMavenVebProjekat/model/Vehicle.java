package com.ftn.PrviMavenVebProjekat.model;

public class Vehicle {

	private Long id;
	private int numberOfSeats;
	private Destinacija finalDestination;
	private String descriptions;
	private String name;
	private boolean active; // Dodano polje za logičko brisanje

	public Vehicle() {}

	public Vehicle(Long id, String name, int numberOfSeats, Destinacija finalDestination, String descriptions, boolean active) {
		super();
		this.id = id;
		this.name = name;
		this.numberOfSeats = numberOfSeats;
		this.finalDestination = finalDestination;
		this.descriptions = descriptions;
		this.active = active;
	}

	public Vehicle(String name, int numberOfSeats, Destinacija finalDestination, String descriptions, boolean active) {
		super();
		this.name = name;
		this.numberOfSeats = numberOfSeats;
		this.finalDestination = finalDestination;
		this.descriptions = descriptions;
		this.active = active;
	}
	public Vehicle(String name, int numberOfSeats, Destinacija finalDestination, String descriptions) {
		super();
		this.name = name;
		this.numberOfSeats = numberOfSeats;
		this.finalDestination = finalDestination;
		this.descriptions = descriptions;
		this.active = true; // Automatski postavljamo na true
	}




	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	public Destinacija getFinalDestination() {
		return finalDestination;
	}

	public void setFinalDestination(Destinacija finalDestination) {
		this.finalDestination = finalDestination;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
