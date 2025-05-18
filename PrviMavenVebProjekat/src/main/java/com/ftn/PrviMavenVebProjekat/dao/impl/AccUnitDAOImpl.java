
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
import org.springframework.transaction.annotation.Transactional;

import com.ftn.PrviMavenVebProjekat.dao.AccUnitDAO;
import com.ftn.PrviMavenVebProjekat.dao.DestinacijaDAO;
import com.ftn.PrviMavenVebProjekat.model.AccUnit;
import com.ftn.PrviMavenVebProjekat.model.Destinacija;
import com.ftn.PrviMavenVebProjekat.model.TypeOfAccommodationUnit;
import com.ftn.PrviMavenVebProjekat.model.Vehicle;

@Repository
public class AccUnitDAOImpl implements AccUnitDAO{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DestinacijaDAO destinacijaDAO;
	
	private class AccUnitRowCallBackHandler implements RowCallbackHandler{

		
		private Map<Long, AccUnit> accUnits = new LinkedHashMap<>();
		
		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
		    int index = 1;
		    Long id = resultSet.getLong(index++);
		    String namee = resultSet.getString(index++);
		    String typeOfAccommodationUnitStr = resultSet.getString(index++);
		    TypeOfAccommodationUnit typeOfAccommodationUnit = null;

		    try {
		        typeOfAccommodationUnit = TypeOfAccommodationUnit.valueOf(typeOfAccommodationUnitStr);
		    } catch (IllegalArgumentException e) {
		        e.printStackTrace();
		    }
		    Integer capacity = resultSet.getInt(index++);
		    Destinacija destinacija = retrieveDestinacija(resultSet.getLong(index++));
		    String services = resultSet.getString(index++);
		    String description = resultSet.getString(index++);
			boolean activity = resultSet.getBoolean(index++);

			AccUnit accUnit = accUnits.get(id);
			if (accUnit == null) {
				accUnit = new AccUnit(id, namee, typeOfAccommodationUnit, capacity, destinacija, services, description);
				accUnit.setActive(activity); // postavljanje polja aktivnost
				accUnits.put(accUnit.getId(), accUnit);
			}

		}
		private Destinacija retrieveDestinacija(Long destinacijaId) {
            // Use the injected DestinacijaDAO
            return destinacijaDAO.findOne(destinacijaId);
        }

		public List<AccUnit> getAccUnits() {
		    return new ArrayList<>(accUnits.values());
		}

	}

	@Override
	public AccUnit findOne(Long id) {
		String sql = "SELECT * FROM accunits WHERE id = ? AND active = true";
		AccUnitRowCallBackHandler rowCallbackHandler = new AccUnitRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);
		List<AccUnit> accUnits = rowCallbackHandler.getAccUnits();
		return accUnits.isEmpty() ? null : accUnits.get(0);
	}

	@Override
	public List<AccUnit> findAll() {
		String sql = "SELECT * FROM accUnits WHERE active = true ORDER BY id";

		try {
			AccUnitRowCallBackHandler rowCallbackHandler = new AccUnitRowCallBackHandler();
			jdbcTemplate.query(sql, rowCallbackHandler);

			return rowCallbackHandler.getAccUnits();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Transactional
	@Override
	public int save(AccUnit accUnit) {
		String sql = "INSERT INTO accUnits (namee, typeOfAccommodationUnit, capacity, destinacijaId, services, descriptions, active) VALUES (?, ?, ?, ?, ?, ?, ?)";

		Object[] params = {
				accUnit.getNamee(),
				accUnit.getTypeOfAccommodationUnit().name(),
				accUnit.getCapacity(),
				accUnit.getDestinacija().getId(),
				accUnit.getServices(),
				accUnit.getDescription(),
				accUnit.isActive()
		};

		return jdbcTemplate.update(sql, params);
	}

	@Transactional
	@Override
	public int update(AccUnit accUnit) {
		String sql = "UPDATE accUnits SET namee = ?, typeOfAccommodationUnit = ?, capacity = ?, destinacijaId = ?, services = ?, descriptions = ?, active = ? WHERE id = ?";

		return jdbcTemplate.update(sql,
				accUnit.getNamee(),
				accUnit.getTypeOfAccommodationUnit().name(),
				accUnit.getCapacity(),
				accUnit.getDestinacija().getId(),
				accUnit.getServices(),
				accUnit.getDescription(),
				accUnit.isActive(),
				accUnit.getId());
	}
	@Transactional
	@Override
	public int delete(Long id) {
		String sql = "UPDATE accUnits SET active = false WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public List<AccUnit> findByDestinacijaId(Long destinacijaId) {
		String sql = "SELECT * FROM accUnits WHERE destinacijaId = ? AND active = true ORDER BY id";

		try {
			AccUnitRowCallBackHandler rowCallbackHandler = new AccUnitRowCallBackHandler();
			jdbcTemplate.query(sql, rowCallbackHandler, destinacijaId);

			return rowCallbackHandler.getAccUnits();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Collections.emptyList();
		}
	}
}