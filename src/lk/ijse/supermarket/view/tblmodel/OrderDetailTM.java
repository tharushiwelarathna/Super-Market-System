package lk.ijse.supermarket.view.tblmodel;

import java.math.BigDecimal;

public class OrderDetailTM {
    private String propertyID;
    private Double unitPrice;
    private int qty;
    private Double discount;
    private Double total;

    public OrderDetailTM() {
    }

    public OrderDetailTM(String propertyID, Double unitPrice, int qty, Double discount, Double total) {
        this.propertyID = propertyID;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.discount = discount;
        this.total = total;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderDetailTM{" +
                "propertyID='" + propertyID + '\'' +
                ", unitPrice=" + unitPrice +
                ", qty=" + qty +
                ", discount=" + discount +
                ", total=" + total +
                '}';
    }
}
