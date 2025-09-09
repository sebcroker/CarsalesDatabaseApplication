package Presentation;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import Business.BusinessComponentFactory;
import Business.CarSale;
import Business.Summary;
import Business.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CarSaleTrackerFrame extends JFrame {

	private static final long serialVersionUID = 5532618722097754725L;
	
	private AddEntitiesPanel addEntitiesPanel = null;
	private CarSaleDetailPanel detailPanel = null;
	private CarSaleSidePanel sidePanel = null;
	private CardLayout cardLayout = null;
	private CarSaleSummaryPane summaryPane = null;
	
	static String loggedInUsername = null;
	static String loggedInUserFullName = null;
	/**
	 * Main carSale tracker frame
	 * Logs on the user
	 * Initialize side panel + add entities panel + containing event list + detail panel
	 */
	public CarSaleTrackerFrame()
	{
		setTitle(StringResources.getAppTitle());
	    setLocationRelativeTo(null);
	    
	    logOnUser();
	    initialise();
	    
	    setDefaultCloseOperation(EXIT_ON_CLOSE);  
	}
	
	/**
	 *  !!! 
	 *  Only used to simulate logon
	 *  This should really be implemented using proper salted hashing
	 *	and compare hash to that in DB
	 *  really should display an error message for bad login as well
	 *	!!!
	 */
	private void logOnUser() {
		boolean OK = false;
		while (!OK) {		
				String userName = (String)JOptionPane.showInputDialog(
									this,
									null,
									StringResources.getEnterUserNameString(),
									JOptionPane.QUESTION_MESSAGE);
				
				JPasswordField jpf = new JPasswordField();
				int okCancel = JOptionPane.showConfirmDialog(
									null,
									jpf,
									StringResources.getEnterPasswordString(),
									JOptionPane.OK_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE);
				
				String password = null;
				if (okCancel == JOptionPane.OK_OPTION) {
					password = new String(jpf.getPassword());
				}

				if (userName == null || password == null)
					System.exit(0);
				else
					if (!userName.isEmpty() && !password.isEmpty()) {
						User user = checkUserCredentials(userName, password);
						loggedInUsername = (user != null) ? user.getUserName() : null; //Get logged in userName
						if (loggedInUsername != null) {
							OK = true;
							loggedInUserFullName = user.getFirstName() + " " + user.getLastName();
						} else {
							System.out.println("Password authentication failed for user: " + userName);
						}
					}
		}
	}

	private void initialise()
	{	addEntitiesPanel = new AddEntitiesPanel();
	    detailPanel = new CarSaleDetailPanel(true);
	    sidePanel = getSidePanel(new CarSaleListPanel(findCarSalesByCriteria(loggedInUserFullName)));

		final CarSaleTrackerFrame frame = this;

		cardLayout = new CardLayout();
		final JPanel mainPanel = new JPanel(cardLayout);
		summaryPane = new CarSaleSummaryPane(getAllCarSalesSummary());

		JPanel carSalePanel = new JPanel(new BorderLayout());
		carSalePanel.add(sidePanel,BorderLayout.WEST);
		carSalePanel.add(detailPanel,BorderLayout.CENTER);

		mainPanel.add(summaryPane,"Summary");
		mainPanel.add(carSalePanel,"CarSale");

		BorderLayout borderLayout = new BorderLayout();
	    borderLayout.addLayoutComponent(addEntitiesPanel, BorderLayout.NORTH);
		borderLayout.addLayoutComponent(mainPanel, BorderLayout.CENTER);
	    
		// Create buttons to switch panels
		JPanel buttonPanel = new JPanel();
		JButton summaryButton = new JButton("Summary");
		JButton carSalesButton = new JButton("Car Sales");
		
		// JButton refreshButton = new JButton(StringResources.getRefreshButtonLabel());
		// buttonPanel.add(refreshButton);
		// borderLayout.addLayoutComponent(refreshButton, BorderLayout.SOUTH);

		buttonPanel.add(summaryButton);
		buttonPanel.add(carSalesButton);
		
		// Button actions to switch panels
		carSalesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String searchString = summaryPane.getClickedRow() != null ? summaryPane.getClickedRow() : loggedInUserFullName;
				frame.refresh(frame.findCarSalesByCriteria(searchString));
				cardLayout.show(mainPanel, "CarSale");
			}
		});

		summaryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.refreshSummary(frame.getAllCarSalesSummary());
				cardLayout.show(mainPanel, "Summary");
			}
		});

		borderLayout.addLayoutComponent(buttonPanel, BorderLayout.SOUTH);
		cardLayout.show(mainPanel, "Summary");

	    this.setLayout(borderLayout);
	    this.add(addEntitiesPanel);
	    this.add(buttonPanel);
		this.add(mainPanel);
	    this.setSize(800, 500);
	}
	
	private CarSaleSidePanel getSidePanel(CarSaleListPanel listPanel)
	{
		final CarSaleTrackerFrame frame = this;
		listPanel.registerCarSaleSelectionNotifiableObject(detailPanel);
		return new CarSaleSidePanel(new ISearchCarSaleListener() {
			@Override
			public void searchClicked(String searchString) {
				frame.refresh(frame.findCarSalesByCriteria(searchString));
			}
		},listPanel);
	}

	private User checkUserCredentials(String userName, String password)
	{
		return BusinessComponentFactory.getInstance().getCarSaleProvider().checkLogin(userName, password);
	}
	
	private Vector<Summary> getAllCarSalesSummary()
	{
		return BusinessComponentFactory.getInstance().getCarSaleProvider().getCarSalesSummary();
	}
	
	private Vector<CarSale> findCarSalesByCriteria(String pSearchString)
	{
		pSearchString = pSearchString.trim();
		if (!pSearchString.equals(""))
			return BusinessComponentFactory.getInstance().getCarSaleProvider().findCarSales(pSearchString);
		else
			return BusinessComponentFactory.getInstance().getCarSaleProvider().findCarSales(loggedInUserFullName);
	}

	private void refresh(Vector<CarSale> carSales)
	{
		if(sidePanel != null && detailPanel!= null)
		{
			sidePanel.refresh(carSales);
			detailPanel.refresh();
			sidePanel.registerCarSaleSelectionNotifiableObject(detailPanel);
		}
	}

	private void refreshSummary(Vector<Summary> summaryList)
	{
		if(summaryPane != null)
		{
			summaryPane.refresh(summaryList);
		}
	}

	public static void main(String[] args)
	{
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	CarSaleTrackerFrame ex = new CarSaleTrackerFrame();
                ex.setVisible(true);
            }
        });		
	}
}
