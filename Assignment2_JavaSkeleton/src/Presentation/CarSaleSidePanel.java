package Presentation;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;

import Business.CarSale;

/**
 * 
 * Represents instruction list panel of instruction tracker that includes
 * both the search box/button and text field; AND the instruction list
 *
 */
public class CarSaleSidePanel extends JPanel{

	private static final long serialVersionUID = 2693528613703066603L;

	private CarSaleListPanel aListPanel;
	
	/**
	 * Represents the left panel of carSale tracker that includes
	 * both the search box/button and text field; AND the carSale list
	 * 
	 * @param searchEventListener : used to retrieve user search query in search box
	 * @param listPanel : carSale list panel
	 */
	public CarSaleSidePanel(ISearchCarSaleListener searchEventListener, CarSaleListPanel listPanel)
	{
		aListPanel = listPanel;
		CarSaleSearchComponents searchComponents = new CarSaleSearchComponents(searchEventListener);
	
		add(searchComponents);
		add(listPanel);
		
		BorderLayout layout = new BorderLayout();
		layout.addLayoutComponent(searchComponents, BorderLayout.NORTH);
		layout.addLayoutComponent(listPanel, BorderLayout.CENTER);
		setLayout(layout);
	}
	
	public void refresh(Vector<CarSale> cs)
	{
		aListPanel.refresh(cs);
	}
	
	public void registerCarSaleSelectionNotifiableObject(ICarSaleSelectionNotifiable notifiable)
	{
		aListPanel.registerCarSaleSelectionNotifiableObject(notifiable);
	}
}
