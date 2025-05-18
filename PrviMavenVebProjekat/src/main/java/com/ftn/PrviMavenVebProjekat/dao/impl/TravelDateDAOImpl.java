package com.ftn.PrviMavenVebProjekat.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ftn.PrviMavenVebProjekat.controller.MainController;
import com.ftn.PrviMavenVebProjekat.model.TravelDate;
import com.ftn.PrviMavenVebProjekat.model.TypeOfTravel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class TravelDateDAOImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public void save(TravelDate travelDate) {
        String sql = "INSERT INTO travel_dates (travel_id, arrival_date, departure_date, number_of_nights, number_of_free_seats, number_of_free_beds, discount, discount_start_date, discount_end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Integer discount = travelDate.getDiscount();
        LocalDateTime discountStartDate = travelDate.getDiscountStartDate();
        LocalDateTime discountEndDate = travelDate.getDiscountEndDate();

        if (discount == null) {
            jdbcTemplate.update(sql, travelDate.getTravelId(), travelDate.getArrivalDate(), travelDate.getDepartureDate(), travelDate.getNumberOfNights(), travelDate.getNumberOfFreeSeats(), travelDate.getNumberOfFreeBeds(), null, null, null);
        } else {
            jdbcTemplate.update(sql, travelDate.getTravelId(), travelDate.getArrivalDate(), travelDate.getDepartureDate(), travelDate.getNumberOfNights(), travelDate.getNumberOfFreeSeats(), travelDate.getNumberOfFreeBeds(), discount, discountStartDate, discountEndDate);
            saveDiscountPrice(travelDate.getTravelId(), discount);
        }
    }
    private void saveDiscountPrice(Long travelId, Integer discount) {
        // Dobijanje originalne cene putovanja iz baze podataka
        String sqlGetPrice = "SELECT price FROM travels WHERE id = ?";
        Double originalPrice = jdbcTemplate.queryForObject(sqlGetPrice, Double.class, travelId);

        // Računanje snižene cene
        Double discountPrice = originalPrice - (originalPrice * discount / 100);

        // Čuvanje snižene cene u tabelu travel_dates
        String sqlUpdateDiscountPrice = "UPDATE travel_dates SET discount_price = ? WHERE travel_id = ?";
        jdbcTemplate.update(sqlUpdateDiscountPrice, discountPrice, travelId);
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
                travelDate.setNumberOfFreeSeats(rs.getInt("number_of_free_seats"));
                travelDate.setNumberOfFreeBeds(rs.getInt("number_of_free_beds"));
                travelDate.setDiscount(rs.getInt("discount"));
                travelDate.setDiscountStartDate(rs.getTimestamp("discount_start_date") != null ? rs.getTimestamp("discount_start_date").toLocalDateTime() : null);
                travelDate.setDiscountEndDate(rs.getTimestamp("discount_end_date") != null ? rs.getTimestamp("discount_end_date").toLocalDateTime() : null);
                return travelDate;
            }
        }, travelId);
    }
    public int getNumberOfFreeBeds(Long travelId) {
        String sql = "SELECT au.capacity FROM accunits au INNER JOIN travels t ON au.id = t.accUnitId WHERE t.id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, travelId);
    }
    public void update(TravelDate travelDate, String updateType, List<Long> travelDateIds, TypeOfTravel typeOfTravel) {
        try {
            logger.info("Update Type: {}, TravelDate ID: {}", updateType, travelDate.getId());

            // Dohvati cenu iz tabele 'travels' za dati 'travel_id'
            String getPriceSql = "SELECT price FROM travels WHERE id = ?";
            Double price = jdbcTemplate.queryForObject(getPriceSql, Double.class, travelDate.getTravelId());
            logger.info("Fetched price for TravelDate ID {}: {}", travelDate.getId(), price);

            String getNumberOfNightsSql = "SELECT number_of_nights FROM travel_dates WHERE id = ?";
            Integer number_of_nights = jdbcTemplate.queryForObject(getNumberOfNightsSql, Integer.class, travelDate.getId());
            logger.info("Fetched number_of_nights for TravelDate ID {}: {}", travelDate.getId(), number_of_nights);

            if (price != null && number_of_nights != null) {
                Double discountPrice = price * number_of_nights - (price * number_of_nights * travelDate.getDiscount() / 100);
                BigDecimal roundedDiscountPrice = new BigDecimal(discountPrice).setScale(2, RoundingMode.HALF_UP);

                logger.info("Calculated discount price for TravelDate ID {}: {}", travelDate.getId(), roundedDiscountPrice);

                int rowsAffected = 0;

                switch (updateType) {
                    case "ID":
                        String updateSqlById = "UPDATE travel_dates SET discount = ?, discount_start_date = ?, discount_end_date = ?, discount_price = ? WHERE id = ?";
                        rowsAffected = jdbcTemplate.update(updateSqlById,
                                travelDate.getDiscount(),
                                travelDate.getDiscountStartDate(),
                                travelDate.getDiscountEndDate(),
                                roundedDiscountPrice,
                                travelDate.getId());
                        logger.info("Rows affected (ID update): {}", rowsAffected);
                        break;

                    case "TYPE":
                        List<TravelDate> travelDatesByType = findByTypeOfTravel(typeOfTravel);  // Dohvati sve TravelDate za određeni tip putovanja
                        for (TravelDate td : travelDatesByType) {
                            String getPriceSqlType = "SELECT price FROM travels WHERE id = ?";
                            Double priceType = jdbcTemplate.queryForObject(getPriceSqlType, Double.class, td.getTravelId());
                            Integer nightsType = td.getNumberOfNights();

                            if (priceType != null && nightsType != null) {
                                Double discountPriceType = priceType * nightsType - (priceType * nightsType * travelDate.getDiscount() / 100);
                                BigDecimal roundedDiscountPriceType = new BigDecimal(discountPriceType).setScale(2, RoundingMode.HALF_UP);

                                String updateSqlByType = "UPDATE travel_dates SET discount = ?, discount_start_date = ?, discount_end_date = ?, discount_price = ? WHERE id = ?";
                                int rowsAffectedType = jdbcTemplate.update(updateSqlByType,
                                        travelDate.getDiscount(),
                                        travelDate.getDiscountStartDate(),
                                        travelDate.getDiscountEndDate(),
                                        roundedDiscountPriceType,
                                        td.getId());
                                logger.info("Rows affected (Type update for ID {}): {}", td.getId(), rowsAffectedType);
                            }
                        }
                        break;



                    case "ALL":
                        List<TravelDate> allTravelDates = findAll();  // Dohvati sve TravelDate iz baze podataka
                        for (TravelDate td : allTravelDates) {
                            String getPriceSqlAll = "SELECT price FROM travels WHERE id = ?";
                            Double priceAll = jdbcTemplate.queryForObject(getPriceSqlAll, Double.class, td.getTravelId());
                            Integer nightsAll = td.getNumberOfNights();

                            if (priceAll != null && nightsAll != null) {
                                Double discountPriceAll = priceAll * nightsAll - (priceAll * nightsAll * travelDate.getDiscount() / 100);
                                BigDecimal roundedDiscountPriceAll = new BigDecimal(discountPriceAll).setScale(2, RoundingMode.HALF_UP);

                                String updateSqlAll = "UPDATE travel_dates SET discount = ?, discount_start_date = ?, discount_end_date = ?, discount_price = ? WHERE id = ?";
                                int rowsAffectedAll = jdbcTemplate.update(updateSqlAll,
                                        travelDate.getDiscount(),
                                        travelDate.getDiscountStartDate(),
                                        travelDate.getDiscountEndDate(),
                                        roundedDiscountPriceAll,
                                        td.getId());
                                logger.info("Rows affected (ALL update for ID {}): {}", td.getId(), rowsAffectedAll);
                            }
                        }
                        break;

                }

                if (rowsAffected > 0) {
                    logger.info("Travel dates updated successfully.");
                } else {
                    logger.warn("No travel dates were updated.");
                }
            } else {
                logger.warn("Price or number of nights not found for travel ID: {}", travelDate.getTravelId());
            }
        } catch (Exception e) {
            logger.error("Error updating travel date", e);
        }
    }

    public List<TravelDate> findAll() {
        String sql = "SELECT * FROM travel_dates ORDER BY id";
        return jdbcTemplate.query(sql, new TravelDateRowMapper());
    }

    public List<TravelDate> findByTypeOfTravel(TypeOfTravel typeOfTravel) {
        String sql = "SELECT td.* FROM travel_dates td JOIN travels t ON td.travel_id = t.id WHERE t.typeOfTravel = ?";
        return jdbcTemplate.query(sql, new TravelDateRowMapper(), typeOfTravel.name());
    }

    public List<TravelDate> findByTravelIds(List<Long> travelIds) {
        String sql = "SELECT * FROM travel_dates WHERE id IN (" + String.join(",", travelIds.stream().map(String::valueOf).toArray(String[]::new)) + ")";
        return jdbcTemplate.query(sql, new TravelDateRowMapper());
    }
    private static class TravelDateRowMapper implements RowMapper<TravelDate> {
        @Override
        public TravelDate mapRow(ResultSet rs, int rowNum) throws SQLException {
            TravelDate travelDate = new TravelDate();
            travelDate.setId(rs.getLong("id"));
            travelDate.setTravelId(rs.getLong("travel_id"));
            travelDate.setArrivalDate(rs.getTimestamp("arrival_date").toLocalDateTime());
            travelDate.setDepartureDate(rs.getTimestamp("departure_date").toLocalDateTime());
            travelDate.setNumberOfNights(rs.getInt("number_of_nights"));
            travelDate.setNumberOfFreeSeats(rs.getInt("number_of_free_seats"));
            travelDate.setNumberOfFreeBeds(rs.getInt("number_of_free_beds"));
            travelDate.setDiscount(rs.getInt("discount"));
            travelDate.setDiscountStartDate(rs.getTimestamp("discount_start_date") != null ? rs.getTimestamp("discount_start_date").toLocalDateTime() : null);
            travelDate.setDiscountEndDate(rs.getTimestamp("discount_end_date") != null ? rs.getTimestamp("discount_end_date").toLocalDateTime() : null);
            travelDate.setDiscountPrice(rs.getInt("discount_price"));
            return travelDate;
        }
    }
}


