package Presentation;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import Business.CarSale;

/**
 * 
 *
 * AddCarSaleDialog - used to add a new car sale record
 * 
 */
public class AddCarSaleDialog extends JDialog{

	private static final long serialVersionUID = 173323780409671768L;
	
	/**
	 * detailPanel: reuse CarSaleDetailPanel to add carSales
	 */
	private CarSaleDetailPanel detailPanel = new CarSaleDetailPanel(false);

	public AddCarSaleDialog()
	{
		setTitle(StringResources.getAppTitle());
		detailPanel.initCarSaleDetails(getBlankCarSale());
		add(detailPanel);
		updateLayout();
		setSize(400, 400);
	}

	private void updateLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		layout.addLayoutComponent(detailPanel, BorderLayout.CENTER);
	}

	private CarSale getBlankCarSale() {
		CarSale carSale = new CarSale();
		return carSale;
	}
}
