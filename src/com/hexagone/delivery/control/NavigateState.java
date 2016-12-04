package com.hexagone.delivery.control;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.models.ArrivalPoint;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.Route;
import com.hexagone.delivery.models.Warehouse;
import com.hexagone.delivery.ui.MainFrame;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;

public class NavigateState implements ControllerActions {

	private MainFrame frame;

	private int step;

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


		HashMap<Integer, ArrivalPoint> tour = route.getRoute();
		Set<Entry<Integer, ArrivalPoint>> entrySet= tour.entrySet();
		Iterator<Entry<Integer, ArrivalPoint>> iterator= entrySet.iterator();
		for (int i=0;i<step+1 && i<tour.size();i++) {

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
			if(i == step){
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

		}// TODO Auto-generated method stub

	}
	
	public void startTour(){
		step = 0;
		frame.repaint();
	}
	
	public void nextDelivery(int maxValue){
		step ++;
		if(step > maxValue){
			step = maxValue;
		}
			
		frame.repaint();
	}
	
	public void previousDelivery(){
		if(step > 0){
			step --;
		}
		
		frame.repaint();
	}
	
	
	public NavigateState(MainFrame frame){
		this.frame = frame;
	}

}
