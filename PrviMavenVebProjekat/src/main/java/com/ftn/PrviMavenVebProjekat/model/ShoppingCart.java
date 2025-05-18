package com.ftn.PrviMavenVebProjekat.model;

public class ShoppingCart {
	private Long id;
	private User user;
	private Travel travel;

	private Long travelDateId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Travel getTravel() {
		return travel;
	}

	public void setTravel(Travel travel) {
		this.travel = travel;
	}

	public Long getTravelDateId() {
		return travelDateId;
	}

	public void setTravelDateId(Long travelDateId) {
		this.travelDateId = travelDateId;
	}


}
