package com.ftn.PrviMavenVebProjekat.service.impl;

import java.util.List;
import java.util.Map;

import com.ftn.PrviMavenVebProjekat.model.TypeOfTravel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ftn.PrviMavenVebProjekat.dao.TravelDAO;
import com.ftn.PrviMavenVebProjekat.model.Travel;
import com.ftn.PrviMavenVebProjekat.service.TravelService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseTravelServiceImpl implements TravelService{

	@Autowired
	private TravelDAO travelDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public Travel findOne(Long id) {
		return travelDAO.findOne(id);
	}

	@Override
	public List<Travel> findAll() {
		return travelDAO.findAll();
	}

	@Override
	@Transactional
	public void save(Travel travel) {
		travelDAO.save(travel);
	}

	@Override
	public int getAvailableSeats(Long travelDateId) {
		return travelDAO.findAvailableSeatsByTravelDateId(travelDateId);
	}

	@Override
	public int getAvailableBeds(Long travelDateId) {
		return travelDAO.findAvailableBedsByTravelDateId(travelDateId);
	}

	@Override
	public List<Travel> findAllByParams(String typeOfAccommodationUnit, String typeOfTravel, String typeOfVehicle, String destinationCity, String priceMin, String priceMax, String dateFrom, String dateTo, Integer minNights, Integer maxNights, Integer minFreeSeats, Long travelId) {
		return travelDAO.findAllByParams(typeOfAccommodationUnit, typeOfTravel, typeOfVehicle, destinationCity, priceMin, priceMax, dateFrom, dateTo,minNights,maxNights,minFreeSeats, travelId);
	}

	@Override
	public List<Map<String, Object>> findTravelReports(String sortColumn, String sortOrder,String fromDate, String toDate) {
		return travelDAO.findTravelReports(sortColumn, sortOrder, fromDate, toDate);
	}

	@Override
	public List<Travel> findAllByParamsForPassenger(String typeOfAccommodationUnit, String typeOfTravel, String typeOfVehicle, String destinationCity, String priceMin, String priceMax, String dateFrom, String dateTo, Integer minNights, Integer maxNights, Integer minFreeSeats, Long travelId) {
		return travelDAO.findAllByParamsForPassenger(typeOfAccommodationUnit, typeOfTravel, typeOfVehicle, destinationCity, priceMin, priceMax, dateFrom, dateTo, minNights, maxNights, minFreeSeats, travelId);
	}

	public int getLoyaltyPoints(Long userId) {
		String sql = "SELECT number_of_points FROM loyalty_card WHERE user_id = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			// Ako korisnik nema loyalty karticu, vrati 0 bodova
			return 0;
		}
	}


	public void updateLoyaltyPoints(Long userId, int newPoints) {
		String sql = "UPDATE loyalty_card SET number_of_points = ? WHERE user_id = ?";
		jdbcTemplate.update(sql, newPoints, userId);
	}

	@Override
	public List<Travel> findAllByType(TypeOfTravel typeOfTravel) {
		return travelDAO.findAllByType(typeOfTravel);
	}

	@Override
	public Double getPriceById(Long travelId) {
		String sql = "SELECT price FROM travels WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, Double.class, travelId);
	}



}
