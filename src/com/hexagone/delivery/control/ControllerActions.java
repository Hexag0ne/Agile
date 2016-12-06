package com.hexagone.delivery.control;

import java.awt.Graphics;

import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.RouteHelper;

public interface ControllerActions {
	
	/**
	 * @return a Map object
	 */
	public Map loadMap();
	
	/**
	 * @return a DeliveryQuery object
	 */
	public DeliveryQuery loadDeliveryQuery();
	
	/**
	 * @return a DeliveryQuery solving the Map / DeliveryQuery selected by the user
	 */
	public RouteHelper computeDelivery(Map map, DeliveryQuery delivery);
	
	public void generatePlanning(RouteHelper routeHelper);
	
	public void DrawMap(Graphics g, float scale, Map m, DeliveryQuery delivery, RouteHelper routeHelper);
	
}
