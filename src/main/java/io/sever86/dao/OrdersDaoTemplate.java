package io.sever86.dao;

import io.sever86.controllers.Controller;
import io.sever86.domain.Orders;
import io.sever86.domain.Personal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Костя on 03.08.2017.
 */
public class OrdersDaoTemplate implements OrdersDao {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Orders> rowMapperOrders = (ResultSet rs, int rowNum) -> {
        Orders orders = new Orders();
        orders.setOrderId(rs.getInt("order_id"));
        orders.setProduct(rs.getString("product"));
        orders.setExecutor(rs.getString("executor"));
        orders.setExpirationDate(rs.getTimestamp("expiration_date"));
        orders.setFulfiled(rs.getBoolean("fulfilled"));
        return orders;
    };

    private RowMapper<String> rowMapperTime = (ResultSet rs, int rowNum) -> {
        String time = rs.getString("time");
        return time;
    };

    private RowMapper<String> rowMapperProduct = (ResultSet rs, int rowNum) -> {
        String time = rs.getString("department_name");
        return time;
    };

    private RowMapper<Personal> rowMapperPersonal = (ResultSet rs, int rowNum) -> {
        Personal personal = new Personal();
        personal.setPersonalId(rs.getInt("personal_id"));
        personal.setFirstName(rs.getString("first_name"));
        personal.setSecondName(rs.getString("second_name"));
        personal.setLastName(rs.getString("last_name"));
        personal.setDepartment(rs.getString("department"));
        return personal;
    };

    public List<Orders> findAllOrders() {
        return jdbcTemplate.query("SELECT * FROM orders ", rowMapperOrders);
    }

    public List<Orders> findUnfulfilled() {
        return jdbcTemplate.query("SELECT * FROM orders  where fulfilled=false ", rowMapperOrders);
    }

    public List<Orders> findOrdersByDepartmets() {
        return jdbcTemplate.query("select * from orders where executor='cushioned furniture' \n" +
                "or\n" +
                "executor='storage systems' \n" +
                "or\n" +
                "executor='office furniture' \n ", rowMapperOrders);
    }

    public List<Orders> findOrdersByPersonal() {
        return jdbcTemplate.query("select * from orders where \n" +
                "executor<>'cushioned furniture' \n" +
                "and\n" +
                "executor<>'storage systems' \n" +
                "and\n" +
                "executor<>'office furniture' ", rowMapperOrders);
    }

    public String loadTimeExpiration(Integer id) {
        return jdbcTemplate.query(" select ((select expiration_date from orders \n" +
                "where order_id=?) -now()) as \"time\" ", rowMapperTime, id).get(0);
    }

    public Integer addOrder(Orders order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Orders(product,executor,expiration_date,fulfilled) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, order.getProduct());
            ps.setString(2, order.getExecutor());
            ps.setTimestamp(3, order.getExpirationDate());
            ps.setBoolean(4, order.isFulfiled());
            return ps;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return (Integer) keyHolder.getKeys().get("order_id");
    }

    public String findDepartmentByProduct(Orders order) {
        return jdbcTemplate.query("select department_name from department where product_name=? ", rowMapperProduct, order.getProduct()).get(0);
    }

    public void deletePersonal(String secondName) {
        List<Orders> list = jdbcTemplate.query("SELECT * FROM orders where executor=? ", rowMapperOrders, secondName);
        if (!list.isEmpty()) {
            String department = jdbcTemplate.query("select department as \"department_name\" from personal where second_name=?", rowMapperProduct, secondName).get(0);
            jdbcTemplate.update("UPDATE orders SET executor = ? WHERE executor = ?; ", department, secondName);
            jdbcTemplate.update("DELETE FROM personal where second_name=?", secondName);
        }
    }


    /////////////CRUD
    public Orders readOrder(Integer id) {
        return jdbcTemplate.query("SELECT * FROM orders  where order_id=? ", rowMapperOrders, id).get(0);
    }

    public void updateOrder(Orders order) {
        jdbcTemplate.update("UPDATE orders SET product = ?,executor=?,expiration_date=?,fulfilled=? WHERE order_id = ?; ",
                order.getProduct(), order.getExecutor(), order.getExpirationDate(), order.isFulfiled(), order.getOrderId());
    }

    public void deleteOrder(Integer id) {
        jdbcTemplate.update("DELETE FROM orders where order_id=?", id);

    }

    public void creatPersonal(Personal personal) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Personal(first_name,second_name,last_name,department) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, personal.getFirstName());
            ps.setString(2, personal.getSecondName());
            ps.setString(3, personal.getLastName());
            ps.setString(4, personal.getDepartment());
            return ps;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
    }

    public Personal readPersonal(Integer id) {
        return jdbcTemplate.query("SELECT * FROM personal  where personal_id=? ", rowMapperPersonal, id).get(0);
    }

    public void updatePersonal(Personal personal) {
        jdbcTemplate.update("UPDATE personal SET first_name = ?,second_name=?,last_name=?,department=? WHERE personal_id = ?; ",
                personal.getFirstName(), personal.getSecondName(), personal.getLastName(), personal.getDepartment(), personal.getPersonalId());
    }
}
