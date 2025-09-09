package Presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Business.CarSale;

/**
 * Panel encapsulating carSale list
 *
 */
public class CarSaleListPanel extends JPanel{

	private static final long serialVersionUID = 1013855025757989473L;
	
	private List<ICarSaleSelectionNotifiable> notifiables = new ArrayList<ICarSaleSelectionNotifiable>();
	private Vector<CarSale> carSales;
	
	/**
	 * 
	 * @param carSales - vector of carSales to display in the carSale list panel
	 */
	public CarSaleListPanel(Vector<CarSale> carSales)
	{
		this.carSales = carSales;
		this.setBorder(BorderFactory.createLineBorder(Color.black));	
		initList(this.carSales);
	}

	private void initList(Vector<CarSale> carSales) {
		
		final JList<CarSale> list = new JList<CarSale>(carSales);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		JScrollPane listScroller = new JScrollPane(list);
		this.add(listScroller);
		
		BorderLayout listLayout = new BorderLayout();
		listLayout.addLayoutComponent(listScroller, BorderLayout.CENTER);
		this.setLayout(listLayout);
		list.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e) {
				for(ICarSaleSelectionNotifiable notifiable : notifiables)
				{
					CarSale selectedCarSale = list.getSelectedValue();
					if(selectedCarSale != null)
					{
						notifiable.carSaleSelected(selectedCarSale);
					}
				}
			}
		});
	}
	
	/**
	 * Refresh carSale list to display vector of carSale objects
	 * @param carSales - vector of carSale objects to display
	 */
	public void refresh(Vector<CarSale> carSales)
	{
		this.removeAll();
		this.initList(carSales);
		this.updateUI();
		this.notifiables.clear();
	}
	
	/**
	 * Register an object to be notified of a carSale selection change
	 * @param notifiable object to invoke when a new carSale is selected
	 */
	public void registerCarSaleSelectionNotifiableObject(ICarSaleSelectionNotifiable notifiable)
	{
		notifiables.add(notifiable);
	}
}
