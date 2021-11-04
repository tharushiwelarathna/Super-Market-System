package lk.ijse.supermarket.model;


import java.util.Date;

public class Orders {
         private String orderID;
        private Date orderDate;
       private String customerID;
       private int userID;

    public Orders() {
    }

    public Orders(String orderID, Date orderDate, String customerID, int userID) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.customerID = customerID;
        this.userID = userID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderID='" + orderID + '\'' +
                ", orderDate=" + orderDate +
                ", customerID='" + customerID + '\'' +
                ", userID=" + userID +
                '}';
    }
}
