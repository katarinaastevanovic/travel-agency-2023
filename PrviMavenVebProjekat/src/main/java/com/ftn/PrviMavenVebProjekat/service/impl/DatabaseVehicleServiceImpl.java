package com.ftn.PrviMavenVebProjekat.service.impl;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.model.AccUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.PrviMavenVebProjekat.dao.VehicleDAO;
import com.ftn.PrviMavenVebProjekat.model.Vehicle;
import com.ftn.PrviMavenVebProjekat.service.VehicleService;

@Service
public class DatabaseVehicleServiceImpl implements VehicleService{
	
	@Autowired
	private VehicleDAO vehicleDAO;

	@Override
	public Vehicle findOne(Long id) {
		return vehicleDAO.findOne(id);
	}

	@Override
	public List<Vehicle> findAll() {
		return vehicleDAO.findAll();
	}

	@Override
	public Vehicle save(Vehicle vehicle) {
		vehicleDAO.save(vehicle);
		return vehicle;
	}

	@Override
	public Vehicle update(Vehicle vehicle) {
		vehicleDAO.update(vehicle);
		return vehicle;
	}

	@Override
	public void delete(Long id) {
		vehicleDAO.delete(id);
	}

	@Override
	public List<Vehicle> findByDestinacijaId(Long destinacijaId) {
		return vehicleDAO.findByDestinacijaId(destinacijaId);
	}
}
