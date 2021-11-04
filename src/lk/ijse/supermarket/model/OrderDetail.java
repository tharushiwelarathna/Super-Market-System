package lk.ijse.supermarket.model;

import java.math.BigDecimal;

public class OrderDetail {

     private int qty;
     private BigDecimal unitPrice;
     private String orderID;
     private String propertyID;

    public OrderDetail() {
    }

    public OrderDetail(int qty, BigDecimal unitPrice, String orderID, String propertyID) {
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.orderID = orderID;
        this.propertyID = propertyID;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", orderID='" + orderID + '\'' +
                ", propertyID='" + propertyID + '\'' +
                '}';
    }
}
