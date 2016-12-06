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
	 * @return a Map object
	 */
	public Map loadMap();
	
	/**
	 * Loads a delivery query
	 * @return a DeliveryQuery object
	 */
	public DeliveryQuery loadDeliveryQuery();
	
	/**
	 * Computes a delivery
	 * @return a DeliveryQuery solving the Map / DeliveryQuery selected by the user
	 */
	public RouteHelper computeDelivery(Map map, DeliveryQuery delivery);
	
	/**
	 * Draws a map
	 */
	public void DrawMap(Graphics g, float scale, Map m, DeliveryQuery delivery, RouteHelper routeHelper);
	public void generatePlanning(RouteHelper routeHelper);
	
	
}
