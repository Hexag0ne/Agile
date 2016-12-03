package com.hexagone.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JPanel;

import com.hexagone.delivery.models.ArrivalPoint;
import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.Warehouse;

public class MapTour extends JPanel {

	private static Map map;
	private static DeliveryQuery deliveryQuery;
	private static LinkedHashMap<Integer, ArrivalPoint> tour;
	private static float coefficient;
	private static int deliveryPoint;

	public MapTour(Map map, DeliveryQuery deliveryQuery,float coefficient,LinkedHashMap<Integer, ArrivalPoint> tour, int deliveryPoint){

		super();
		this.map=map;
		this.deliveryQuery=deliveryQuery;
		this.tour=tour;
		this.coefficient=coefficient;
		this.deliveryPoint=deliveryPoint;
		FlowLayout fl = new FlowLayout();
		setLayout(fl);
		setBackground(Color.WHITE);
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
				g2.draw(lin);
			}
		}



		Warehouse warehouse = deliveryQuery.getWarehouse();
		Intersection intersectionWarehouse = warehouse.getIntersection();
		Point pointWarehouse = new Point();
		for (Intersection in : intersections) {
			if ((in.getId()).equals(intersectionWarehouse.getId())) {
				pointWarehouse = in.getCoordinates();
				break;
			}
		}


		Set<Entry<Integer, ArrivalPoint>> entrySet= tour.entrySet();
		Iterator<Entry<Integer, ArrivalPoint>> iterator= entrySet.iterator();
		for (int i=0;i<deliveryPoint+1 && i<tour.size();i++) {

			Entry<Integer, ArrivalPoint> entry= iterator.next();
			Point pointDelivery = new Point();

			for (Entry<Integer, ArrivalPoint> entryALLMAP : tour.entrySet()) {

				ArrayList<Road> roadsToNextDP = entryALLMAP.getValue().getRoads();
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
            
			ArrayList<Road> roadsToNextDP = entry.getValue().getRoads();
			for (Road r : roadsToNextDP) {
				g.setColor(Color.blue);
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
				g2.setStroke(new BasicStroke(3));
				g2.draw(lin);
			}


			for (Intersection in : intersections) {
				Point p = new Point();
				p = in.getCoordinates();
				g.setColor(Color.BLUE);
				g.fillOval((int)(((p.x)) / coefficient),(int)( ((p.y)) / coefficient), 10, 10);
			}
			if(i == deliveryPoint){
				g.setColor(new Color(0, 102, 0));
				for (Intersection in : intersections) {
					if ((in.getId()).equals(entry.getKey())) {
						pointDelivery = in.getCoordinates();
						break;
					}
				}
				g.fillOval((int)(((pointDelivery.x)) / coefficient),(int)( ((pointDelivery.y)) / coefficient), 20, 20);
			}



			g.setColor(Color.RED);
			g.fillOval((int)(((pointWarehouse.x)) / coefficient),(int)( ((pointWarehouse.y)) / coefficient), 15, 15);
			g.drawString("Entrepôt",(int)( ((pointWarehouse.x)) / coefficient + 5), (int)(((pointWarehouse.y)) / coefficient));

		}


	}

}
