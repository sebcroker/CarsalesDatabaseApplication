package Business;


public class BusinessComponentFactory {

	private static  BusinessComponentFactory sFactory = new BusinessComponentFactory();
	
	public static BusinessComponentFactory getInstance()
	{
		return sFactory;
	}
	
	public ICarSaleProvider getCarSaleProvider()
	{
		return new CarSaleProvider();
	}

}
