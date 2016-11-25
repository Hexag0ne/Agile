package com.hexagone.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.print.attribute.SetOfIntegerSyntax;
import javax.swing.JPanel;

import com.hexagone.delivery.algo.CompleteGraphComputer;
import com.hexagone.delivery.algo.TSPSolverV1;
import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.Warehouse;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class MapFrame extends JPanel {

	private static Map map;
	private static DeliveryQuery deliveryQuery;
	private static Boolean calculateflag;
	// Shrink coefficient
	private static int coefficient=1;

	MapFrame(Map map, DeliveryQuery deliveryQuery,Boolean calculateflag, int coefficient) {
		super();
		this.map = map;
		this.deliveryQuery = deliveryQuery;
		this.calculateflag=calculateflag;
		this.coefficient=coefficient;
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

		// Draw Delivery
		if ((this.deliveryQuery) != null) {
			Warehouse warehouse = deliveryQuery.getWarehouse();
			Delivery[] deliveries = deliveryQuery.getDelivery();

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

		if(MapFrame.calculateflag == true){
            
			
			Double[][] costsAdjacencyMatrix = CompleteGraphComputer.getAdjacencyMatrix(map, deliveryQuery);

			Delivery[] deliveries = deliveryQuery.getDelivery();
			int lenght = deliveries.length;

			Integer[] stayingTime = new Integer[lenght];
			int i=0;
			for(Delivery d:deliveries){
				stayingTime[i]=d.getDuration();
				i++;
			}

			/*TSPSolverV1 tspSolver = new TSPSolverV1(costsAdjacencyMatrix, stayingTime);
			tspSolver.computeSolution();
			ArrayList<Integer> tour = tspSolver.getBestSolution();
			
			for(int j=0;j < tour.size();j++){
				for(Intersection in: intersections){
					if((in.getId()).equals(tour.get(j))){
						Point p = in.getCoordinates();
						g.setColor(Color.BLACK);
						g.drawString(""+j, ((p.x)+10) / coefficient, ((p.y)+10) / coefficient);
					}
				}
			}*/
			LinkedHashMap<Integer, ArrayList<Road>> tour = new LinkedHashMap<>();
            ArrayList<Road> road21 = new ArrayList<>();
            road21.add(new Road(21, 16));
            road21.add(new Road(16,11));
            road21.add(new Road(11, 12));
            road21.add(new Road(12,13));
			tour.put(21, road21);
			
			ArrayList<Road> road13 = new ArrayList<>();
			road13.add(new Road(13,8));
			road13.add(new Road(8,7));
			road13.add(new Road(7,2));
			road13.add(new Road(2,3));
			road13.add(new Road(3,4));
			road13.add(new Road(4,9));
			tour.put(13,road13);
			
			ArrayList<Road> road9 = new ArrayList<>();
			road9.add(new Road(9,4));
			road9.add(new Road(4,3));
			tour.put(9,road9);
			
			ArrayList<Road> road3 = new ArrayList<>();
			road3.add(new Road(3,2));
			road3.add(new Road(2,1));
			tour.put(3, road3);
			
			ArrayList<Road> road1 = new ArrayList<>();
			road1.add(new Road(1,0));
			road1.add(new Road(0,5));
			road1.add(new Road(5,10));
			road1.add(new Road(10,11));
			road1.add(new Road(11,16));
			road1.add(new Road(16,21));
			tour.put(1, road1);

			for (Entry<Integer, ArrayList<Road>> entry : tour.entrySet()) {
				
				ArrayList<Road> roadsToNextDP = entry.getValue();
				for (Road r:roadsToNextDP){
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

	

	public void startTour(int deliveryPoint) {
		ArrayList<Intersection> intersections = new ArrayList<Intersection>();
		intersections = map.getIntersections();
		LinkedHashMap<Integer, ArrayList<Road>> tour = new LinkedHashMap<>();
        ArrayList<Road> road21 = new ArrayList<>();
        road21.add(new Road(21, 16));
        road21.add(new Road(16,11));
        road21.add(new Road(11, 12));
        road21.add(new Road(12,13));
		tour.put(21, road21);
		
		ArrayList<Road> road13 = new ArrayList<>();
		road13.add(new Road(13,8));
		road13.add(new Road(8,7));
		road13.add(new Road(7,2));
		road13.add(new Road(2,3));
		road13.add(new Road(3,4));
		road13.add(new Road(4,9));
		tour.put(13,road13);
		
		ArrayList<Road> road9 = new ArrayList<>();
		road9.add(new Road(9,4));
		road9.add(new Road(4,3));
		tour.put(9,road9);
		
		ArrayList<Road> road3 = new ArrayList<>();
		road3.add(new Road(3,2));
		road3.add(new Road(2,1));
		tour.put(3, road3);
		
		ArrayList<Road> road1 = new ArrayList<>();
		road1.add(new Road(1,0));
		road1.add(new Road(0,5));
		road1.add(new Road(5,10));
		road1.add(new Road(10,11));
		road1.add(new Road(11,16));
		road1.add(new Road(16,21));
		tour.put(1, road1);
		
		Set<Integer> idDPs = tour.keySet();
        Iterator<Integer> iterator = idDPs.iterator();
        iterator.hasNext();
        int idDP  = iterator.next();
        Point pointDP=null;
        for (Intersection in : intersections) {
			if ((in.getId()).equals(idDP)) {
				pointDP = in.getCoordinates();
				break;
			}
		}
        Graphics g = this.getGraphics();
        g.setColor(Color.ORANGE);
        g.fillOval(((pointDP.x)) / coefficient, ((pointDP.y)) / coefficient, 10, 10);
		
	}

}
