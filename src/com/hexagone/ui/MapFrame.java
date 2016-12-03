package com.hexagone.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JPanel;

import com.hexagone.delivery.models.ArrivalPoint;
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
	private static LinkedHashMap<Integer, ArrivalPoint> tour;
	// Shrink coefficient
	private static float coefficient;

	MapFrame(Map map, DeliveryQuery deliveryQuery, Boolean calculateflag, float coefficient,
			LinkedHashMap<Integer, ArrivalPoint> tour) {
		super();
		this.map = map;
		this.deliveryQuery = deliveryQuery;
		this.calculateflag = calculateflag;
		this.coefficient = coefficient;
		this.tour = tour;
		FlowLayout fl = new FlowLayout();
		setLayout(fl);



	}
	@Override
	public Dimension getPreferredSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return new Dimension(screenSize.width, screenSize.height+10);
	}

	public void paint(Graphics g) {
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
		}
		if (MapFrame.calculateflag == true) {
			Warehouse warehouse = deliveryQuery.getWarehouse();
			Intersection intersectionWarehouse = warehouse.getIntersection();
			Point pointWarehouse = new Point();
			for (Intersection in : intersections) {
				if ((in.getId()).equals(intersectionWarehouse.getId())) {
					pointWarehouse = in.getCoordinates();
					break;
				}
			}
			g.setColor(Color.RED);
			g.fillOval((int)(((pointWarehouse.x)) / coefficient),(int)( ((pointWarehouse.y)) / coefficient), 15, 15);
			g.drawString("Entrepôt",(int)( ((pointWarehouse.x)) / coefficient + 5), (int)(((pointWarehouse.y)) / coefficient));
			for (Entry<Integer, ArrivalPoint> entry : tour.entrySet()) {

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

}
