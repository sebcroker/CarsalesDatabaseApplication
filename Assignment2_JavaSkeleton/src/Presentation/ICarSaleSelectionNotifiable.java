package Presentation;

import Business.CarSale;

/**
 *
 * 
 * Used to notify any interested object that implements this interface
 * and registers with InstructionListPanel of an InstructionSelection
 *
 */
public interface ICarSaleSelectionNotifiable {
	public void carSaleSelected(CarSale carSale);
}
