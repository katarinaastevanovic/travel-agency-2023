package com.ftn.PrviMavenVebProjekat.dao.impl;

import com.ftn.PrviMavenVebProjekat.dao.ShoppingCartDAO;
import com.ftn.PrviMavenVebProjekat.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ShoppingCartDAOImpl implements ShoppingCartDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(ShoppingCart shoppingCart) {
        String sql = "INSERT INTO shopping_cart (user_id, travel_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, shoppingCart.getUser().getId(), shoppingCart.getTravel().getId());
    }

    @Override
    public List<ShoppingCart> findByUserId(Long userId) {
        String sql = "SELECT * FROM shopping_cart WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToShoppingCart(rs), userId);
    }

    @Override
    public List<ShoppingCart> findByUser(User user) {
        return findByUserId(user.getId()); // Koristimo findByUserId za pretragu
    }

    @Override
    public void saveReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (travel_id, travel_date_id, departure_date, return_date, final_price, creation_date, status, number_of_passengers, user_id, points) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                reservation.getTravelId(),
                reservation.getTravelDateId(),
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getFinalPrice(),
                reservation.getCreationDate(),
                reservation.getStatus().toString(),
                reservation.getNumberOfPassengers(),
                reservation.getUserId(),
                reservation.getPoints()); // Sačuvaj broj poena
    }





    @Override
    public List<Reservation> findReservationsByStatus(String status) {
        String sql = "SELECT * FROM reservations WHERE status = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Reservation reservation = new Reservation();
            reservation.setId(rs.getLong("id"));
            reservation.setTravelId(rs.getLong("travel_id"));
            reservation.setTravelDateId(rs.getLong("travel_date_id"));  // Mapiranje travel_date_id
            reservation.setDepartureDate(rs.getTimestamp("departure_date").toLocalDateTime());
            reservation.setReturnDate(rs.getTimestamp("return_date").toLocalDateTime());
            reservation.setFinalPrice(rs.getDouble("final_price"));
            reservation.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
            reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
            reservation.setNumberOfPassengers(rs.getInt("number_of_passengers"));
            reservation.setUserId(rs.getLong("user_id"));
            return reservation;
        }, status);
    }





    private ShoppingCart mapRowToShoppingCart(ResultSet rs) throws SQLException {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(rs.getLong("id"));

        User user = new User();
        user.setId(rs.getLong("user_id"));
        shoppingCart.setUser(user);

        Travel travel = new Travel();
        travel.setId(rs.getLong("travel_id"));
        shoppingCart.setTravel(travel);

        return shoppingCart;
    }

    @Override
    public void deleteReservationById(Long reservationId) {
        System.out.println("Deleting reservation with ID: " + reservationId);
        String sql = "DELETE FROM reservations WHERE id = ?";
        jdbcTemplate.update(sql, reservationId);
    }

    @Override
    public void updateReservationStatus(Long reservationId, ReservationStatus status) {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status.toString(), reservationId);
    }


    @Override
    public void updateFreeSeatsAndBeds(Long travelDateId, int numberOfPassengers) {
        String sql = "UPDATE travel_dates SET number_of_free_seats = number_of_free_seats - ?, number_of_free_beds = number_of_free_beds - ? WHERE id = ?";
        jdbcTemplate.update(sql, numberOfPassengers, numberOfPassengers, travelDateId);
    }

    @Override
    public List<Reservation> findReservationsByUserIdAndStatus(Long userId, ReservationStatus status) {
        String sql = "SELECT * FROM reservations WHERE user_id = ? AND status = ?";
        return jdbcTemplate.query(sql, new Object[]{userId, status.toString()}, (rs, rowNum) -> {
            Reservation reservation = new Reservation();
            reservation.setId(rs.getLong("id"));
            reservation.setTravelId(rs.getLong("travel_id"));
            reservation.setDepartureDate(rs.getTimestamp("departure_date").toLocalDateTime());
            reservation.setReturnDate(rs.getTimestamp("return_date").toLocalDateTime());
            reservation.setFinalPrice(rs.getDouble("final_price"));
            reservation.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
            reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
            reservation.setNumberOfPassengers(rs.getInt("number_of_passengers"));
            reservation.setUserId(rs.getLong("user_id"));
            return reservation;
        });
    }

    @Override
    public String findDestinationCityByTravelId(Long travelId) {
        String sql = "SELECT d.grad FROM travels t JOIN destinacije d ON t.destinacijaId = d.id WHERE t.id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{travelId}, String.class);
    }

    @Override
    public Reservation findReservationById(Long reservationId) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{reservationId}, (rs, rowNum) -> {
            Reservation reservation = new Reservation();
            reservation.setId(rs.getLong("id"));
            reservation.setTravelId(rs.getLong("travel_id"));
            reservation.setDepartureDate(rs.getTimestamp("departure_date").toLocalDateTime());
            reservation.setReturnDate(rs.getTimestamp("return_date").toLocalDateTime());
            reservation.setFinalPrice(rs.getDouble("final_price"));
            reservation.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
            reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
            reservation.setNumberOfPassengers(rs.getInt("number_of_passengers"));
            reservation.setUserId(rs.getLong("user_id"));
            reservation.setPoints(rs.getInt("points"));
            reservation.setUserId(rs.getLong("user_id"));
            // Dodajte ostala polja po potrebi
            return reservation;
        });
    }

    @Override
    public void updateLoyaltyCardPoints(Long userId, int points) {
        String sql = "UPDATE loyalty_card SET number_of_points = number_of_points + ? WHERE user_id = ?";
        jdbcTemplate.update(sql, points, userId);
    }

    @Override
    public void addPointsToLoyaltyCard(Long userId, int points) {
        String sql = "UPDATE loyalty_card SET number_of_points = number_of_points + ? WHERE user_id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, points, userId);
        System.out.println("Broj ažuriranih redova: " + rowsUpdated);
    }

    @Override
    public void deductPointsFromLoyaltyCard(Long userId, int points) {
        String sql = "UPDATE loyalty_card SET number_of_points = number_of_points - ? WHERE user_id = ?";
        jdbcTemplate.update(sql, points, userId);
    }


}
