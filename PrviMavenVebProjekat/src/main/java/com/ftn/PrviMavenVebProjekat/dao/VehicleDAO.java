package com.ftn.PrviMavenVebProjekat.dao;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.model.Vehicle;

public interface VehicleDAO {
	Vehicle findOne(Long id);
	List <Vehicle> findAll();
	int save(Vehicle vehicle);
	int update(Vehicle vehicle);
	int delete(Long id);


	List<Vehicle> findByDestinacijaId(Long destinacijaId);
}
