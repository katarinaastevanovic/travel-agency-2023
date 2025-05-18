package com.ftn.PrviMavenVebProjekat.dao.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.PrviMavenVebProjekat.dao.UserDAO;
import com.ftn.PrviMavenVebProjekat.model.Role;
import com.ftn.PrviMavenVebProjekat.model.User;

@Repository
public class UserDAOImpl implements UserDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private class UserRowCallBackHandler implements RowCallbackHandler{
			
			private Map<Long, User> users = new LinkedHashMap<>();
			
			@Override
			public void processRow(ResultSet resultSet) throws SQLException {
				int index = 1;
				Long id = resultSet.getLong(index++);
				String username = resultSet.getString(index++);
				String password = resultSet.getString(index++);
				String name = resultSet.getString(index++);
				String lastName = resultSet.getString(index++);
				String email = resultSet.getString(index++);
				Date dateOfBirth = resultSet.getDate(index++);
				String address = resultSet.getString(index++);
				String number = resultSet.getString(index++);
			    LocalDateTime  dateOfReg = resultSet.getTimestamp(index++).toLocalDateTime();
				Role role = Role.valueOf(resultSet.getString(index++));
				boolean isBlocked = resultSet.getBoolean(index++);
		
				
		
				User user = users.get(id);
				if (user == null) {
					user = new User(id, username, password, name,lastName,email,
							dateOfBirth,address,number,dateOfReg,role,isBlocked);
					users.put(user.getId(), user); // dodavanje u kolekciju
				}
				else {
					boolean found = false;
					
				}
			}
			
			
			public List<User> getUsers() {
				return new ArrayList<>(users.values());
			}
		}
	
	@Override
	public User findOne(Long id) {
		String sql = " SELECT * from users d "
				+ " WHERE d.id=?"
				+ " ORDER BY d.id";

		UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getUsers().get(0);
	}
	@Override
	public User findOne(String email) {
		String sql = " SELECT * from users d "
				+ " WHERE d.email=?"
				+ " ORDER BY d.id";

		UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, email);

		List<User> users = rowCallbackHandler.getUsers();
	    return users.isEmpty() ? null : users.get(0);
	}

	@Override
	public User findOne(String email, String password) {
		String sql = " SELECT * from users d "
				+ " WHERE d.email=? AND d.sifra = ?" 
				+ " ORDER BY d.id";

		UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, email,password);

		
		if(rowCallbackHandler.getUsers().size() == 0) {
			return null;
		}
		
		return rowCallbackHandler.getUsers().get(0);
	}

	@Override
	public List<User> findAll() {
		
		String sql = "SELECT * FROM users d "
	    		+" ORDER BY d.id";

	    try {
	    	UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
	        jdbcTemplate.query(sql, rowCallbackHandler);

	        return rowCallbackHandler.getUsers();
	    } catch (Exception ex) {
	        ex.printStackTrace(); 
	        return Collections.emptyList(); 
	    }
	}

	@Override
	public List<User> findAll(String sortField, String sortOrder) {
		String sql = "SELECT * FROM users";
		if (sortField != null && !sortField.isEmpty()) {
			sql += " ORDER BY " + sortField;
			if (sortOrder != null && !sortOrder.isEmpty()) {
				sql += " " + sortOrder;
			}
		}
		return jdbcTemplate.query(sql, new UserRowMapper());
	}

	@Transactional
    @Override
    public int save(User korisnik) {
        String sql = "INSERT INTO users (username, sifra, ime,lastName,email,dateOfBirth,address,brojTelefona,dateOfReg,uloga,isBlocked) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        Object[] params = {
                korisnik.getUsername(),
                korisnik.getPassword(),
                korisnik.getName(),
                korisnik.getLastName(),
                korisnik.getEmail(),
                new java.sql.Date(korisnik.getDateOfBirth().getTime()),
                korisnik.getAddress(),
                korisnik.getNumber(),
                java.sql.Timestamp.valueOf(korisnik.getDateOfReg()),
                korisnik.getRole().toString(),
                korisnik.getIsBlocked(),
        };

        return jdbcTemplate.update(sql, params);
    }
	@Transactional
	@Override
	public int update(User user) {
		String sql = "UPDATE users SET username=?, sifra=?, ime=?,lastName=?,email=?,"
				+ "	dateOfBirth=?,address=?,brojTelefona=? WHERE id = ?";	
		boolean uspeh = jdbcTemplate.update(sql, user.getUsername(),user.getPassword(),user.getName(), 
				user.getLastName(),user.getEmail(),user.getDateOfBirth(),user.getAddress(),user.getNumber(),
				user.getId()) == 1;
		
		return uspeh?1:0;
	}

	@Transactional
	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM users WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}
	@Override
	public int block(Long id) {
		String sql = "UPDATE users SET isBlocked = 1 WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}
	@Override
	public int unblock(Long id) {
		String sql = "UPDATE users SET isBlocked = 0 WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}
	

	private static final class UserRowMapper implements RowMapper<User> {
	    @Override
	    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
	        Long id = resultSet.getLong("id");
	        String username = resultSet.getString("username");
	        String password = resultSet.getString("sifra");
	        String name = resultSet.getString("ime");
	        String lastName = resultSet.getString("lastName");
	        String email = resultSet.getString("email");
	        Date dateOfBirth = resultSet.getDate("dateOfBirth");
	        String address = resultSet.getString("address");
	        String number = resultSet.getString("brojTelefona");
	        LocalDateTime dateOfReg = resultSet.getTimestamp("dateOfReg").toLocalDateTime();
	        Role role = Role.valueOf(resultSet.getString("uloga"));
	        boolean isBlocked = resultSet.getBoolean("isBlocked");

	        return new User(id, username, password, name, lastName, email, dateOfBirth, address, number, dateOfReg, role, isBlocked);
	    }
	}
	@Override
	public boolean existsByEmail(String email) {
		String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, email);
		return count > 0;
	}

	@Override
	public List<User> findByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}

		// Kreiranje SQL upita sa IN klauzulom
		String sql = "SELECT * FROM users WHERE id IN (" + String.join(",", Collections.nCopies(ids.size(), "?")) + ")";

		// Upit za preuzimanje korisnika
		return jdbcTemplate.query(sql, ids.toArray(), new UserRowMapper());
	}

	@Override
	public List<User> findAllByRole(String role) {
		String sql = "SELECT * FROM users WHERE uloga = ?";
		return jdbcTemplate.query(sql, new Object[]{role}, new UserRowMapper());
	}
	@Override
	public List<User> searchByUsername(String username) {
		String sql = "SELECT * FROM users WHERE username LIKE ?";
		return jdbcTemplate.query(sql, new Object[]{"%" + username + "%"}, new UserRowMapper());
	}

	@Override
	public boolean existsByUsername(String username) {
		String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
		int count = jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class);
		return count > 0;
	}

	@Override
	public User findOneByUsername(String username) {
		String sql = "SELECT * FROM users WHERE username = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{username}, new UserRowMapper());
	}


}
