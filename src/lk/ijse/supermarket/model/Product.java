package lk.ijse.supermarket.model;

public class Product {
    private String productID;
    private String productName;
    private String description;
    private String specification;
    private String displayName;
    private String availability;
    private String activeState;
    private String availableBrand;

    public Product() {

    }

    public Product(String productID, String productName, String description, String specification, String displayName, String availability, String activeState, String availableBrand) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.specification = specification;
        this.displayName = displayName;
        this.availability = availability;
        this.activeState = activeState;
        this.availableBrand = availableBrand;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getActiveState() {
        return activeState;
    }

    public void setActiveState(String activeState) {
        this.activeState = activeState;
    }

    public String getAvailableBrand() {
        return availableBrand;
    }

    public void setAvailableBrand(String availableBrand) {
        this.availableBrand = availableBrand;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productID='" + productID + '\'' +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", specification='" + specification + '\'' +
                ", displayName='" + displayName + '\'' +
                ", availability='" + availability + '\'' +
                ", activeState='" + activeState + '\'' +
                ", availableBrand='" + availableBrand + '\'' +
                '}';
    }
}
