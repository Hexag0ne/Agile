package com.hexagone.delivery.control;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.RouteHelper;
import com.hexagone.delivery.ui.Popup;
import com.hexagone.delivery.xml.NoFileChosenException;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;

/**
 * This class allows us to draw the map and the points of the delivery on top of
 * it when the state is LOADDELIVERY_STATE
 */
public class LoadDeliveryState implements ControllerActions {

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
	 * This method allows to load a delivery query from a XML file
	 */
	@Override
	public DeliveryQuery loadDeliveryQuery() {
		try {
			return XMLDeserialiser.loadDeliveryQuery();
		} catch (XMLException e) {
			Popup.showInformation("Le fichier choisi n'est pas une livraison valide.");
			return null;
		} catch (NoFileChosenException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.hexagone.delivery.control.ControllerActions#computeDelivery(com.hexagone.delivery.models.Map, com.hexagone.delivery.models.DeliveryQuery)
	 */
	@Override
	public RouteHelper computeDelivery(Map map, DeliveryQuery delivery) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.hexagone.delivery.control.ControllerActions#generatePlanning(com.hexagone.delivery.models.RouteHelper)
	 */
	@Override
	public void generatePlanning(RouteHelper routeHelper) {
		JOptionPane.showMessageDialog(null, "Veuillez calculez la tournée.", "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.hexagone.delivery.control.ControllerActions#DrawMap(java.awt.Graphics, float, com.hexagone.delivery.models.Map, com.hexagone.delivery.models.DeliveryQuery, com.hexagone.delivery.models.RouteHelper)
	 */
	@Override
	public void DrawMap(Graphics g, float scale, Map map, DeliveryQuery delivery, RouteHelper routeHelper) {
		ArrayList<Intersection> intersections = new ArrayList<Intersection>(map.getIntersections().values());
		Set<Integer> roads = new HashSet<Integer>();
		roads = (map.getRoads()).keySet();

		for (int j : roads) {
			ArrayList<Road> roadsFromI = new ArrayList<Road>();
			roadsFromI = map.getRoads().get(j);
			for (Road r : roadsFromI) {
				g.setColor(Color.BLACK);
				Graphics2D g2 = (Graphics2D) g;
				Point destination = intersections.get(r.getOrigin()).getCoordinates();
                Point origine = intersections.get(r.getDestination()).getCoordinates();
				Line2D lin = new Line2D.Float(((origine.x) / scale) + 5, ((origine.y) / scale) + 5,
						((destination.x) / scale) + 5, ((destination.y) / scale) + 5);
				// g2.setStroke(new BasicStroke(2));
				g2.draw(lin);
			}
		}

		for (Intersection i : intersections) {
			Point p = i.getCoordinates();
			g.setColor(Color.BLUE);
			g.fillOval((int) (((p.x)) / scale), (int) (((p.y)) / scale), 10, 10);
		}
	}

}
