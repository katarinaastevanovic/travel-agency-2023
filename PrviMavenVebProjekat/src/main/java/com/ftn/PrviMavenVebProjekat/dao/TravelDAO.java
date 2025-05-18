package com.ftn.PrviMavenVebProjekat.dao;

import java.util.List;
import java.util.Map;

import com.ftn.PrviMavenVebProjekat.model.Travel;
import com.ftn.PrviMavenVebProjekat.model.TypeOfAccommodationUnit;
import com.ftn.PrviMavenVebProjekat.model.TypeOfTravel;

public interface TravelDAO {
	Travel findOne(Long id);
	List<Travel>findAll();

	void save(Travel travel);

	List<Travel> findAllByParams(String typeOfAccommodationUnit, String typeOfTravel, String typeOfVehicle, String destinationCity, String priceMin, String priceMax, String dateFrom, String dateTo, Integer minNights, Integer maxNights, Integer minFreeSeats, Long travelId);

	int findRemainingSeatsByTravelDateId(Long travelDateId);

	List<Map<String, Object>> findTravelReports();

    List<Map<String, Object>> findTravelReports(String sortColumn, String sortOrder, String fromDate, String toDate);

	List<Travel> findAllByParamsForPassenger(String typeOfAccommodationUnit, String typeOfTravel, String typeOfVehicle, String destinationCity, String priceMin, String priceMax, String dateFrom, String dateTo, Integer minNights, Integer maxNights, Integer minFreeSeats, Long travelId);

	int findAvailableSeatsByTravelDateId(Long travelDateId);

	int findAvailableBedsByTravelDateId(Long travelDateId);

    List<Travel> findAllByType(TypeOfTravel typeOfTravel);
}
