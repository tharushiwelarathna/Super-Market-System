package lk.ijse.supermarket.model;

public class Customer {
    private String customerID;
    private String customerType;
    private String customerName;
    private String customerAddress;
    private String city;
    private String province;
    private int contact;

    public Customer() {
    }

    public Customer(String customerID, String customerType, String customerName, String customerAddress, String city, String province, int contact) {
        this.customerID = customerID;
        this.customerType = customerType;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.city = city;
        this.province = province;
        this.contact = contact;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID='" + customerID + '\'' +
                ", customerType='" + customerType + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", contact=" + contact +
                '}';
    }
}
