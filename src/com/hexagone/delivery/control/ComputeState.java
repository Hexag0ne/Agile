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

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.Route;
import com.hexagone.delivery.models.Warehouse;
import com.hexagone.delivery.ui.Popup;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;

public class ComputeState implements ControllerActions {

	@Override
	public Map loadMap() {
		try {
			return XMLDeserialiser.loadMap();
		} catch (XMLException e) {
			Popup.showInformation("Le fichier choisi n'est pas un plan valide.");
			return null;
		}
	}

	@Override
	public DeliveryQuery loadDeliveryQuery() {
		try {
			return XMLDeserialiser.loadDeliveryQuery();
		} catch (XMLException e) {
			Popup.showInformation("Le fichier choisi n'est pas une livraison valide.");
			return null;
		}
	}

	@Override
	public Route computeDelivery(Map map, DeliveryQuery delivery) {
		DeliveryComputer computer = new DeliveryComputer(map, delivery);
		computer.getDeliveryPoints();
		
		return new Route(map, delivery, computer);
	}

	/**
	 * In the ComputeState, the map and the deliveryQuery are known. Hence we draw the map and the points of delivery
	 * on top of it
	 */
	@Override
	public void DrawMap(Graphics g, float scale, Map map, DeliveryQuery deliveryQuery, Route route) {
		
		//Painting the map
		//Painting the roads first
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
				Line2D lin = new Line2D.Float(((origine.x) / scale) + 5, ((origine.y) / scale) + 5,
						((destination.x) / scale) + 5, ((destination.y) / scale) + 5);
				g2.setStroke(new BasicStroke(2));
				g2.draw(lin);
			}
		}
		//Painting the  of the map
		for (Intersection i : intersections) {
			Point p = new Point();
			p = i.getCoordinates();
			g.setColor(Color.BLUE);
			g.fillOval((int)(((p.x)) / scale),(int) (((p.y)) / scale), 10, 10);
		}
		
		
		//Drawing the deliveryQuery
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
		g.fillOval((int)(((pointWarehouse.x)) / scale),(int)( ((pointWarehouse.y)) / scale), 15, 15);
		g.drawString("Entrepôt",(int)( ((pointWarehouse.x)) / scale + 5), (int)(((pointWarehouse.y)) / scale));

		// Draw Delivery points
		g.setColor(new Color(20, 200, 20));
		for (Delivery d : deliveries) {
			Intersection i = d.getIntersection();
			Point pointDelivery = new Point();
			for (Intersection in : intersections) {
				if ((in.getId()).equals(i.getId())) {
					pointDelivery = in.getCoordinates();
					break;
				}
			}
			g.fillOval((int)(((pointDelivery.x)) / scale),(int)( ((pointDelivery.y)) / scale), 10, 10);

		}
	}

}
