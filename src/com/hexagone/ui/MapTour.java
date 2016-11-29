package com.hexagone.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JPanel;

import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.Warehouse;

public class MapTour extends JPanel {

	private static Map map;
	private static DeliveryQuery deliveryQuery;
	private static LinkedHashMap<Integer, ArrayList<Road>> tour;
	private static int coefficient;
	private static int deliveryPoint;

	public MapTour(Map map, DeliveryQuery deliveryQuery,int coefficient,LinkedHashMap<Integer, ArrayList<Road>> tour, int deliveryPoint){

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

	public void paint(Graphics g) {

		ArrayList<Intersection> intersections = new ArrayList<Intersection>();
		intersections = map.getIntersections();
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
		
		Set<Entry<Integer, ArrayList<Road>>> entrySet= tour.entrySet();
		Iterator<Entry<Integer, ArrayList<Road>>> iterator= entrySet.iterator();
		for (int i=0;i<deliveryPoint+1 && i<tour.size();i++) {
			    
				g.setColor(Color.ORANGE);
				Entry<Integer, ArrayList<Road>> entry= iterator.next();
				Point pointDelivery = new Point();
				for (Intersection in : intersections) {
					if ((in.getId()).equals(entry.getKey())) {
						pointDelivery = in.getCoordinates();
						break;
					}
				}
				g.fillOval(((pointDelivery.x)) / coefficient, ((pointDelivery.y)) / coefficient, 10, 10);


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
