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
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JOptionPane;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.models.ArrivalPoint;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.RouteHelper;
import com.hexagone.delivery.models.Warehouse;
import com.hexagone.delivery.ui.MainFrame;
import com.hexagone.delivery.ui.Popup;
import com.hexagone.delivery.xml.NoFileChosenException;
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
			Popup.showInformation("Le fichier choisi n'est pas un plan valide.");
			return null;
		} catch (NoFileChosenException e) {
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
		} catch (NoFileChosenException e) {
			return null;
		}
	}

	@Override
	public RouteHelper computeDelivery(Map map, DeliveryQuery delivery) {
		DeliveryComputer computer = new DeliveryComputer(map, delivery);
		computer.getDeliveryPoints();

		return new RouteHelper(map, delivery, computer);
	}

	@Override
	public void generatePlanning(RouteHelper routeHelper) {
		routeHelper.writeToTxt("export/planning.txt");
		
		Popup.showInformation(routeHelper.getPlanning(), "Feuille de route généré !");
	}

	@Override
	public void DrawMap(Graphics g, float coefficient, Map map, DeliveryQuery deliveryQuery, RouteHelper routeHelper) {
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
				// TODO
				for (Intersection in : intersections) {
					if ((in.getId()).equals(r.getOrigin())) {
						origine = in.getCoordinates();
						break;
					}
				}
				// TODO
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
		// TODO
		for (Intersection in : intersections) {
			if ((in.getId()).equals(intersectionWarehouse.getId())) {
				pointWarehouse = in.getCoordinates();
				break;
			}
		}

		HashMap<Integer, ArrivalPoint> tour = routeHelper.getRoute();
		Set<Entry<Integer, ArrivalPoint>> entrySet = tour.entrySet();
		Iterator<Entry<Integer, ArrivalPoint>> iterator = entrySet.iterator();
		for (int i = 0; i < step + 1 && i < tour.size(); i++) {

			Entry<Integer, ArrivalPoint> entry = iterator.next();
			Point pointDelivery = new Point();

			for (Entry<Integer, ArrivalPoint> entryALLMAP : tour.entrySet()) {

				ArrayList<Road> roadsToNextDP = entryALLMAP.getValue().getRoads();
				for (Road r : roadsToNextDP) {
					Graphics2D g2 = (Graphics2D) g;
					Point destination = null;
					Point origine = null;
					// TODO
					for (Intersection in : intersections) {
						if ((in.getId()).equals(r.getOrigin())) {
							origine = in.getCoordinates();
							break;
						}
					}
					// TODO
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
				// TODO
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
				g.fillOval((int) (((p.x)) / coefficient), (int) (((p.y)) / coefficient), 10, 10);
			}
			if (i == step) {
				g.setColor(new Color(0, 102, 0));
				// TODO
				for (Intersection in : intersections) {
					if ((in.getId()).equals(entry.getKey())) {
						pointDelivery = in.getCoordinates();
						break;
					}
				}
				g.fillOval((int) (((pointDelivery.x)) / coefficient), (int) (((pointDelivery.y)) / coefficient), 20,
						20);
			}

			g.setColor(Color.RED);
			g.fillOval((int) (((pointWarehouse.x)) / coefficient), (int) (((pointWarehouse.y)) / coefficient), 15, 15);
			g.drawString("Entrepôt", (int) (((pointWarehouse.x)) / coefficient + 5),
					(int) (((pointWarehouse.y)) / coefficient));

		} // TODO Auto-generated method stub

	}

	public void startTour() {
		step = 0;
		frame.repaint();
		frame.setFocusableOnCenterPanel();
	}

	public void nextDelivery(int maxValue) {
		step++;
		if (step > maxValue) {
			step = maxValue;
		}
		frame.selectionRow(step);
		frame.repaint();
		frame.setFocusableOnCenterPanel();
	}

	public void previousDelivery() {
		if (step > 0) {
			step--;
		}

		frame.selectionRow(step);
		frame.repaint();
		frame.setFocusableOnCenterPanel();
	}

	public NavigateState(MainFrame frame) {
		this.frame = frame;
	}

}
