package com.ftn.PrviMavenVebProjekat.dao.impl;

import com.ftn.PrviMavenVebProjekat.dao.WishListDAO;
import com.ftn.PrviMavenVebProjekat.model.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WishListDAOImpl implements WishListDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class WishListRowMapper implements RowMapper<WishList> {
        @Override
        public WishList mapRow(ResultSet rs, int rowNum) throws SQLException {
            WishList wishList = new WishList();
            wishList.setId(rs.getLong("id"));
            wishList.setUserId(rs.getLong("user_id"));
            wishList.setTravelId(rs.getLong("travel_id"));
            wishList.setTravelDateId(rs.getLong("travel_date_id"));
            wishList.setNumberOfPassengers(rs.getInt("number_of_passengers"));
            wishList.setTotalPrice(rs.getDouble("total_price"));
            return wishList;
        }
    }

    @Override
    public List<WishList> findByUserId(Long userId) {
        String sql = "SELECT * FROM wish_list WHERE user_id = ?";
        return jdbcTemplate.query(sql, new WishListRowMapper(), userId);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM wish_list WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    @Override
    public void save(WishList wishList) {
        String sql = "INSERT INTO wish_list (user_id, travel_id, travel_date_id, number_of_passengers, total_price) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, wishList.getUserId(), wishList.getTravelId(), wishList.getTravelDateId(), wishList.getNumberOfPassengers(), wishList.getTotalPrice());
    }
}
