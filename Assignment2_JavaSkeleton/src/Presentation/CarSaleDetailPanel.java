package Presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Business.BusinessComponentFactory;
import Business.CarSale;


/**
 * Panel used for creating and updating carSales
 */
public class CarSaleDetailPanel extends JPanel implements ICarSaleSelectionNotifiable {

	private static final long serialVersionUID = 2031054367491790942L;

	private CarSale currentCarSale = null;
	private boolean isUpdatePanel = true;

	private JTextField CarSaleIdField;
	private JTextField makeField;
	private JTextField modelField;
	private JTextField priceField;
	private JTextField saleDateField;
	private JTextField buyerField;
	private JTextField salespersonField;
	private JTextField builtYearField;
	private JTextField odometerField;
	private JTextField isSoldField;
	
	/**
	 * Panel used for creating and updating carSales
	 * @param isUpdatePanel : describes whether panel will be used to either create or update carSale
	 */
	public CarSaleDetailPanel(boolean isUpdatePanel)
	{
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.isUpdatePanel = isUpdatePanel;
	}

	/**
	 * Re-populates panel details with given carSale object
	 * @param carsale new carSale object to populate panel details with
	 */
	public void initCarSaleDetails(CarSale carsale) {
		removeAll();	
		if(carsale != null)
		{
			currentCarSale = carsale;
			addAll();
		}
	}

	private void makeFieldsUneditable(JTextField field) {
		field.setEditable(false);
		field.setBackground(Color.LIGHT_GRAY);
		field.setOpaque(true);
	}
	private void addAll() {
		JPanel lTextFieldPanel = new JPanel();
		BoxLayout lTextFieldLayout = new BoxLayout(lTextFieldPanel, BoxLayout.Y_AXIS);
		lTextFieldPanel.setLayout(lTextFieldLayout);

		BorderLayout lPanelLayout = new BorderLayout();	
		lPanelLayout.addLayoutComponent(lTextFieldPanel, BorderLayout.NORTH);

		//create carSale text fields
		//application convention is to map null to empty string (if db has null this will be shown as empty string)
		if(currentCarSale.getCarSaleId() > 0) {
			CarSaleIdField = createLabelTextFieldPair(StringResources.getCarSaleIdLabel(), ""+ currentCarSale.getCarSaleId(), lTextFieldPanel);
			makeFieldsUneditable(CarSaleIdField);
		}

		if(isUpdatePanel) { // Update panel
			makeField = createLabelTextFieldPair(StringResources.getMakeLabel(), currentCarSale.getMake(), lTextFieldPanel);
			makeFieldsUneditable(makeField);
			modelField = createLabelTextFieldPair(StringResources.getModelLabel(), currentCarSale.getModel(), lTextFieldPanel);
			makeFieldsUneditable(modelField);
			builtYearField = createLabelTextFieldPair(StringResources.getBuiltYearLabel(), ""+currentCarSale.getBuiltYear(), lTextFieldPanel);
			makeFieldsUneditable(builtYearField);
			odometerField = createLabelTextFieldPair(StringResources.getOdometerLabel(), ""+currentCarSale.getOdometer(), lTextFieldPanel);
			makeFieldsUneditable(odometerField);
			priceField = createLabelTextFieldPair(StringResources.getPriceLabel(), ""+currentCarSale.getPrice(), lTextFieldPanel);
			makeFieldsUneditable(priceField);
			isSoldField = createLabelTextFieldPair(StringResources.getIsSoldLabel(), ""+currentCarSale.getIsSold(), lTextFieldPanel);
			makeFieldsUneditable(isSoldField);
			buyerField = createLabelTextFieldPair(StringResources.getBuyerLabel(), currentCarSale.getBuyer() == null ? "" : ""+ currentCarSale.getBuyer(), lTextFieldPanel);
			salespersonField = createLabelTextFieldPair(StringResources.getSalespersonLabel(), currentCarSale.getSalesperson() == null ? "" : ""+ currentCarSale.getSalesperson(), lTextFieldPanel);
			saleDateField = createLabelTextFieldPair(StringResources.getSaleDateLabel(), currentCarSale.getSaleDateString() == null ? "" : ""+ currentCarSale.getSaleDateString(), lTextFieldPanel);

		} else { // Add panel
			makeField = createLabelTextFieldPair(StringResources.getMakeLabel(), "", lTextFieldPanel);
			modelField = createLabelTextFieldPair(StringResources.getModelLabel(), "", lTextFieldPanel);
			builtYearField = createLabelTextFieldPair(StringResources.getBuiltYearLabel(), "", lTextFieldPanel);
			odometerField = createLabelTextFieldPair(StringResources.getOdometerLabel(), "", lTextFieldPanel);
			priceField = createLabelTextFieldPair(StringResources.getPriceLabel(), "", lTextFieldPanel);
		}

		add(lTextFieldPanel);

		JButton saveButton = createCarSaleSaveButton();
		lPanelLayout.addLayoutComponent(saveButton, BorderLayout.SOUTH);
		this.add(saveButton);

		setLayout(lPanelLayout);
		updateUI();
	}

	private JButton createCarSaleSaveButton() {
		JButton saveButton = new JButton(isUpdatePanel ? StringResources.getCarSaleUpdateButtonLabel() :
			StringResources.getCarSaleAddButtonLabel());
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//application convention is to map empty string to null (ie. if app has empty string - this will be null in db)
				if(isUpdatePanel) {
					currentCarSale.setBuyer(buyerField.getText().equals("") ? null : buyerField.getText());
					currentCarSale.setSalesperson(salespersonField.getText().equals("") ? null : salespersonField.getText());
					currentCarSale.setSaleDate(saleDateField.getText().equals("") ? null : saleDateField.getText());

					BusinessComponentFactory.getInstance().getCarSaleProvider().updateCarSale(currentCarSale);
				} else {
					currentCarSale.setMake(makeField.getText().equals("") ? null : makeField.getText());
					currentCarSale.setModel(modelField.getText().equals("") ? null : modelField.getText());
					currentCarSale.setBuiltYear(builtYearField.getText().equals("") ? null : Integer.valueOf(builtYearField.getText()));
					currentCarSale.setOdometer(odometerField.getText().equals("") ? null : Integer.valueOf(odometerField.getText()));
					currentCarSale.setPrice(priceField.getText().equals("") ? null : new BigDecimal(priceField.getText()));

					BusinessComponentFactory.getInstance().getCarSaleProvider().addCarSale(currentCarSale);
				}
			}
		});
		
		return saveButton;
	}

	private JTextField createLabelTextFieldPair(String label, String value, JPanel container) {
		
		JPanel pairPanel = new JPanel();
		JLabel jlabel = new JLabel(label);
		JTextField field = new JTextField(value);
		pairPanel.add(jlabel);
		pairPanel.add(field);

		container.add(pairPanel);

		BorderLayout lPairLayout = new BorderLayout();
		lPairLayout.addLayoutComponent(jlabel, BorderLayout.WEST);
		lPairLayout.addLayoutComponent(field, BorderLayout.CENTER);
		pairPanel.setLayout(lPairLayout);	
		
		return field;
	}

	/**
	 * Implementation of ICarSaleSelectionNotifiable::CarSaleSelected used to switch carSale
	 * displayed on CarSaleDisplayPanel
	 */
	@Override
	public void carSaleSelected(CarSale carSale) {
		initCarSaleDetails(carSale);
	}
	
	/**
	 * Clear carSale details panel
	 */
	public void refresh()
	{
		initCarSaleDetails(null);
	}
}
