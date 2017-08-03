package io.sever86.dao;

/**
 * Created by Костя on 03.08.2017.
 */

import io.sever86.domain.Orders;
import io.sever86.domain.Personal;

import java.util.List;

public interface OrdersDao {
    List<Orders> findAllOrders();

    List<Orders> findUnfulfilled();

    List<Orders> findOrdersByDepartmets();

    List<Orders> findOrdersByPersonal();

    String loadTimeExpiration(Integer id);

    Integer addOrder(Orders order);

    String findDepartmentByProduct(Orders order);

    void deletePersonal(String secondName);

    //CRUD
    Orders readOrder(Integer id);

    void updateOrder(Orders order);

    void deleteOrder(Integer id);

    void creatPersonal(Personal personal);

    Personal readPersonal(Integer id);

    void updatePersonal(Personal personal);


}
