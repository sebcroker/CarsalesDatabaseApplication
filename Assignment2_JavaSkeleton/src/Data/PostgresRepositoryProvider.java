package Data;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import Presentation.IRepositoryProvider;
import Business.User;
import Business.CarSale;
import Business.Summary;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.util.PSQLException;



/**
 * Encapsulates create/read/update/delete operations to PostgreSQL database
 */
public class PostgresRepositoryProvider implements IRepositoryProvider {

	// instance variable for the database connection   
	private final String[] myHost = new String[] {"awsprddbs4836.shared.sydney.edu.au"};
	private final String userid = "y25s1c9120_scro5859";
    private final String passwd = "Sqlseb1!";
    
	private Connection openConnection() throws SQLException {
		PGSimpleDataSource source = new PGSimpleDataSource();
		source.setServerNames(myHost);
		source.setDatabaseName(userid);
		source.setUser(userid);
		source.setPassword(passwd);
		Connection conn = source.getConnection();
	    
	    return conn;
	}
	/**
	 * Check Salesperson login
	 *
	 * @param userName : the userName of Salesperson login
	 * @param password : the password of Salesperson login
	 */
	@Override
	public User checkLogin(String userName, String password) {
		//Create Object to connect with the database
		PostgresRepositoryProvider carDB = new PostgresRepositoryProvider();
		
		//Object setup
		String uName = null;
		String firstName = null;
		String lastName = null;
		User returnuser = new User();

		//try-catch block for error handling
		try {
			Connection conn = carDB.openConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM salesperson WHERE username ilike ? AND password = ?");
			stmt.setString(1, userName);
			stmt.setString(2, password);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				uName = rset.getString("username");
				firstName = rset.getString("firstname");
				lastName = rset.getString("lastname");
			}

			//must close our connection etc. objects. do they need to be in the finally block?
			conn.close();
			stmt.close();
			rset.close();
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}

		returnuser.setFirstName(firstName);
		returnuser.setLastName(lastName);
		returnuser.setUserName(uName);
		return returnuser;
	};

	/**
	 * Retrieves the summary of car sales from the database.
	 * Each Summary object holds details about the car sale including make, model, and sale details.
	 * @return A Vector<Summary> containing the car sale summaries.
	 */
	@Override
	public Vector<Summary> getCarSalesSummary() {
		//Object setup
		PostgresRepositoryProvider carDB = new PostgresRepositoryProvider();
		Vector<Summary> vectorSummary = new Vector<Summary>();

		//try-catch block
		try {
			Connection conn = carDB.openConnection();
			PreparedStatement stmt = conn.prepareStatement("select makename, modelname, (count(*) - count(saledate))::INTEGER as availableunits, count(saledate)::INTEGER as soldunits, sum(CASE WHEN issold = true THEN price else 0 END)::varchar(20) as totalsales, Coalesce(TO_CHAR(max(saledate), 'DD-MM-YYYY'), '') as lastpurchasedat\n" + //
								"from CarSales join (Model join Make using (MakeCode)) using (ModelCode)\n" + //
								"group by makename, modelname\n" + //
								"order by makename, modelname asc");
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				Summary sum = new Summary();
				String Make = rset.getString("makename");
				String Model = rset.getString("modelname");
				Integer availableUnits = rset.getInt("availableunits");
				Integer soldUnits = rset.getInt("soldunits");
				String soldTotalPrices = rset.getString("totalsales");
				String lastPurchaseAt = rset.getString("lastpurchasedat");
				sum.setMake(Make);
				sum.setModel(Model);
				sum.setAvailableUnits(availableUnits);
				sum.setSoldUnits(soldUnits);
				sum.setSoldTotalPrices(soldTotalPrices);
				sum.setLastPurchaseAt(lastPurchaseAt);
				vectorSummary.add(sum);
			}
			
			//close the objects!!
			conn.close();
			stmt.close();
			rset.close();
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
		return vectorSummary;
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
		//Object setup
		PostgresRepositoryProvider carDB = new PostgresRepositoryProvider();
		Vector<CarSale> carsalevector = new Vector<CarSale>();

		try {
			Connection conn = carDB.openConnection();
			PreparedStatement stmt = conn.prepareStatement("select carsaleid, makename, modelname, builtyear, odometer, price, issold, coalesce(customer.firstname||' '||customer.lastname, '') as buyer, coalesce(salesperson.firstname||' '||salesperson.lastname, '') as salesperson, Coalesce(TO_CHAR(saledate, 'DD-MM-YYYY'), '') as saledate\n" + //
			"from ((CarSales join (Model join Make using (MakeCode)) using (ModelCode)) left join customer on (customerid = buyerid)) left join salesperson on (username = salespersonid)\n" + //
			"where (saledate is null or saledate >= NOW() - INTERVAL '3 years') and (modelname ilike ? OR makename ilike ? or customer.firstname||' '||customer.lastname ilike ? or salesperson.firstname||' '||salesperson.lastname ilike ?)\n" + //
			"order by issold, EXTRACT(YEAR FROM saledate) asc, EXTRACT(MONTH FROM saledate) asc, EXTRACT(DAY FROM saledate) asc, makename asc, modelname asc");
			stmt.setString(1, "%" + searchString + "%");
			stmt.setString(2, "%" + searchString + "%");
			stmt.setString(3, "%" + searchString + "%");
			stmt.setString(4, "%" + searchString + "%");
			
			ResultSet rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				CarSale cs = new CarSale();
				int carSaleId = rset.getInt("carsaleid");
				String Make = rset.getString("makename");
				String Model = rset.getString("modelname");
				Integer BuiltYear = rset.getInt("builtyear");
				Integer Odometer = rset.getInt("odometer");
				BigDecimal Price = rset.getBigDecimal("price");
				String SaleDate = rset.getString("saledate");
				Boolean IsSold = rset.getBoolean("issold");
				String Buyer = rset.getString("buyer");
				String Salesperson = rset.getString("salesperson");
				
				cs.setCarSaleId(carSaleId);
				cs.setMake(Make);
				cs.setModel(Model);
				cs.setBuiltYear(BuiltYear);
				cs.setOdometer(Odometer);
				cs.setPrice(Price);
				cs.setSaleDate(SaleDate);
				cs.setIsSold(IsSold);
				cs.setBuyer(Buyer);
				cs.setSalesperson(Salesperson);
				carsalevector.add(cs);
				
			}

			conn.close(); 
			stmt.close();
			rset.close();
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}


		return carsalevector;
	}

	/**
	 * Adds a new car sale to the database.
	 *
	 * @param carSale : carSale The CarSale object to be added to the database.
	 * @return true if the car sale was added successfully, false if there was a failure.
	 */
	@Override
	public boolean addCarSale(CarSale carSale) {
		PostgresRepositoryProvider carDB = new PostgresRepositoryProvider();
		boolean returnvalue = false;

		try {
			Connection conn = carDB.openConnection();
			CallableStatement call = conn.prepareCall("call Addcarsale(?, ?, ?, ?, ?)");
			call.setString(1, carSale.getMake());
			call.setString(2, carSale.getModel());
			call.setInt(3, carSale.getBuiltYear());
			call.setInt(4, carSale.getOdometer());
			call.setBigDecimal(5, carSale.getPrice());
			call.execute();
			
			conn.close();
			call.close();
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}

		returnvalue = true;
		return returnvalue;
	}

	/**
	 * Updates an existing car sale in the database. The method assumes
 	 * that the car sale to be updated already exists in the system.
	 *
	 * @param carSale : The CarSale object containing updated details for the car sale.
	 * @return true if the car sale was updated successfully, false if the update failed.
	 */
	@Override
	public boolean updateCarSale(CarSale carSale) {
		PostgresRepositoryProvider carDB = new PostgresRepositoryProvider();
		boolean returnvalue = false;

		try {
			Connection conn = carDB.openConnection();
			CallableStatement call = conn.prepareCall("call Updatecarsale2(?, ?, ?, ?)");
			if (carSale.getBuyer() == null && carSale.getSalesperson() == null && carSale.getSaleDateString() == null) {
				call.setString(1, "");
				call.setString(2, "");
				call.setString(3, "");
				call.setInt(4, carSale.getCarSaleId()); 
				call.execute();
			} else {
				call.setString(1, carSale.getBuyer().toLowerCase());
				call.setString(2, carSale.getSalesperson().toLowerCase());
				call.setString(3, carSale.getSaleDateString());
				call.setInt(4, carSale.getCarSaleId()); 
				call.execute();
			}
			

			conn.close();
			call.close();
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		returnvalue = true;
		return returnvalue;
	}
}
