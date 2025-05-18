package com.ftn.PrviMavenVebProjekat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.ftn.PrviMavenVebProjekat.dao.DestinacijaDAO;
import com.ftn.PrviMavenVebProjekat.dao.VehicleDAO;
import com.ftn.PrviMavenVebProjekat.model.Destinacija;
import com.ftn.PrviMavenVebProjekat.model.Vehicle;

@Repository
public class VehicleDAOImpl implements VehicleDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DestinacijaDAO destinacijaDAO;
	
	private class VehicleRowCallBackHandler implements RowCallbackHandler{
		
		private Map<Long, Vehicle> vehicles = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			Integer numberOfSeats = resultSet.getInt(index++);
			Destinacija finalDestination = retrieveDestinacija(resultSet.getLong(index++));
			String descriptions = resultSet.getString(index++);
			String name = resultSet.getString(index++);
			boolean active = resultSet.getBoolean(index++); // Pretpostavljam da je ovo polje za 'active'

			Vehicle vehicle = vehicles.get(id);
			if (vehicle == null) {
				// Ako vozilo nije pronađeno, kreiramo novi objekat
				vehicle = new Vehicle(id, name, numberOfSeats, finalDestination, descriptions, active);
				vehicles.put(vehicle.getId(), vehicle); // Dodavanje u kolekciju
			} else {
				// Ako vozilo već postoji, ažuriramo njegove vrednosti
				vehicle.setName(name);
				vehicle.setNumberOfSeats(numberOfSeats);
				vehicle.setFinalDestination(finalDestination);
				vehicle.setDescriptions(descriptions);
				vehicle.setActive(active);
			}
		}

	private Destinacija retrieveDestinacija(Long destinacijaId) {
        // Use the injected DestinacijaDAO
        return destinacijaDAO.findOne(destinacijaId);
    }
	
	
	public List<Vehicle> getVehicles() {
		return new ArrayList<>(vehicles.values());
	}
}




	@Override
	public int save(Vehicle vehicle) {
		String sql = "INSERT INTO vehicles (numberOfSeats,finalDestinationID,descriptions,name) "
                + "VALUES (?, ?, ?, ?)";

        Object[] params = {

        		vehicle.getNumberOfSeats(),
        		vehicle.getFinalDestination().getId(),
        		vehicle.getDescriptions(),
				vehicle.getName()
                
        };

        return jdbcTemplate.update(sql, params);
	}

	@Override
	public int update(Vehicle vehicle) {
		String sql = "UPDATE vehicles SET numberOfSeats = ?, finalDestinationID = ?, descriptions = ?, name = ? WHERE id = ?";
		boolean uspeh = jdbcTemplate.update(sql, vehicle.getNumberOfSeats(), vehicle.getFinalDestination().getId(), vehicle.getDescriptions(), vehicle.getName(), vehicle.getId()) == 1;

		return uspeh ? 1 : 0;
	}


	@Override
	public int delete(Long id) {
		String sql = "UPDATE vehicles SET active = false WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public Vehicle findOne(Long id) {
		String sql = "SELECT * FROM vehicles WHERE id = ? AND active = true";
		VehicleRowCallBackHandler rowCallbackHandler = new VehicleRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);
		List<Vehicle> vehicles = rowCallbackHandler.getVehicles();
		return vehicles.isEmpty() ? null : vehicles.get(0);
	}


	@Override
	public List<Vehicle> findAll() {
		String sql = "SELECT * FROM vehicles WHERE active = true ORDER BY id";
		try {
			VehicleRowCallBackHandler rowCallbackHandler = new VehicleRowCallBackHandler();
			jdbcTemplate.query(sql, rowCallbackHandler);
			return rowCallbackHandler.getVehicles();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public List<Vehicle> findByDestinacijaId(Long destinacijaId) {
		String sql = "SELECT * FROM vehicles WHERE finalDestinationID = ? AND active = true ORDER BY id";

		try {
			VehicleDAOImpl.VehicleRowCallBackHandler rowCallbackHandler = new VehicleDAOImpl.VehicleRowCallBackHandler();
			jdbcTemplate.query(sql, rowCallbackHandler, destinacijaId);

			return rowCallbackHandler.getVehicles();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Collections.emptyList();
		}
	}

}
