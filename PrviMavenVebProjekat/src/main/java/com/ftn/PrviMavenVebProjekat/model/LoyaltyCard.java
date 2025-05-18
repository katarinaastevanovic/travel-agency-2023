package com.ftn.PrviMavenVebProjekat.model;

public class LoyaltyCard {

	public LoyaltyCard() {
		super();
	}


	private Long id;
	private Long userId;
	private int discount;
	private int numberOfPoints;
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public int getDiscount() {
		return discount;
	}


	public void setDiscount(int discount) {
		this.discount = discount;
	}


	public int getNumberOfPoints() {
		return numberOfPoints;
	}


	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}


	public LoyaltyCard(Long id,Long userId, int discount, int numberOfPoints) {
		super();
		this.id = id;
		this.userId = userId;
		this.discount = discount;
		this.numberOfPoints = numberOfPoints;
	}
}
