package Business;

public class Summary {
    private String Make;
    private String Model;
    private Integer availableUnits;
    private Integer soldUnits;
    private String soldTotalPrices;
    private String lastPurchaseAt;

    //Define the fields used to display search results under Find button
    public String toString() {
        return getMake() + " - " + getModel();
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String make) {
        Make = make;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public Integer getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(Integer availableUnits) {
        this.availableUnits = availableUnits;
    }

    public Integer getSoldUnits() {
        return soldUnits;
    }

    public void setSoldUnits(Integer soldUnits) {
        this.soldUnits = soldUnits;
    }

    public String getSoldTotalPrices() {
        return soldTotalPrices;
    }

    public void setSoldTotalPrices(String soldTotalPrices) {
        this.soldTotalPrices = soldTotalPrices;
    }

    public String getLastPurchaseAt() {
        return lastPurchaseAt;
    }

    public void setLastPurchaseAt(String lastPurchaseAt) {
        this.lastPurchaseAt = lastPurchaseAt;
    }
}
