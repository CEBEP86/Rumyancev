package io.sever86.controllers;


import io.sever86.dao.OrdersDao;
import io.sever86.domain.Orders;
import io.sever86.domain.Personal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class Controller {
    @Autowired
    OrdersDao ordersDao;



    private static final Logger log = LoggerFactory.getLogger(Controller.class);

//////////////////////////////////////////////
    @RequestMapping(method = RequestMethod.GET, value = "/api/find-all-orders")
    @ResponseBody
    public List<Orders> findAllOrdersClient() {
        return ordersDao.findAllOrders();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/find-unfulfilled")
    @ResponseBody
    public List<Orders> findUnfulfilledClient() {
        return ordersDao.findUnfulfilled();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/find-orders-by-departments")
    @ResponseBody
    public List<Orders> findOrdersByDepartmetsClient() {
        return ordersDao.findOrdersByDepartmets();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/find-orders-by-personal")
    @ResponseBody
    public List<Orders> findOrdersByPersonalClient() {
        return ordersDao.findOrdersByPersonal();
    }



    @RequestMapping(method = RequestMethod.GET, path = "/api/load-time-expiration/{id}")
    @ResponseBody
    public String loadTimeExpirationClient(@PathVariable Integer id) {
        return ordersDao.loadTimeExpiration(id);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/add-order")
    @ResponseBody
    public void addOrderClient(@RequestBody Orders order) {
        if(order.getExecutor()=="")order.setExecutor(ordersDao.findDepartmentByProduct(order));
        Integer key_bd =(Integer) ordersDao.addOrder(order);
    }


    @RequestMapping(method = RequestMethod.DELETE , path = "/api/delete-personal/{secondName}")
    @ResponseBody
    public void deletePersonalClient(@PathVariable String secondName) {
        ordersDao.deletePersonal(secondName);
    }

    //////////////////////CRUD
    @RequestMapping(method = RequestMethod.GET, path = "/api/read-order/{id}")
    @ResponseBody
    public Orders readOrderClient(@PathVariable Integer id) {
        return ordersDao.readOrder(id);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/update-order")
    @ResponseBody
    public void updateOrderClient(@RequestBody Orders order) {
        ordersDao.updateOrder(order);
    }

    @RequestMapping(method = RequestMethod.DELETE , path = "/api/delete-order/{id}")
    @ResponseBody
    public void deleteOrderClient(@PathVariable Integer id) {
        ordersDao.deleteOrder(id);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/create-personal")
    @ResponseBody
    public void createPersonalClient(@RequestBody Personal personal) {
      ordersDao.creatPersonal(personal);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/read-personal/{id}")
    @ResponseBody
    public Personal readPersonalClient(@PathVariable Integer id) {
        return ordersDao.readPersonal(id);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/api/update-personal")
    @ResponseBody
    public void updatePersonalClient(@RequestBody Personal personal) {
        ordersDao.updatePersonal(personal);
    }

}
