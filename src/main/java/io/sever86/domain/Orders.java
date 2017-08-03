package io.sever86.domain;

import java.sql.Timestamp;

/**
 * Created by Администратор on 29.12.2016.
 */
public class Orders {
    private Integer orderId;
    private String product;
    private String executor;
    private Timestamp expirationDate;
    private boolean fulfiled;


    public Orders() {
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isFulfiled() {
        return fulfiled;
    }

    public void setFulfiled(boolean fulfiled) {
        this.fulfiled = fulfiled;
    }
}
