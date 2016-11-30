package com.hexagone.delivery.models;

import java.awt.Point;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.launcher.Main;

public class Route {

	private LinkedHashMap<Integer, ArrayList<Road>> roads;

	private Map map;

	private DeliveryQuery deliveryQuery;

	private DeliveryComputer deliveryComputer;

	public Route(Map map, DeliveryQuery dq, DeliveryComputer dc) {
		this.map = map;
		this.deliveryQuery = dq;
		this.deliveryComputer = dc;
	}

	public LinkedHashMap<Integer, ArrayList<Road>> getRoute() {
		return this.roads;
	}

	public void generateRoute() {
		LinkedHashMap<Integer, ArrayList<Road>> final_roads = new LinkedHashMap<Integer, ArrayList<Road>>();
		// ArrayList<Integer> deliveryPoints = deliveryComputer.getDeliveryPoints();
		ArrayList<Integer> deliveryPoints = new ArrayList<Integer>();

		for (int i = 0; i < deliveryPoints.size() - 1; i++) {
			Integer it1 = deliveryPoints.get(i);
			Integer it2 = deliveryPoints.get(i + 1);
			// Calling Djisktra to get intermediary roads
			// returns [it1,it2] if no intermediary intersections
			ArrayList<Integer> sols = Main.getIntersectionsBetween(it1, it2);

			ArrayList<Road> roads = new ArrayList<Road>();
			for (int j = 0; j < sols.size() - 1; j++) {
				// System.out.println("Current sol (" + sols.get(j) + ")");
				for (Road r : this.map.getRoadsStartingFrom(sols.get(j))) {
					// System.out.println("Destination (" + r.getDestination() +
					// ") ==? " + sols.get(j + 1));
					if (r.getDestination().equals(sols.get(j + 1))) {
						roads.add(r);
						// System.out.println(r + " added.");
						break;
					}
				}
			}
			final_roads.put(it2, roads);
		}
		// Setting roads attribute to the calculated roads
		this.roads = final_roads;
	}

	public void generateTxt(String pathName) {
		File outfile = new File(pathName);
		PrintWriter writer;
		try {
			writer = new PrintWriter(outfile, "UTF-8");
			writer.println(this.generateString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String generateString() {
		// Formatting stuff
		Calendar calStart = Calendar.getInstance();
		// set hour, minutes, seconds and millis at departure
		calStart.setTime(deliveryQuery.getWarehouse().getDepartureTime());
		// Setting formats
		SimpleDateFormat full = new SimpleDateFormat("dd MMMM. yyyy");
		SimpleDateFormat small = new SimpleDateFormat("HH:mm");
		String res = "";
		// Getting necessary objects
		Delivery[] deliveries = deliveryQuery.getDeliveries();
		// Displaying title
		String planningDate = full.format(calStart.getTime());
		planningDate = planningDate.substring(0, 6) + planningDate.substring(10, planningDate.length());
		res += "Mon planning (" + planningDate + ")\n";
		res += "\tDépart de l'entrepôt. Départ: " + small.format(calStart.getTime()) + ".\n";
		String instruction = "";
		boolean deliveryFound = false;
		int waitingTime = -1;
		Integer origin = -1;
		Calendar calEnd = null;
		for (Integer it : roads.keySet()) {
			// Adding road time
			int roadTime = 0;
			for (Road r : roads.get(it)) {
				roadTime += r.getTime();
			}
			calStart.add(Calendar.SECOND, roadTime);
			instruction = "Livraison";
			// Iterating over deliveries
			for (int i = 0; i < deliveries.length; i++) {
				Delivery d = deliveries[i];
				if (d.getIntersection().getId().equals(it)) {
					deliveryFound = true;
					origin = d.getIntersection().getId();
					// adding duration
					int duration = d.getDuration();
					calEnd = (Calendar) calStart.clone();
					calEnd.add(Calendar.SECOND, duration);
					// (if startSchedule not null)
					long waitingMilliSeconds;
					if (d.getStartSchedule() != null) {
						Calendar calTemp = Calendar.getInstance();
						calTemp.setTime(d.getStartSchedule());
						// (if arrival time before startSchedule, wait)
						if (calStart.before(calTemp)) {
							waitingMilliSeconds = (calTemp.getTimeInMillis() - calStart.getTimeInMillis()) / 1000;
							waitingTime = (int) (waitingMilliSeconds / 60);
						}
						calStart = (Calendar) calTemp.clone();
					}
				}
			}
			// Else, it's a warehouse
			if (!deliveryFound) {
				origin = it;
				instruction = "Retour à l'entrepôt";
			}
			res += "\t" + instruction + "\n";
			res += "\t\tArrivée: " + small.format(calStart.getTime());
			if (calEnd != null) {
				res += ". Départ: " + small.format(calEnd.getTime()) + ". ";
			}
			res += "Adresse: Intersection " + origin + ".\n";
			// iterate over roads
			Road lastRoad = null;
			int deg = -1;
			int pos = -1;
			HashMap<Integer, Intersection> intersections = map.getIntersections();
			for (Road r : roads.get(it)) {
				if (lastRoad != null) {
					Point p1 = intersections.get(lastRoad.getOrigin()).getCoordinates();
					Point p2 = intersections.get(r.getOrigin()).getCoordinates();
					Point p3 = intersections.get(r.getDestination()).getCoordinates();
					deg = getAngle(p1, p2, p3);
					
					ArrayList<Road> roads = map.getRoadsStartingFrom(r.getOrigin());
					Collections.sort(roads, new Comparator<Road>() {
						@Override
						public int compare(Road r1, Road r2) {
							Point pp1 = intersections.get(r1.getDestination()).getCoordinates();
							Point pp2 = intersections.get(r2.getDestination()).getCoordinates();
							int angle1 = getAngle(p1, p2, pp1);
							int angle2 = getAngle(p1, p2, pp2);
							if (angle1 > angle2) {
								return 1;
							} else {
								return -1;
							}
						}
					});
					pos = 1 + roads.indexOf(r);
				} else {
					deg = 0; // Go straight from warehouse
				}
				// get name of road
				String name = r.getRoadName();
				res += "\t\t\t" + "Prendre la " + getPlainPosition(pos) + " " + getPlainDegree(deg) + " (rue " + name + ")\n";
				lastRoad = r;
			}
			if (waitingTime != -1) {
				res += "\t\t\t" + "Attendre" + waitingTime + " minutes/n.";
			}
		}
		System.out.println(res);
		// Fin des instructions
		return res;
	}

	private String getPlainPosition(int pos) {
		String res = "";
		String num = String.valueOf(pos); // default value
		if (pos > 1) {
			res += num;
			res += "ème ";
		}
		if (pos == 1 ){
			res += num;
			res += "ère ";
		}
		res += "rue";
		return res;
	}

	private String getPlainDegree(int deg) {
		String dir = "tout droit"; // default value
		int threshhold = 10;
		if (deg > threshhold / 2 && deg < 90) {
			dir = "à droite";
		}
		if (deg > 90 && deg < (360 - threshhold / 2)) {
			dir = "à gauche";
		}
		return dir;
	}

	/*
	 * Returns angle between three points (marked as p1/p2/p3)
	 */
	public int getAngle(Point p0, Point p1, Point p2) {
		double b = Math.pow(p1.x - p0.x, 2) + Math.pow(p1.y - p0.y, 2);
		double a = Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
		double c = Math.pow(p2.x - p0.x, 2) + Math.pow(p2.y - p0.y, 2);
		double res = Math.acos((a + b - c) / Math.sqrt(4 * a * b));
		res = res * 180 / Math.PI;
		return (int) res;
	}
}
