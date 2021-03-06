package cs353.proje.usecases.courier.repository;

import cs353.proje.usecases.common.dto.AssignedOrder;
import cs353.proje.usecases.common.dto.Order;
import cs353.proje.usecases.courier.dto.AllCourierData;
import cs353.proje.usecases.courier.dto.OperateRegion;
import cs353.proje.usecases.courier.dto.OrderDetailsForCourier;
import cs353.proje.usecases.customer.dto.Customer;
import cs353.proje.usecases.customer.dto.OrderDetails;
import cs353.proje.usecases.customer.repository.CustomerRepository;
import cs353.proje.usecases.loginregister.dto.Courier;
import cs353.proje.usecases.loginregister.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Repository
public class CourierRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Lazy
    CustomerRepository customerRepository;

    RowMapper<AllCourierData> allCourierDataRowMapper = (rs, rowNum) -> {
        AllCourierData allCourierData = new AllCourierData();
        Courier courier = new Courier();
        courier.setUserId(rs.getInt("courier_id"));
        courier.setStatus(rs.getBoolean("status"));
        courier.setRating(rs.getDouble("rating"));
        courier.setEmail(rs.getString("email"));
        courier.setUsername(rs.getString("username"));
        courier.setPassword(rs.getString("password"));
        courier.setTelephone(rs.getString("telephone"));
        courier.setImage(rs.getString("image"));
        courier.setRegistrationDate(rs.getDate("registration_date"));
        courier.setName(rs.getString("name"));
        courier.setSurname(rs.getString("surname"));
        courier.setUserType(rs.getString("user_type"));

        allCourierData.setCourier(courier);
        allCourierData.setOperateRegions(getOperateRegions(courier.getUserId()));

        return allCourierData;
    };

    RowMapper<AssignedOrder> assignedOrderRowMapper = (rs, rowNum) ->{
        AssignedOrder assignedOrder = new AssignedOrder();
        assignedOrder.setCourierId(rs.getInt("courier_id"));
        assignedOrder.setAssignmentTime(rs.getTimestamp("assignment_time"));
        assignedOrder.setDecision(rs.getString("decision"));
        assignedOrder.setDecisionTime(rs.getTimestamp("decision_time"));
        assignedOrder.setOrderId(rs.getInt("order_id"));
        assignedOrder.setRestaurantId(rs.getInt("restaurant_id"));
        assignedOrder.setCustomerId(rs.getInt("customer_id"));
        assignedOrder.setPrice(rs.getDouble("price"));
        assignedOrder.setOrderTime(rs.getTimestamp("order_time"));
        assignedOrder.setDeliveryTime(rs.getTimestamp("delivery_time"));
        assignedOrder.setStatus(rs.getString("status"));
        assignedOrder.setOptionalDeliveryTime(rs.getTimestamp("optional_delivery_time"));
        assignedOrder.setPaymentMethod(rs.getString("payment_method"));
        assignedOrder.setCoupon(rs.getString("coupon"));
        assignedOrder.setRestaurantName(customerRepository.getRestaurantInfo(assignedOrder.getRestaurantId()).getRestaurantName());

        return assignedOrder;
    };

    RowMapper<Order> orderRowMapper = (rs, rowNum) ->{
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setRestaurantId(rs.getInt("restaurant_id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setPrice(rs.getDouble("price"));
        order.setOrderTime(rs.getTimestamp("order_time"));
        order.setDeliveryTime(rs.getTimestamp("delivery_time"));
        order.setStatus(rs.getString("status"));
        order.setOptionalDeliveryTime(rs.getTimestamp("optional_delivery_time"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setCoupon(rs.getString("coupon"));
        order.setRestaurantName(customerRepository.getRestaurantInfo(order.getRestaurantId()).getRestaurantName());

        return order;
    };

    RowMapper<OperateRegion> operateRegionRowMapper = (rs, rowNum) ->{
        OperateRegion operateRegion = new OperateRegion();
        operateRegion.setRegionId(rs.getInt("region_id"));
        operateRegion.setFee(rs.getDouble("fee"));

        return operateRegion;
    };

    RowMapper<OrderDetailsForCourier> orderDetailsForCourierRowMapper = (rs, rowNum) ->{
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setRestaurantId(rs.getInt("restaurant_id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setPrice(rs.getDouble("price"));
        order.setOrderTime(rs.getTimestamp("order_time"));
        order.setDeliveryTime(rs.getTimestamp("delivery_time"));
        order.setStatus(rs.getString("status"));
        order.setOptionalDeliveryTime(rs.getTimestamp("optional_delivery_time"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setCoupon(rs.getString("coupon"));
        order.setRestaurantName(customerRepository.getRestaurantInfo(order.getRestaurantId()).getRestaurantName());

        OrderDetailsForCourier orderDetailsForCourier = new OrderDetailsForCourier();
        orderDetailsForCourier.setOrder(order);
        orderDetailsForCourier.setCustomerName(rs.getString("name"));
        orderDetailsForCourier.setCustomerSurname(rs.getString("surname"));
        orderDetailsForCourier.setCustomerImage(rs.getString("image"));
        orderDetailsForCourier.setCustomerAddress(rs.getString("address"));
        orderDetailsForCourier.setCustomerRegionName(rs.getString("region_name"));
        orderDetailsForCourier.setCustomerTelephone(rs.getString("telephone"));
        orderDetailsForCourier.setCourierScore(getCourierScoreFromReview(order.getOrderId()));

        return orderDetailsForCourier;
    };

    RowMapper<Integer> integerRowMapper = (rs, rowNum) ->{
        return rs.getInt(1);
    };


    public List<AllCourierData> getCourierData(int courierId){

        String sql = "SELECT * FROM courier INNER JOIN user " +
                "ON user.user_id = courier.courier_id WHERE courier.courier_id = ?";
        Object[] params = {courierId};

        return jdbcTemplate.query(sql, params, allCourierDataRowMapper);
    }

    public boolean updateCourierData(int courierId, User updatedCourierData){

        String sql = "UPDATE user " +
                "SET email = ?, " +
                "username = ?, " +
                "password = ?, " +
                "telephone = ?, " +
                "image = ?, " +
                "name = ?, " +
                "surname = ? WHERE user.user_id = ?";
        Object[] params = {updatedCourierData.getEmail(), updatedCourierData.getUsername(), updatedCourierData.getPassword(),
        updatedCourierData.getTelephone(), updatedCourierData.getImage(), updatedCourierData.getName(),
        updatedCourierData.getSurname(), courierId};

        return jdbcTemplate.update(sql, params) == 1;
    }

    public boolean updateOperateRegions(int courierId, List<OperateRegion> regions){

        //If regions array is empty, result2 always becomes 0
        //And if no deletion occurs, result also becomes 0
        int result, result2 = 1;
        String sql = "DELETE FROM operates_in WHERE courier_id = ? ";
        Object[] params = {courierId};
        result = jdbcTemplate.update(sql, params);

        sql = "INSERT INTO operates_in(courier_id, region_id, fee) VALUES (?,?,?)";
        for (OperateRegion region : regions) {
            Object[] params2 = {courierId, region.getRegionId(), region.getFee()};
            result2 = jdbcTemplate.update(sql, params2);
        }

        return (result > 0 && result2 > 0);
    }

    public List<OrderDetailsForCourier> getCurrentAssignments(int courierId){
        String sql = "SELECT * FROM assigned_to " +
                "INNER JOIN `order` ON `order`.order_id = assigned_to.order_id " +
                "INNER JOIN customer ON customer.customer_id = order.customer_id " +
                "INNER JOIN user ON user.user_id = customer.customer_id " +
                "INNER JOIN region ON region.region_id = customer.region_id " +
                "WHERE decision = 'Pending' AND courier_id = ?";
        Object[] params = {courierId};

        return jdbcTemplate.query(sql, params, orderDetailsForCourierRowMapper);
    }

    public List<OrderDetailsForCourier> getAcceptedOrder(int courierId){
        String sql = "SELECT * FROM `order` " +
                "INNER JOIN assigned_to ON assigned_to.order_id = `order`.order_id " +
                "INNER JOIN customer ON customer.customer_id = order.customer_id " +
                "INNER JOIN user ON user.user_id = customer.customer_id " +
                "INNER JOIN region ON region.region_id = customer.region_id " +
                "WHERE courier_id = ? AND `order`.status = 'Delivering' AND assigned_to.decision = 'Accepted'  ";
        Object[] params = {courierId};

        return jdbcTemplate.query(sql, params, orderDetailsForCourierRowMapper );
    }

    public boolean acceptAssignment(int courierId, int orderId){
        String sql = "UPDATE assigned_to SET decision = 'Accepted', decision_time = ? " +
                "WHERE courier_id = ? AND order_id = ?";
        Object[] params = {Timestamp.from(Instant.now()), courierId, orderId};

        boolean result1 = jdbcTemplate.update(sql, params) == 1;

        OrderDetails orderDetails = customerRepository.getOrderDetails(orderId);
        Customer customer = customerRepository.getCustomerData(orderDetails.getOrder().getCustomerId());

        String sql2 = "SELECT fee FROM operates_in " +
                "WHERE courier_id = ? AND region_id = ? ";
        Object[] params2 = {courierId, customer.getRegion_id()};
        double fee = jdbcTemplate.queryForObject(sql2, params2, Double.class);

        String sql3 = "UPDATE `order` SET delivery_fee = ? " +
                "WHERE order_id = ?";
        Object[] params3 = {fee, orderId};

        boolean result2 = jdbcTemplate.update(sql3, params3) == 1;

        return result1 && result2;

    }

    public boolean rejectAssignment(int courierId, int orderId){
        String sql = "UPDATE assigned_to SET decision = 'Declined', decision_time = ? " +
                "WHERE courier_id = ? AND order_id = ?";
        Object[] params = {Timestamp.from(Instant.now()), courierId, orderId};

        return jdbcTemplate.update(sql, params) == 1;
    }

    public boolean finalizeOrder(int courierId, int orderId){
        String sql = "UPDATE `order` SET status = 'Delivered-Waiting Your Approval', delivery_time = ? " +
                "WHERE order_id = ? AND EXISTS " +
                "(SELECT * FROM assigned_to WHERE courier_id = ? AND order_id = ? AND decision = 'Accepted')";
        Object[] params = {Timestamp.from(Instant.now()), orderId, courierId, orderId};
        return jdbcTemplate.update(sql, params) == 1;
    }

    public List<OperateRegion> getOperateRegions(int courierId){
        String sql = "SELECT region_id, fee FROM operates_in WHERE courier_id = ?";
        Object[] params = {courierId};

        return jdbcTemplate.query(sql, params, operateRegionRowMapper);
    }

    public List<OrderDetailsForCourier> getOldOrders(int courierId){
        String sql = "SELECT * FROM `order` " +
                "INNER JOIN assigned_to ON assigned_to.order_id = order.order_id " +
                "INNER JOIN customer ON customer.customer_id = order.customer_id " +
                "INNER JOIN user ON user.user_id = customer.customer_id " +
                "INNER JOIN region ON region.region_id = customer.region_id " +
                "WHERE courier_id = ? AND decision = 'Accepted' " +
                "ORDER BY order_time DESC";
        Object[] params = {courierId};

        return jdbcTemplate.query(sql, params,orderDetailsForCourierRowMapper);
    }

    public boolean statusUpdateDelivering(int orderId) {
        String sql = "UPDATE `order` SET status = ? " +
                "WHERE order_id = ? ";
        Object[] params = {"Delivering", orderId};

        return jdbcTemplate.update(sql, params) == 1;
    }

    public boolean statusUpdateWaitingCourier(int orderId) {
        String sql = "UPDATE `order` SET status = ? " +
                "WHERE order_id = ? ";
        Object[] params = {"Waiting Courier", orderId};

        return jdbcTemplate.update(sql, params) == 1;
    }

    public boolean open(int courierId) {
        String sql = "UPDATE courier SET status = ? " +
                "WHERE courier_id = ?";
        Object[] params = {1, courierId};

        return jdbcTemplate.update(sql, params) == 1;
    }

    public boolean close(int courierId) {
        String sql = "UPDATE courier SET status = ? " +
                "WHERE courier_id = ?";
        Object[] params = {0, courierId};

        return jdbcTemplate.update(sql, params) == 1;
    }

    public boolean getCourierStatus(int courierId){
        String sql = "SELECT status FROM courier WHERE courier_id = ?";
        Object[] params = {courierId};

        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    public int getCourierScoreFromReview(int orderId){
        String sql = "SELECT courier_score FROM" +
                " review WHERE order_id = ?";
        Object[] params = {orderId};

        List<Integer> score = jdbcTemplate.query(sql, params, integerRowMapper);
        if (score.size() == 0)
            return -1;
        else
            return score.get(0);
    }

}
