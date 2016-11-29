package com.hexagone.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JPanel;

import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.Warehouse;

public class MapFrame extends JPanel {

	private static Map map;
	private static DeliveryQuery deliveryQuery;
	private static Boolean calculateflag;
	private static LinkedHashMap<Integer, ArrayList<Road>> tour;
	// Shrink coefficient
	private static int coefficient = 1;

	MapFrame(Map map, DeliveryQuery deliveryQuery, Boolean calculateflag, int coefficient,
			LinkedHashMap<Integer, ArrayList<Road>> tour) {
		super();
		this.map = map;
		this.deliveryQuery = deliveryQuery;
		this.calculateflag = calculateflag;
		this.coefficient = coefficient;
		this.tour = tour;
		FlowLayout fl = new FlowLayout();
		setLayout(fl);
		setBackground(Color.WHITE);
	}

	public void paint(Graphics g) {
		ArrayList<Intersection> intersections = (ArrayList<Intersection>) map.getIntersections().values();
		Set<Integer> roads = new HashSet<Integer>();
		roads = (map.getRoads()).keySet();

		for (Intersection i : intersections) {
			Point p = new Point();
			p = i.getCoordinates();
			g.setColor(Color.BLUE);
			g.fillOval(((p.x)) / coefficient, ((p.y)) / coefficient, 10, 10);
		}
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
				g2.draw(lin);
			}
		}

		// Draw Delivery
		if ((deliveryQuery) != null) {
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
			g.fillOval(((pointWarehouse.x)) / coefficient, ((pointWarehouse.y)) / coefficient, 10, 10);
			g.drawString("Entrepôt", ((pointWarehouse.x)) / coefficient + 5, ((pointWarehouse.y)) / coefficient);

			// Draw Delivery points
			g.setColor(Color.GREEN);
			for (Delivery d : deliveries) {
				Intersection i = d.getIntersection();
				Point pointDelivery = new Point();
				for (Intersection in : intersections) {
					if ((in.getId()).equals(i.getId())) {
						pointDelivery = in.getCoordinates();
						break;
					}
				}
				g.fillOval(((pointDelivery.x)) / coefficient, ((pointDelivery.y)) / coefficient, 10, 10);

			}

		}

		if (MapFrame.calculateflag == true) {

			for (Entry<Integer, ArrayList<Road>> entry : tour.entrySet()) {

				ArrayList<Road> roadsToNextDP = entry.getValue();
				for (Road r : roadsToNextDP) {
					g.setColor(Color.YELLOW);
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
					g2.draw(lin);
				}

			}
		}

	}

}
