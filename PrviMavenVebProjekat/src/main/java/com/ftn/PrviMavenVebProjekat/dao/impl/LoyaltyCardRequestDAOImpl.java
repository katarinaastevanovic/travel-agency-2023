package com.ftn.PrviMavenVebProjekat.dao.impl;

import com.ftn.PrviMavenVebProjekat.dao.LoyaltyCardRequestDAO;
import com.ftn.PrviMavenVebProjekat.model.LoyaltyCard;
import com.ftn.PrviMavenVebProjekat.model.LoyaltyCardRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class LoyaltyCardRequestDAOImpl implements LoyaltyCardRequestDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(LoyaltyCardRequest request) {
        String sql = "INSERT INTO loyalty_card_requests (user_id, first_name, last_name, email, request_date) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, request.getUserId(), request.getFirstName(), request.getLastName(), request.getEmail(), request.getRequestDate());
    }

    @Override
    public int update(LoyaltyCardRequest request) {
        String sql = "UPDATE loyalty_card_requests SET first_name = ?, last_name = ?, email = ?, request_date = ? WHERE id = ?";
        return jdbcTemplate.update(sql, request.getFirstName(), request.getLastName(), request.getEmail(), request.getRequestDate(), request.getId());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM loyalty_card_requests WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public LoyaltyCardRequest findById(Long id) {
        String sql = "SELECT * FROM loyalty_card_requests WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRow(rs), id);
        } catch (EmptyResultDataAccessException e) {
            return null; // Ako nije pronađen rezultat, vraća null
        }
    }

    @Override
    public List<LoyaltyCardRequest> findAll() {
        String sql = "SELECT * FROM loyalty_card_requests";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public List<Long> findUserIdsWithRequests() {
        String sql = "SELECT DISTINCT user_id FROM loyalty_card_requests";

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"));
    }

    @Override
    public LoyaltyCardRequest findByUserId(Long userId) {
        String sql = "SELECT * FROM loyalty_card_requests WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRow(rs), userId);
        } catch (EmptyResultDataAccessException e) {
            return null; // Ako nije pronađen rezultat, vraća null
        }
    }


    private LoyaltyCardRequest mapRow(ResultSet rs) throws SQLException {
        return new LoyaltyCardRequest(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getObject("request_date", LocalDateTime.class)
        );
    }


}
