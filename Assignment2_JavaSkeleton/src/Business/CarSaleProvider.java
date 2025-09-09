package Business;

import java.util.Vector;

import Data.RepositoryProviderFactory;

/**
 * Encapsulates any business logic to be executed on the app server; 
 * and uses the data layer for data queries/creates/updates/deletes
 *
 */
public class CarSaleProvider implements ICarSaleProvider {
	/**
	 * Check Salesperson login
	 *
	 * @param userName : the userName of Salesperson login
	 * @param password : the password of Salesperson login
	 */
	@Override
	public User checkLogin(String userName, String password) {
		return RepositoryProviderFactory.getInstance().getRepositoryProvider().checkLogin(userName, password);
	}

	/**
	 * Retrieves the summary of car sales from the database.
	 * Each Summary object holds details about the car sale including make, model, and sale details.
	 * @return A Vector<Summary> containing the car sale summaries.
	 */
	@Override
	public Vector<Summary> getCarSalesSummary() {
		return RepositoryProviderFactory.getInstance().getRepositoryProvider().getCarSalesSummary();
	}

	/**
	 * Searches for car sales in the database based on the provided search string.
	 * 
	 * Given an expression searchString like 'ro' or 'a class', this method should return
	 * returns a list of CarSale objects that match the search string.
	 *
	 * @param searchString : the searchString to use for finding carsales in the database
	 * @return A Vector<CarSale> containing the car sales that match the search criteria.
	 */
	@Override
	public Vector<CarSale> findCarSales(String searchString) {
		return RepositoryProviderFactory.getInstance().getRepositoryProvider().findCarSales(searchString);
	}

	/**
	 * Adds a new car sale to the database.
	 *
	 * @param carSale : carSale The CarSale object to be added to the database.
	 */
	@Override
	public void addCarSale(CarSale carSale) {
		boolean success = RepositoryProviderFactory.getInstance().getRepositoryProvider().addCarSale(carSale);

		if (success) {
			System.out.println("Add successfully!");
		} else {
			System.out.println("There was an error adding a new car for sale.");
		}
	}

	/**
	 * Updates an existing car sale in the database. The method assumes
	 * that the car sale to be updated already exists in the system.
	 *
	 * @param carSale : The CarSale object containing updated details for the car sale.
	 */
	@Override
	public void updateCarSale(CarSale carSale) {
		boolean success = RepositoryProviderFactory.getInstance().getRepositoryProvider().updateCarSale(carSale);

		if (success) {
			System.out.println("Update successfully!");
		} else {
			System.out.println("There was an error updating the carsale.");
		}
	}
}
