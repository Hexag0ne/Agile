package com.hexagone.delivery.control;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.models.ArrivalPoint;
import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.Route;
import com.hexagone.delivery.models.Warehouse;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;

/**
 * This class allows us to draw the map and the points of the delivery on top of it
 * when the state is ROUTEVIEW_STATE
 */
public class RouteViewState implements ControllerActions {

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
	 * This method allows to load a delivery query from a XML file
	 */
	@Override
	public DeliveryQuery loadDeliveryQuery() {
		try {
			return XMLDeserialiser.loadDeliveryQuery();
		} catch (XMLException e) {
			//TODO Exception popup for the user ?
			return null;
		}
	}

	/**
	 * This method computes a delivery and returns a Route   
	 * @param map
	 * @param delivery
	 * @return the route computed as a Route Object
	 * 
	 */
	@Override
	public Route computeDelivery(Map map, DeliveryQuery delivery) {
		DeliveryComputer computer = new DeliveryComputer(map, delivery);
		computer.getDeliveryPoints();
		
		return new Route(map, delivery, computer);
	}
	
	/**
	 * This methods draws the map and the points of the delivery on top of it
	 * (as the map and the deliveryQuery are known in the class). 
	 * @param g 
	 * @param scale 
	 * 			: ratio chosen for the drawing of the map
	 * @param map
	 * @param deliveryQuery
	 * @param route
	 */
	@Override
	public void DrawMap(Graphics g, float coefficient, Map map, DeliveryQuery deliveryQuery, Route route) {
		ArrayList<Intersection> intersections = new ArrayList<Intersection>(map.getIntersections().values());
		Set<Integer> roads = new HashSet<Integer>();
		roads = (map.getRoads()).keySet();

		for (int j : roads) {
			ArrayList<Road> roadsFromI = new ArrayList<Road>();
			roadsFromI = map.getRoads().get(j);
			for (Road r : roadsFromI) {
				g.setColor(Color.BLACK);
				Graphics2D g2 = (Graphics2D) g;
				Point destination = null;
				Point origine = null;
				for (Intersection in : intersections) {
					if ((in.getId()).equals(r.getOrigin())) {
						origine = in.getCoordinates();
						break;
					}
				}
				for (Intersection in : intersections) {
					if ((in.getId()).equals(r.getDestination())) {
						destination = in.getCoordinates();
						break;
					}
				}
				Line2D lin = new Line2D.Float(((origine.x) / coefficient) + 5, ((origine.y) / coefficient) + 5,
						((destination.x) / coefficient) + 5, ((destination.y) / coefficient) + 5);
				g2.setStroke(new BasicStroke(2));
				g2.draw(lin);
			}
			
			//Delivery route computed we display the rods to follow
			for (Entry<Integer, ArrivalPoint> entry : route.getRoute().entrySet()) {

				ArrayList<Road> roadsToNextDP = entry.getValue().getRoads();
				for (Road r : roadsToNextDP) {
					g.setColor(Color.green);
					Graphics2D g2 = (Graphics2D) g;
					Point destination = null;
					Point origine = null;
					for (Intersection in : intersections) {
						if ((in.getId()).equals(r.getOrigin())) {
							origine = in.getCoordinates();
							break;
						}
					}
					for (Intersection in : intersections) {
						if ((in.getId()).equals(r.getDestination())) {
							destination = in.getCoordinates();
							break;
						}
					}
					Line2D lin = new Line2D.Float(((origine.x) / coefficient) + 5, ((origine.y) / coefficient) + 5,
							((destination.x) / coefficient) + 5, ((destination.y) / coefficient) + 5);
					g2.setColor(Color.GREEN);
					g2.setStroke(new BasicStroke(3));
					g2.draw(lin);
				}

			}
		}
		
		for (Intersection i : intersections) {
			Point p = new Point();
			p = i.getCoordinates();
			g.setColor(Color.BLUE);
			g.fillOval((int)(((p.x)) / coefficient),(int) (((p.y)) / coefficient), 10, 10);
		}


		Warehouse warehouse = deliveryQuery.getWarehouse();
		Delivery[] deliveries = deliveryQuery.getDeliveries();

		Intersection intersectionWarehouse = warehouse.getIntersection();
		Point pointWarehouse = new Point();
		for (Intersection in : intersections) {
			if ((in.getId()).equals(intersectionWarehouse.getId())) {
				pointWarehouse = in.getCoordinates();
				break;
			}
		}
		// Draw Warehouse
		g.setColor(Color.RED);
		g.fillOval((int)(((pointWarehouse.x)) / coefficient),(int)( ((pointWarehouse.y)) / coefficient), 15, 15);
		g.drawString("Entrepôt",(int)( ((pointWarehouse.x)) / coefficient + 5), (int)(((pointWarehouse.y)) / coefficient));

		// Draw Delivery points
		g.setColor(new Color(0, 102, 0));
		for (Delivery d : deliveries) {
			Intersection i = d.getIntersection();
			Point pointDelivery = new Point();
			for (Intersection in : intersections) {
				if ((in.getId()).equals(i.getId())) {
					pointDelivery = in.getCoordinates();
					break;
				}
			}
			g.fillOval((int)(((pointDelivery.x)) / coefficient),(int)( ((pointDelivery.y)) / coefficient), 10, 10);
		}
	}

}
