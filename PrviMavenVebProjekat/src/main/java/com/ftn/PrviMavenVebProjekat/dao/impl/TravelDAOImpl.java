package com.ftn.PrviMavenVebProjekat.dao.impl;
import com.ftn.PrviMavenVebProjekat.dao.AccUnitDAO;
import com.ftn.PrviMavenVebProjekat.dao.DestinacijaDAO;
import com.ftn.PrviMavenVebProjekat.dao.TravelDAO;
import com.ftn.PrviMavenVebProjekat.dao.VehicleDAO;
import com.ftn.PrviMavenVebProjekat.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class TravelDAOImpl implements TravelDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DestinacijaDAO destinacijaDAO;

	@Autowired
	private VehicleDAO vehicleDAO;

	@Autowired
	private AccUnitDAO accUnitDAO;

	@Autowired
	private TravelDateDAOImpl travelDateDAO; // Dodaj DAO za TravelDate

	private static final Logger logger = LoggerFactory.getLogger(TravelDAOImpl.class);

	private class TravelRowMapper implements RowMapper<Travel> {
		@Override
		public Travel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			Long destinacijaId = resultSet.getLong(index++);
			Destinacija destinacija = destinacijaDAO.findOne(destinacijaId);

			Long idVehicle = resultSet.getLong(index++);
			Long idAccUnit = resultSet.getLong(index++);

			Vehicle vehicle = vehicleDAO.findOne(idVehicle);
			AccUnit accUnit = accUnitDAO.findOne(idAccUnit);

			if (vehicle == null || !vehicle.isActive() || accUnit == null || !accUnit.isActive()) {
				logger.error("Vehicle or AccUnit not found or inactive for Travel ID: " + id);
				return null;  // Preskoči ovu liniju, kasnije će se ignorisati
			}

			TypeOfTravel typeOfTravel = TypeOfTravel.valueOf(resultSet.getString(index++));
			Double price = resultSet.getDouble(index++);

			Travel travel = new Travel(id, destinacija, vehicle, accUnit, typeOfTravel, price);

			// Učitaj povezane TravelDate zapise
			List<TravelDate> travelDates = travelDateDAO.findByTravelId(id);
			travel.setTravelDates(travelDates);

			logger.info("Travel created: ID={}, Destinacija={}, Vehicle={}, AccUnit={}", id, destinacija.getGrad(), vehicle.getName(), accUnit.getNamee());
			return travel;
		}
	}


	private class TravelRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, Travel> travels = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			Long destinacijaId = resultSet.getLong(index++);
			Destinacija destinacija = destinacijaDAO.findOne(destinacijaId);

			Long idVehicle = resultSet.getLong(index++);
			Long idAccUnit = resultSet.getLong(index++);

			Vehicle vehicle = vehicleDAO.findOne(idVehicle);
			AccUnit accUnit = accUnitDAO.findOne(idAccUnit);

			TypeOfTravel typeOfTravel = TypeOfTravel.valueOf(resultSet.getString(index++));

			Double price = resultSet.getDouble(index++);

			Travel travel = travels.get(id);
			if (travel == null) {
				travel = new Travel(id, destinacija, vehicle, accUnit, typeOfTravel, price);
				travels.put(travel.getId(), travel); // dodavanje u kolekciju
			}

			// Učitaj povezane TravelDate zapise
			List<TravelDate> travelDates = travelDateDAO.findByTravelId(id);
			travel.setTravelDates(travelDates);
		}

		public List<Travel> getTravels() {
			return new ArrayList<>(travels.values());
		}
	}

	@Override
	public Travel findOne(Long id) {
		String sql = "SELECT * from travels d WHERE d.id=? ORDER BY d.id";

		TravelRowCallBackHandler rowCallbackHandler = new TravelRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getTravels().get(0);
	}

	@Override
	public List<Travel> findAll() {
		String sql = "SELECT t.* FROM travels t " +
				"JOIN destinacije d ON t.destinacijaId = d.id " +
				"JOIN vehicles v ON t.vehicleId = v.id " +
				"JOIN accunits a ON t.accUnitId = a.id " +
				"WHERE d.aktivnost = true AND v.active = true AND a.active = true " +
				"ORDER BY t.id";

		try {
			TravelRowCallBackHandler rowCallbackHandler = new TravelRowCallBackHandler();
			jdbcTemplate.query(sql, rowCallbackHandler);

			return rowCallbackHandler.getTravels();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Collections.emptyList();
		}
	}


	@Override
	@Transactional
	public void save(Travel travel) {
		String sql = "INSERT INTO travels (destinacijaId, vehicleId, accUnitId, typeOfTravel, price) " +
				"VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, travel.getDestinacija().getId(), travel.getVehicle().getId(), travel.getAccUnit().getId(),
				travel.getTypeOfTravel().toString(), travel.getPrice());

		Long travelId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
		travel.setId(travelId); // Postavljanje ID-a za travel objekat

		// Čuvanje povezanih TravelDate zapisa
		for (TravelDate travelDate : travel.getTravelDates()) {
			travelDate.setTravelId(travelId); // Postavljanje ID putovanja
			travelDateDAO.save(travelDate);
		}
	}

	// Dodaj save metodu za TravelDate
	@Transactional
	public void save(TravelDate travelDate) {
		String sql = "INSERT INTO travel_dates (travel_id, arrival_date, departure_date, number_of_nights, number_of_free_seats) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, travelDate.getTravelId(), travelDate.getArrivalDate(), travelDate.getDepartureDate(), travelDate.getNumberOfNights(), travelDate.getNumberOfFreeSeats());
	}


	public List<TravelDate> findByTravelId(Long travelId) {
		String sql = "SELECT * FROM travel_dates WHERE travel_id = ?";
		return jdbcTemplate.query(sql, new RowMapper<TravelDate>() {
			@Override
			public TravelDate mapRow(ResultSet rs, int rowNum) throws SQLException {
				TravelDate travelDate = new TravelDate();
				travelDate.setId(rs.getLong("id"));
				travelDate.setTravelId(rs.getLong("travel_id"));
				travelDate.setArrivalDate(rs.getTimestamp("arrival_date").toLocalDateTime());
				travelDate.setDepartureDate(rs.getTimestamp("departure_date").toLocalDateTime());
				travelDate.setNumberOfNights(rs.getInt("number_of_nights"));
				travelDate.setNumberOfFreeSeats(rs.getInt("number_of_free_seats")); // Mapiranje nove kolone
				return travelDate;
			}
		}, travelId);
	}

	@Override
	public List<Travel> findAllByParams(String typeOfAccommodationUnit, String typeOfTravel, String typeOfVehicle, String destinationCity, String priceMin, String priceMax, String dateFrom, String dateTo, Integer minNights, Integer maxNights, Integer minFreeSeats, Long travelId) {
		String sql = "SELECT DISTINCT t.* FROM travels t " +
				"JOIN accunits a ON t.accUnitId = a.id " +
				"JOIN vehicles v ON t.vehicleId = v.id " +
				"JOIN destinacije d ON t.destinacijaId = d.id " +
				"JOIN travel_dates td ON t.id = td.travel_id " +
				"WHERE a.active = true AND v.active = true AND d.aktivnost = true";

		List<Object> params = new ArrayList<>();

		if (travelId != null) {
			sql += " AND t.id = ?";
			params.add(travelId);
		}

		if (typeOfAccommodationUnit != null && !typeOfAccommodationUnit.isEmpty()) {
			sql += " AND a.typeOfAccommodationUnit = ?";
			params.add(typeOfAccommodationUnit);
		}

		if (typeOfTravel != null && !typeOfTravel.isEmpty()) {
			sql += " AND t.typeOfTravel = ?";
			params.add(typeOfTravel);
		}

		if (typeOfVehicle != null && !typeOfVehicle.isEmpty()) {
			sql += " AND v.name = ?";
			params.add(typeOfVehicle);
		}

		if (destinationCity != null && !destinationCity.isEmpty()) {
			sql += " AND d.grad LIKE ?";
			params.add("%" + destinationCity + "%");
		}

		if (priceMin != null && !priceMin.isEmpty()) {
			sql += " AND t.price >= ?";
			params.add(Double.parseDouble(priceMin));
		}

		if (priceMax != null && !priceMax.isEmpty()) {
			sql += " AND t.price <= ?";
			params.add(Double.parseDouble(priceMax));
		}

		if (dateFrom != null && !dateFrom.isEmpty()) {
			sql += " AND td.arrival_date >= ?";
			params.add(Date.valueOf(dateFrom));
		}

		if (dateTo != null && !dateTo.isEmpty()) {
			sql += " AND td.departure_date <= ?";
			params.add(Date.valueOf(dateTo));
		}

		if (minNights != null) {
			sql += " AND td.number_of_nights >= ?";
			params.add(minNights);
		}

		if (maxNights != null) {
			sql += " AND td.number_of_nights <= ?";
			params.add(maxNights);
		}

		if (minFreeSeats != null) {
			sql += " AND td.number_of_free_seats >= ?";
			params.add(minFreeSeats);
		}

		Set<Travel> travelSet = new LinkedHashSet<>(jdbcTemplate.query(sql, params.toArray(), new TravelRowMapper()));
		return new ArrayList<>(travelSet);
	}

	@Override
	public int findRemainingSeatsByTravelDateId(Long travelDateId) {
		String sql = "SELECT number_of_free_seats FROM travel_dates WHERE id = ?";
		Integer result = jdbcTemplate.queryForObject(sql, new Object[]{travelDateId}, Integer.class);
		if (result == null) {
			throw new IllegalArgumentException("Invalid travelDateId: " + travelDateId);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> findTravelReports() {
		String sql = "SELECT DISTINCT r.travel_id, d.grad, d.id as destId, v.name as vehicleName, v.numberOfSeats as nos, " +
				"(v.numberOfSeats - td.number_of_free_seats) as soldSeats, " +
				"(t.price * (v.numberOfSeats - td.number_of_free_seats)) as totalPrice " +
				"FROM reservations r " +
				"JOIN travels t ON r.travel_id = t.id " +
				"JOIN destinacije d ON t.destinacijaId = d.id " +
				"JOIN vehicles v ON t.vehicleId = v.id " +
				"JOIN travel_dates td ON r.travel_date_id = td.id " +
				"WHERE r.status = 'APPROVED'";

		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Map<String, Object> result = new HashMap<>();
			result.put("travelId", rs.getLong("travel_id"));
			result.put("grad", rs.getString("grad"));
			result.put("destId", rs.getLong("destId"));
			result.put("vehicleName", rs.getString("vehicleName"));
			result.put("nos", rs.getInt("nos"));
			result.put("soldSeats", rs.getInt("soldSeats"));
			result.put("totalPrice", rs.getBigDecimal("totalPrice")); // Izračunata ukupna cena
			return result;
		});
	}

	@Override
	public List<Map<String, Object>> findTravelReports(String sortColumn, String sortOrder, String fromDate, String toDate) {
		List<String> allowedSortColumns = Arrays.asList("travel_id", "grad", "vehicleName", "nos", "soldSeats", "totalPrice");
		if (sortColumn == null || !allowedSortColumns.contains(sortColumn)) {
			sortColumn = "travel_id"; // Defaultno sortiranje po Travel ID
		}

		if (sortOrder == null || (!sortOrder.equals("asc") && !sortOrder.equals("desc"))) {
			sortOrder = "asc"; // Defaultno je rastući redosled
		}

		String sql = "SELECT DISTINCT r.travel_id, d.grad, d.id as destId, v.name as vehicleName, v.numberOfSeats as nos, " +
				"(v.numberOfSeats - td.number_of_free_seats) as soldSeats, " +
				"(t.price * (v.numberOfSeats - td.number_of_free_seats)) as totalPrice " +
				"FROM reservations r " +
				"JOIN travels t ON r.travel_id = t.id " +
				"JOIN destinacije d ON t.destinacijaId = d.id " +
				"JOIN vehicles v ON t.vehicleId = v.id " +
				"JOIN travel_dates td ON r.travel_date_id = td.id " +
				"WHERE r.status = 'APPROVED' ";

		List<Object> params = new ArrayList<>();

		if (fromDate != null && !fromDate.isEmpty()) {
			sql += " AND r.departure_date >= ? ";
			params.add(Date.valueOf(fromDate));
		}

		if (toDate != null && !toDate.isEmpty()) {
			sql += " AND r.return_date <= ? ";
			params.add(Date.valueOf(toDate));
		}

		sql += "ORDER BY " + sortColumn + " " + sortOrder;

		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Map<String, Object> result = new HashMap<>();
			result.put("travelId", rs.getLong("travel_id"));
			result.put("grad", rs.getString("grad"));
			result.put("destId", rs.getLong("destId"));
			result.put("vehicleName", rs.getString("vehicleName"));
			result.put("nos", rs.getInt("nos"));
			result.put("soldSeats", rs.getInt("soldSeats"));
			result.put("totalPrice", rs.getBigDecimal("totalPrice"));
			return result;
		}, params.toArray());
	}

	@Override
	public List<Travel> findAllByParamsForPassenger(String typeOfAccommodationUnit, String typeOfTravel, String typeOfVehicle, String destinationCity, String priceMin, String priceMax, String dateFrom, String dateTo, Integer minNights, Integer maxNights, Integer minFreeSeats, Long travelId) {
		String sql = "SELECT DISTINCT t.*, td.arrival_date, td.departure_date, td.number_of_nights, td.number_of_free_seats, td.number_of_free_beds FROM travels t " +
				"JOIN accunits a ON t.accUnitId = a.id " +
				"JOIN vehicles v ON t.vehicleId = v.id " +
				"JOIN destinacije d ON t.destinacijaId = d.id " +
				"JOIN travel_dates td ON t.id = td.travel_id " +
				"WHERE a.active = true AND v.active = true AND d.aktivnost = true " +
				"AND td.number_of_free_seats > 0 " +
				"AND td.number_of_free_beds > 0";

		List<Object> params = new ArrayList<>();

		if (travelId != null) {
			sql += " AND t.id = ?";
			params.add(travelId);
		}

		if (typeOfAccommodationUnit != null && !typeOfAccommodationUnit.isEmpty()) {
			sql += " AND a.typeOfAccommodationUnit = ?";
			params.add(typeOfAccommodationUnit);
		}

		if (typeOfTravel != null && !typeOfTravel.isEmpty()) {
			sql += " AND t.typeOfTravel = ?";
			params.add(typeOfTravel);
		}

		if (typeOfVehicle != null && !typeOfVehicle.isEmpty()) {
			sql += " AND v.name = ?";
			params.add(typeOfVehicle);
		}

		if (destinationCity != null && !destinationCity.isEmpty()) {
			sql += " AND d.grad LIKE ?";
			params.add("%" + destinationCity + "%");
		}

		if (priceMin != null && !priceMin.isEmpty()) {
			sql += " AND t.price >= ?";
			params.add(Double.parseDouble(priceMin));
		}

		if (priceMax != null && !priceMax.isEmpty()) {
			sql += " AND t.price <= ?";
			params.add(Double.parseDouble(priceMax));
		}

		if (dateFrom != null && !dateFrom.isEmpty()) {
			sql += " AND td.arrival_date >= ?";
			params.add(Date.valueOf(dateFrom));
		}

		if (dateTo != null && !dateTo.isEmpty()) {
			sql += " AND td.departure_date <= ?";
			params.add(Date.valueOf(dateTo));
		}

		if (minNights != null) {
			sql += " AND td.number_of_nights >= ?";
			params.add(minNights);
		}

		if (maxNights != null) {
			sql += " AND td.number_of_nights <= ?";
			params.add(maxNights);
		}

		if (minFreeSeats != null) {
			sql += " AND td.number_of_free_seats >= ?";
			params.add(minFreeSeats);
		}

		Set<Travel> travelSet = new LinkedHashSet<>(jdbcTemplate.query(sql, params.toArray(), new TravelRowMapper()));
		return new ArrayList<>(travelSet);
	}

	@Override
	public int findAvailableSeatsByTravelDateId(Long travelDateId) {
		String sql = "SELECT number_of_free_seats FROM travel_dates WHERE id = ?";
		Integer result = jdbcTemplate.queryForObject(sql, new Object[]{travelDateId}, Integer.class);
		if (result == null) {
			throw new IllegalArgumentException("Invalid travelDateId: " + travelDateId);
		}
		return result;
	}

	@Override
	public int findAvailableBedsByTravelDateId(Long travelDateId) {
		String sql = "SELECT number_of_free_beds FROM travel_dates WHERE id = ?";
		Integer result = jdbcTemplate.queryForObject(sql, new Object[]{travelDateId}, Integer.class);
		if (result == null) {
			throw new IllegalArgumentException("Invalid travelDateId: " + travelDateId);
		}
		return result;
	}

	@Override
	public List<Travel> findAllByType(TypeOfTravel typeOfTravel) {
		String sql = "SELECT * FROM travels WHERE typeOfTravel = ?";
		return jdbcTemplate.query(sql, new Object[]{typeOfTravel.toString()}, new TravelRowMapper());
	}




}
