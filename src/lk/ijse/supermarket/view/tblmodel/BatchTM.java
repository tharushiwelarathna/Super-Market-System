package lk.ijse.supermarket.view.tblmodel;



import java.math.BigDecimal;


public class BatchTM {
      private String propertyID;
      private String batch;
      private BigDecimal price;
      private boolean discountState;
      private BigDecimal discount;
      private String activeState;
      private int quantity;
      private String systemDate;
      private String productID;

    public BatchTM() {

    }

    public BatchTM(String propertyID, String batch, BigDecimal price, boolean discountState, BigDecimal discount, String activeState, int quantity, String systemDate, String productID) {
        this.propertyID = propertyID;
        this.batch = batch;
        this.price = price;
        this.discountState = discountState;
        this.discount = discount;
        this.activeState = activeState;
        this.quantity = quantity;
        this.systemDate = systemDate;
        this.productID = productID;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isDiscountState() {
        return discountState;
    }

    public void setDiscountState(boolean discountState) {
        this.discountState = discountState;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getActiveState() {
        return activeState;
    }

    public void setActiveState(String activeState) {
        this.activeState = activeState;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(String systemDate) {
        this.systemDate = systemDate;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    @Override
    public String toString() {
        return "BatchTM{" +
                "propertyID='" + propertyID + '\'' +
                ", batch='" + batch + '\'' +
                ", price=" + price +
                ", discountState=" + discountState +
                ", discount=" + discount +
                ", activeState='" + activeState + '\'' +
                ", quantity=" + quantity +
                ", systemDate='" + systemDate + '\'' +
                ", productID='" + productID + '\'' +
                '}';
    }
}
