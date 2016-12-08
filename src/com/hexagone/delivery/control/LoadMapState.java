package com.hexagone.delivery.control;

import java.awt.Graphics;

import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.RouteHelper;
import com.hexagone.delivery.ui.Popup;
import com.hexagone.delivery.xml.NoFileChosenException;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;

/**
 * This class allows us to draw the map and the points of the delivery on top of
 * it when the state is LOADMAP_STATE
 */
public class LoadMapState implements ControllerActions {

	/**
	 * Opens a FileChooser that lets the user pick an XML file on the file
	 * system.
	 */
	@Override
	public Map loadMap() {
		try {
			return XMLDeserialiser.loadMap();
		} catch (XMLException e) {
			Popup.showInformation("Le fichier choisi n'est pas un plan valide.");
			return null;
		} catch (NoFileChosenException e) {
			return null;
		}
	}

	/**
	 * Returns null, one cannot load a deliveryQuery before loading a Map
	 */
	@Override
	public DeliveryQuery loadDeliveryQuery() {
		return null;
	}

	/**
	 * Returns null. It shouldn't be called in the InitState
	 */
	@Override
	public RouteHelper computeDelivery(Map map, DeliveryQuery delivery) {
		return null;
	}

	@Override
	public void generatePlanning(RouteHelper routeHelper) {
		Popup.showError("Veuillez calculez la tournée", "Erreur");
	}

	/**
	 * In the loadMap state, the map isn't present. Therefore this method
	 * doesn't draw anything
	 */
	@Override
	public void DrawMap(Graphics g, float scale, Map m, DeliveryQuery delivery, RouteHelper routeHelper) {
		// No action
	}

}
