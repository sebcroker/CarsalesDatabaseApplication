package Presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * AddEntitiesPanel is shown at the top of the CarSale tracker window
 * this is where all buttons used to add entities like Event/Instruction/User/CarSale should be added
 * 
 */
public class AddEntitiesPanel extends JPanel {

	private static final long serialVersionUID = 2256207501462485047L;

	private JButton addCarSaleButton = new JButton(StringResources.getCarSaleAddButtonLabel());

	public AddEntitiesPanel()
	{
		setBorder(BorderFactory.createLineBorder(Color.black));
		add(addCarSaleButton);
		updateLayout();
		addCarSaleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddCarSaleDialog dialog = new AddCarSaleDialog();
				dialog.setVisible(true);
			}
		});	
	}

	private void updateLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		layout.addLayoutComponent(addCarSaleButton, BorderLayout.CENTER);
	}
}
