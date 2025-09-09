package Business;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CarSale {

	private int carSaleId = 0;
	private String Make;
	private String Model;
	private Integer BuiltYear;
	private Integer Odometer;
	private BigDecimal Price;
	private String SaleDate;
	private Boolean IsSold;
	private String Buyer;
	private String Salesperson;

	public int getCarSaleId() {
		return carSaleId;
	}

	public void setCarSaleId(int carSaleId) {
		this.carSaleId = carSaleId;
	}

	public Date formatDate(String date) {
		if (date != null && date.length() > 0) {
			java.util.Date newDate = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				sdf.setLenient(false); // strictly enforce the date format
				newDate = sdf.parse(date);
			} catch (ParseException e) {
				System.out.println("Incorrect date format. It should be dd-MM-yyyy.\n" + e.getMessage());
			}
			return new Date(newDate.getTime());
		} else {
			return null;
		}

	}

	public Date getSaleDate() {
		return formatDate(this.SaleDate);
	}

	public String getSaleDateString() {
		return this.SaleDate;
	}

	public void setSaleDate(String saleDate) {
		this.SaleDate = saleDate;
	}

	//Define the fields used to display search results under Find button
	public String toString() {
		return getCarSaleId() + " - " + getMake() + " " + getModel();
	}

	public Boolean getIsSold() {
		return IsSold;
	}

	public void setIsSold(Boolean sold) {
		IsSold = sold;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal price) {
		Price = price;
	}

	public Integer getOdometer() {
		return Odometer;
	}

	public void setOdometer(Integer odometer) {
		Odometer = odometer;
	}

	public Integer getBuiltYear() {
		return BuiltYear;
	}

	public void setBuiltYear(Integer builtYear) {
		BuiltYear = builtYear;
	}

	public String getSalesperson() {
		return Salesperson;
	}

	public void setSalesperson(String salesperson) {
		Salesperson = salesperson;
	}

	public String getBuyer() {
		return Buyer;
	}

	public void setBuyer(String buyer) {
		Buyer = buyer;
	}

	public String getModel() {
		return Model;
	}

	public void setModel(String model) {
		Model = model;
	}

	public String getMake() {
		return Make;
	}

	public void setMake(String make) {
		Make = make;
	}

}

