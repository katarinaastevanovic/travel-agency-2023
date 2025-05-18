package com.ftn.PrviMavenVebProjekat.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccUnit {
	//lol
	private Long id;
	private String namee;
	private TypeOfAccommodationUnit typeOfAccommodationUnit;	
	private int capacity;
	private Destinacija destinacija;
	private String services;
	private String description;

	private boolean active;


	public AccUnit() {
		this.active = true;  // Podrazumevano aktivan
	}
	public AccUnit(Long id, String namee, TypeOfAccommodationUnit typeOfAccommodationUnit, int capacity, Destinacija destinacija, String services, String description) {
		super();
		this.id = id;
		this.namee = namee;
		this.typeOfAccommodationUnit = typeOfAccommodationUnit;
		this.capacity = capacity;
		this.destinacija = destinacija;
		this.services = services;
		this.description = description;
		this.active = true;  // Podrazumevano aktivan
	}


	public AccUnit(String namee,
			TypeOfAccommodationUnit typeOfAccommodationUnit,	
			 int capacity,Destinacija destinacija,
			 String services,String description) {
				super();
				this.namee = namee;
				this.typeOfAccommodationUnit = typeOfAccommodationUnit;
				this.capacity = capacity;
				this.destinacija = destinacija;
				this.services=services;
				this.description=description;
				this.active = true;
			}

	public List<Services> getServicesList() {
		return Stream.of(services.split(","))
				.map(Services::valueOf)
				.collect(Collectors.toList());
	}


	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setNamee(String namee) {
		this.namee = namee;
	}

	public Long getId() {
	    return id;
	}

	public void setId(Long id) {
	    this.id = id;
	}

	public String getNamee() {
		return namee;
	}

	public void setName(String name) {
		this.namee = name;
	}

	public TypeOfAccommodationUnit getTypeOfAccommodationUnit() {
		return typeOfAccommodationUnit;
	}

	public void setTypeOfAccommodationUnit(TypeOfAccommodationUnit typeOfAccommodationUnit) {
		this.typeOfAccommodationUnit = typeOfAccommodationUnit;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Destinacija getDestinacija() {
		return destinacija;
	}

	public void setDestinacija(Destinacija destinacija) {
		this.destinacija = destinacija;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
