package com.hexagone.delivery.control;

import java.awt.Graphics;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Route;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;

public class RouteViewState implements ControllerActions {

	@Override
	public Map loadMap() {
		try {
			return XMLDeserialiser.loadMap();
		} catch (XMLException e) {
			//TODO Exception popup for the user ?
			return null;
		}
	}

	@Override
	public DeliveryQuery loadDeliveryQuery() {
		try {
			return XMLDeserialiser.loadDeliveryQuery();
		} catch (XMLException e) {
			//TODO Exception popup for the user ?
			return null;
		}
	}

	@Override
	public Route computeDelivery(Map map, DeliveryQuery delivery) {
		DeliveryComputer computer = new DeliveryComputer(map, delivery);
		computer.getDeliveryPoints();
		
		return new Route(map, delivery, computer);
	}

	@Override
	public void DrawMap(Graphics g, float scale, Map m, DeliveryQuery delivery) {
		// TODO Auto-generated method stub

	}

}
