package com.hexagone.delivery.control;

/**
 * This class is an interface of user actions
 *
 */
public interface UserActions {

	public void loadMapButtonClick();
	
	public void loadDeliveryQueryButtonClick();
	
	public void computeRouteButtonClick();
	
	public void generatePlanningButtonClick();

	public void startNavigationButtonClick();
	
	public void nextDelivery();
	
	public void previousDelivery();
}
