package cs353.proje.usecases.common.repository;

import cs353.proje.usecases.common.dto.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public class ReviewRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    RowMapper<Review> reviewRowMapper = (rs, rowNum) -> {
        Review review = new Review();
        review.setReviewId(rs.getInt("review_id"));
        review.setDate(rs.getTimestamp("date"));
        review.setRestaurantScore(rs.getInt("restaurant_score"));
        review.setCourierScore(rs.getInt("courier_score"));
        review.setComment(rs.getString("comment"));
        review.setOrderId(rs.getInt("order_id"));
        review.setResponse(rs.getString("response"));
        return review;
    };

    RowMapper<Integer> integerRowMapper = (rs, rowNum) -> rs.getInt(1);


    public List<Review> getReview( int orderId){
        String sql = "SELECT * FROM review WHERE order_id = ?";
        Object[] params = {orderId};

        return jdbcTemplate.query(sql, params, reviewRowMapper);
    }

    public boolean reviewExistsWithOrderId( int orderId){
        String sql = "SELECT COUNT(*) FROM review WHERE order_id = ?";
        Object[] params = {orderId};
        if(jdbcTemplate.queryForObject(sql,params,Integer.class) > 0)
            return true;
        return false;
    }

    public boolean reviewExistsWithReviewId( int reviewId){
        String sql = "SELECT COUNT(*) FROM review WHERE review_id = ?";
        Object[] params = {reviewId};
        if(jdbcTemplate.queryForObject(sql,params,Integer.class) > 0)
            return true;
        return false;
    }

    public int makeReview( int orderId, Review review){
        Date date = new Date();
        String sql = "INSERT INTO review (date, restaurant_score, courier_score, comment, order_id, response)" +
                " VALUES(?, ?, ?, ?, ?, null)";
        Object[] params = { Timestamp.from(Instant.now()), review.getRestaurantScore(), review.getCourierScore(),
            review.getComment(), orderId};
        if(jdbcTemplate.update(sql, params) < 0)
            return -1;
        else{
            String sqlGetId = "SELECT LAST_INSERT_ID()";
            return jdbcTemplate.queryForObject(sqlGetId, integerRowMapper);
        }
    }

    public boolean responseExists( int reviewId){
        String sql = "SELECT COUNT(*) FROM review WHERE review_id = ? AND response IS NOT NULL ";
        Object[] params = {reviewId};
        if(jdbcTemplate.queryForObject(sql, params, Integer.class) > 0)
            return true;
        return false;
    }

    public boolean makeResponse(int reviewId, String response){
        String sql = "UPDATE review SET response = ? WHERE review_id = ?";
        Object[] params = {response, reviewId};
        return (jdbcTemplate.update(sql,params) > 0);
    }

    public List<Review> getRestaurantReviews(int restaurantId){
        String sql = "SELECT review.review_id, review.date, review.restaurant_score," +
                "review.courier_score, review.comment, review.order_id, review.response " +
                "FROM review " +
                "INNER JOIN `order` ON `order`.order_id = review.order_id " +
                "WHERE restaurant_id = ? " +
                "ORDER BY review.date DESC";
        Object[] params = {restaurantId};

        return jdbcTemplate.query(sql, params, reviewRowMapper);
    }

    public boolean updateRestaurantRating(int score, int orderId){
        String sql = "SELECT DISTINCT rating FROM `order` " +
                "INNER JOIN restaurant ON restaurant.restaurant_id = `order`.restaurant_id " +
                "WHERE restaurant.restaurant_id IN  " +
                "(SELECT restaurant_id FROM `order` WHERE order_id = ?)";
        Object[] params = {orderId};
        double oldRating  = jdbcTemplate.queryForObject(sql, params, Double.class);

        sql = "SELECT DISTINCT restaurant.restaurant_id FROM `order` " +
                "INNER JOIN restaurant ON restaurant.restaurant_id = `order`.restaurant_id " +
                "WHERE restaurant.restaurant_id IN  " +
                "(SELECT restaurant_id FROM `order` WHERE order_id = ?)";
        int restaurantId = jdbcTemplate.queryForObject(sql, params, Integer.class);


        sql = "SELECT COUNT(*) FROM review\n" +
                "INNER JOIN `order` on `order`.order_id = review.order_id \n" +
                "WHERE restaurant_id = ?";
        Object[] params2 = {restaurantId};

        int numberOfOldOrders  = jdbcTemplate.queryForObject(sql, params2, Integer.class);

        double newRating = (oldRating * (numberOfOldOrders - 1) + score) / (numberOfOldOrders);
        newRating = Math.round(newRating * 10.0) / 10.0;

        sql = "UPDATE restaurant SET rating = ? WHERE restaurant_id = ?";
        Object[] params3 = {newRating, restaurantId};

        return(jdbcTemplate.update(sql, params3) > 0);
    }

    public boolean updateCourierRating(int score, int orderId){
        String sql = "SELECT DISTINCT assigned_to.courier_id FROM assigned_to " +
                "INNER JOIN courier ON courier.courier_id = assigned_to.courier_id " +
                "WHERE courier.courier_id IN " +
                "(SELECT courier_id FROM assigned_to WHERE order_id = ? AND decision = 'Accepted') " +
                "AND decision = 'Accepted'";
        Object[] params = {orderId};
        int courierId= jdbcTemplate.queryForObject(sql, params, Integer.class);

        sql = "SELECT COUNT(*) FROM review " +
                "INNER JOIN `order` ON order.order_id = review.order_id " +
                "INNER JOIN assigned_to ON assigned_to.order_id = `order`.order_id " +
                "WHERE assigned_to.courier_id = ?  ";
        Object[] params2 = {courierId};
        int numberOfOldOrders = jdbcTemplate.queryForObject(sql, params2, Integer.class);

        sql = "SELECT DISTINCT rating FROM assigned_to " +
                "INNER JOIN courier ON courier.courier_id = assigned_to.courier_id " +
                "WHERE courier.courier_id IN " +
                "(SELECT courier_id FROM assigned_to WHERE order_id = ? AND decision = 'Accepted') " +
                "AND decision = 'Accepted'";
        double oldRating = jdbcTemplate.queryForObject(sql, params, Double.class);



        double newRating = (oldRating * (numberOfOldOrders - 1) + score) / (numberOfOldOrders);
        newRating = Math.round(newRating * 10.0) / 10.0;

        sql = "UPDATE courier SET rating = ? WHERE courier_id = ?";
        Object[] params3 = {newRating, courierId};

        return(jdbcTemplate.update(sql, params3) > 0);
    }
}
