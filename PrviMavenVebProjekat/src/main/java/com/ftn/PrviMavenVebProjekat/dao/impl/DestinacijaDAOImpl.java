package com.ftn.PrviMavenVebProjekat.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.PrviMavenVebProjekat.dao.DestinacijaDAO;
import com.ftn.PrviMavenVebProjekat.model.Destinacija;

@Repository
public class DestinacijaDAOImpl implements DestinacijaDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private class DestinacijaRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, Destinacija> destinacije = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			String grad = resultSet.getString(index++);
			String drzava = resultSet.getString(index++);
			String kontinent = resultSet.getString(index++);
			String slika = resultSet.getString(index++);
			boolean aktivnost = resultSet.getBoolean(index++);

			Destinacija destinacija = destinacije.get(id);
			if (destinacija == null) {
				destinacija = new Destinacija(id, grad, drzava, kontinent, slika);
				destinacija.setAktivnost(aktivnost); // postavljanje polja aktivnost
				destinacije.put(destinacija.getId(), destinacija);
			}
		}

		public List<Destinacija> getDestinacije() {
			return new ArrayList<>(destinacije.values());
		}
	}

	@Override
	public Destinacija findOne(Long id) {
		String sql = "SELECT * FROM destinacije WHERE id = ? AND aktivnost = true ORDER BY id";
		DestinacijaRowCallBackHandler rowCallbackHandler = new DestinacijaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		List<Destinacija> destinacije = rowCallbackHandler.getDestinacije();
		return destinacije.isEmpty() ? null : destinacije.get(0);
	}

	@Override
	public List<Destinacija> findAll() {
		String sql = "SELECT * FROM destinacije WHERE aktivnost = true ORDER BY id";
		try {
			DestinacijaRowCallBackHandler rowCallbackHandler = new DestinacijaRowCallBackHandler();
			jdbcTemplate.query(sql, rowCallbackHandler);
			return rowCallbackHandler.getDestinacije();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error fetching destinacije from database", ex);
		}
	}

	@Transactional
	@Override
	public int save(Destinacija destinacija) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO destinacije (grad, drzava, kontinent, slika, aktivnost) VALUES (?, ?, ?, ?, true)";
				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setString(index++, destinacija.getGrad());
				preparedStatement.setString(index++, destinacija.getDrzava());
				preparedStatement.setString(index++, destinacija.getKontinent());
				preparedStatement.setString(index++, destinacija.getSlika());
				return preparedStatement;
			}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return uspeh ? 1 : 0;
	}

	@Transactional
	@Override
	public int update(Destinacija destinacija) {
		String sql = "UPDATE destinacije SET grad = ?, drzava = ?, kontinent = ?, slika = ?, aktivnost = ? WHERE id = ?";
		boolean uspeh = jdbcTemplate.update(sql, destinacija.getGrad(), destinacija.getDrzava(), destinacija.getKontinent(), destinacija.getSlika(), destinacija.getAktivnost(), destinacija.getId()) == 1;
		return uspeh ? 1 : 0;
	}

	@Transactional
	@Override
	public int delete(Long id) {
		String sql = "UPDATE destinacije SET aktivnost = false WHERE id = ?";
		int rowsAffected = jdbcTemplate.update(sql, id);
		System.out.println("Rows affected: " + rowsAffected); // Dodavanje loga
		return rowsAffected;
	}
}
