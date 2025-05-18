package com.ftn.PrviMavenVebProjekat.service;

import java.util.List;
import java.util.Map;

import com.ftn.PrviMavenVebProjekat.model.Travel;
import com.ftn.PrviMavenVebProjekat.model.TypeOfAccommodationUnit;
import com.ftn.PrviMavenVebProjekat.model.TypeOfTravel;

public interface TravelService {
	Travel findOne(Long id);
	List <Travel> findAll();
	void save(Travel travel);

	int getAvailableSeats(Long travelDateId);

	int getAvailableBeds(Long travelDateId);

	List<Travel> findAllByParams(String typeOfAccommodationUnit, String typeOfTravel, String typeOfVehicle, String destinationCity, String priceMin, String priceMax, String dateFrom, String dateTo,Integer minNights, Integer maxNights,Integer minFreeSeats, Long travelId);


	List<Map<String, Object>> findTravelReports(String sortColumn,String sortOrder,String fromDate,String toDate);

	List<Travel> findAllByParamsForPassenger(String typeOfAccommodationUnit, String typeOfTravel, String typeOfVehicle, String destinationCity, String priceMin, String priceMax, String dateFrom, String dateTo, Integer minNights, Integer maxNights, Integer minFreeSeats, Long travelId);

    int getLoyaltyPoints(Long id);

    void updateLoyaltyPoints(Long id, int i);

    List<Travel> findAllByType(TypeOfTravel typeOfTravel);

    Double getPriceById(Long travelId);
}
