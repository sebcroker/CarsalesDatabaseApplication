package Presentation;


/**
 *
 *
 * Used to notify any interested object that implements this interface
 * and is used to construct PatientSearchComponents of a search that was issued
 * 
 */
public interface ISearchCarSaleListener {
	public void searchClicked(String searchString);
}
