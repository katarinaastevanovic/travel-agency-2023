package com.ftn.PrviMavenVebProjekat.service;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.model.Vehicle;

public interface VehicleService {
	Vehicle findOne(Long id);
	List <Vehicle> findAll();
	Vehicle save(Vehicle vehicle);
	Vehicle update(Vehicle vehicle);
	void delete(Long id);

    List<Vehicle> findByDestinacijaId(Long destinacijaId);
}
