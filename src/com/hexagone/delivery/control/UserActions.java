package com.hexagone.delivery.control;

/**
 * This class is an interface of user actions
 *
 */
public interface UserActions {

	/**
	 * Method launched when the user clicks on the load map button
	 */
	public void loadMapButtonClick();

	/**
	 * Method launched when the user clicks on the load delivery Query button
	 */
	public void loadDeliveryQueryButtonClick();

	/**
	 * Method maunched when the user clicks on the compute route button
	 */
	public void computeRouteButtonClick();

	/**
	 * Method launched when the user clicks on the generate planning button
	 */
	public void generatePlanningButtonClick();

	/**
	 * Method launched when the user navigates forward through the different
	 * steps of the delivery
	 */
	public void nextDelivery();

	/**
	 * Method launched when the user navigates backward through the different
	 * steps of the delivery
	 */
	public void previousDelivery();

	/**
	 * Actions to make when the user looks for a specific delivery point and clicks on the search button
	 * @param idDP the identifier of the delivery point the user wants to look for
	 */
	public void searchDP(int idDP);
	
	/**
	 * This method is invoked when the user presses the delete key to remove a delivery point.
	 */
	public void deleteDP();

	/**
	 * This method is invoked when the user wants to modify a delivery point
	 */
	public void modifyDP();
}
