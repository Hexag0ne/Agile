package com.hexagone.delivery.control;

import java.awt.Graphics;

import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.RouteHelper;

/**
 * This class is an interface of control actions for the drawing of the map
 *
 */
public interface ControllerActions {

	/**
	 * Loads a map
	 * 
	 * @return a Map object
	 */
	public Map loadMap();

	/**
	 * Loads a delivery query
	 * 
	 * @return a DeliveryQuery object
	 */
	public DeliveryQuery loadDeliveryQuery();

	/**
	 * Computes a delivery with the given map and delivery.
	 * @param map the map on which the problem takes place
	 * @param delivery the deliveries to make
	 * @return a RouteHelper object containing the solution to the problem
	 */
	public RouteHelper computeDelivery(Map map, DeliveryQuery delivery);

	/**
	 * Draws the map with the 'g' Graphics parameter
	 * @param g the paint brush to draw the map with
	 * @param scale the scale at which the map is to be drawn
	 * @param m the map to be drawn
	 * @param delivery the deliveryQuery to be done on the map
	 * @param routeHelper the routeHelper in case a solution to the deliveries has been found
	 */
	public void DrawMap(Graphics g, float scale, Map m, DeliveryQuery delivery, RouteHelper routeHelper);

	/**
	 * Generates the planning of the delivery
	 * @param routeHelper the object that contains all the informations needed to compute the planning.
	 */
	public void generatePlanning(RouteHelper routeHelper);

}
