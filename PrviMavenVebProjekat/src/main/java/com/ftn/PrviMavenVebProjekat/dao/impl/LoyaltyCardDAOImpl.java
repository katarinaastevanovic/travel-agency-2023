package com.ftn.PrviMavenVebProjekat.dao.impl;

import com.ftn.PrviMavenVebProjekat.dao.LoyaltyCardDAO;
import com.ftn.PrviMavenVebProjekat.model.LoyaltyCard;
import com.ftn.PrviMavenVebProjekat.model.LoyaltyCardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LoyaltyCardDAOImpl implements LoyaltyCardDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class LoyaltyCardRowCallbackHandler implements RowCallbackHandler {

        private Map<Long, LoyaltyCard> loyaltyCards = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Long id = resultSet.getLong(index++);
            Long userId = resultSet.getLong(index++);
            int discount = resultSet.getInt(index++);
            int numberOfPoints = resultSet.getInt(index++);

            LoyaltyCard loyaltyCard = loyaltyCards.get(id);
            if (loyaltyCard == null) {
                loyaltyCard = new LoyaltyCard(id, userId, discount, numberOfPoints);
                loyaltyCards.put(loyaltyCard.getId(), loyaltyCard);
            }
        }

        public LoyaltyCard getLoyaltyCard() {
            return loyaltyCards.values().stream().findFirst().orElse(null);
        }
    }

    @Override
    public LoyaltyCard findByUserId(Long userId) {
        String sql = "SELECT * FROM loyalty_card WHERE user_id = ?";
        LoyaltyCardRowCallbackHandler rowCallbackHandler = new LoyaltyCardRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, userId);

        return rowCallbackHandler.getLoyaltyCard();
    }

    @Transactional
    @Override
    public void save(LoyaltyCard loyaltyCard) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO loyalty_card (user_id, discount, number_of_points) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setLong(index++, loyaltyCard.getUserId());
                preparedStatement.setInt(index++, loyaltyCard.getDiscount());
                preparedStatement.setInt(index++, loyaltyCard.getNumberOfPoints());
                return preparedStatement;
            }
        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        loyaltyCard.setId(keyHolder.getKey().longValue());
    }

    @Override
    public LoyaltyCard findById(Long id) {
        String sql = "SELECT * FROM loyalty_card WHERE id = ?";
        List<LoyaltyCard> cards = jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs), id);
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(0);
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM loyalty_card WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<LoyaltyCard> findAll() {
        String sql = "SELECT * FROM loyalty_card";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    private LoyaltyCard mapRow(ResultSet rs) throws SQLException {
        return new LoyaltyCard(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getInt("discount"),
                rs.getInt("number_of_points")
        );
    }
}
