package com.ftn.PrviMavenVebProjekat.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Travel {
	
	private Long id;
	private Destinacija destinacija;
	private Vehicle vehicle;
	private AccUnit accUnit;
	private TypeOfTravel typeOfTravel;
	private Double price;
	private List<TravelDate> travelDates = new ArrayList<>();

	public Travel()
	{

	}

	public Travel(Long id, Destinacija destinacija, Vehicle vehicle,
			AccUnit accUnit, TypeOfTravel typeOfTravel,
			 double price) {
		super();
		this.id=id;
		this.destinacija=destinacija;
		this.vehicle=vehicle;
		this.accUnit=accUnit;
		this.typeOfTravel=typeOfTravel;
		this.price=price;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Destinacija getDestinacija() {
		return destinacija;
	}
	public void setDestinacija(Destinacija destinacija) {
		this.destinacija = destinacija;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public AccUnit getAccUnit() {
		return accUnit;
	}
	public void setAccUnit(AccUnit accUnit) {
		this.accUnit = accUnit;
	}
	public TypeOfTravel getTypeOfTravel() {
		return typeOfTravel;
	}
	public void setTypeOfTravel(TypeOfTravel typeOfTravel) {
		this.typeOfTravel = typeOfTravel;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}


	public void setTravelDates(List<TravelDate> travelDates) {
		this.travelDates = travelDates;
	}

	public List<TravelDate>  getTravelDates() {
		return travelDates;
	}

}