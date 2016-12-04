package com.hexagone.delivery.control;

import java.awt.Graphics;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;

public class LoadMapState implements ControllerActions {

	/**
	 * Opens a FileChooser that lets the user pick an XML file on the file system.
	 */
	@Override
	public Map loadMap() {
		try {
			return XMLDeserialiser.loadMap();
		} catch (XMLException e) {
			//TODO Exception popup for the user ?
			return null;
		}
	}

	/**
	 * Return null, one cannot load a deliveryQuery before loading a Map
	 */
	@Override
	public DeliveryQuery loadDeliveryQuery() {
		return null;
	}

	/**
	 * Returns null. It shouldn't be called in the InitState
	 */
	@Override
	public DeliveryComputer computeDelivery(Map map, DeliveryQuery delivery) {
		return null;
	}

	/**
	 * In the loadMap state, the map isn't present. Therefore this method doesn't draw anything
	 */
	@Override
	public void DrawMap(Graphics g, float scale, Map m, DeliveryQuery delivery) {
		//No action
	}

}
