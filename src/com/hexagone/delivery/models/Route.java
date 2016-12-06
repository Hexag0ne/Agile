package com.hexagone.delivery.models;

import java.awt.Point;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.launcher.Main;

public class Route {

	private LinkedHashMap<Integer, ArrivalPoint> route;

	private Map map;

	private DeliveryQuery deliveryQuery;

	private DeliveryComputer deliveryComputer;

	private String planning;
	
	private HashMap<Integer, Intersection> intersections;

	public Route(Map map, DeliveryQuery dq, DeliveryComputer dc) {
		this.map = map;
		this.deliveryQuery = dq;
		this.deliveryComputer = dc;
		this.intersections = map.getIntersections();
		this.route = generateRoute();
		this.planning = generatePlanning();
	}

	public LinkedHashMap<Integer, ArrivalPoint> getRoute() {
		return route;
	}

	public String getPlanning() {
		return planning;
	}

	public LinkedHashMap<Integer, ArrivalPoint> generateRoute() {
		LinkedHashMap<Integer, ArrivalPoint> route = new LinkedHashMap<Integer, ArrivalPoint>();
		HashMap<Integer, Intersection> intersections = map.getIntersections();
		
		ArrayList<Integer> deliveryPoints = deliveryComputer.getDeliveryPoints();
		//Integer[] its = {0, 8, 12, 22, 5, 0}; ArrayList<Integer> deliveryPoints = new ArrayList<Integer>(); deliveryPoints.addAll(Arrays.asList(its));

		Calendar calArrival = Calendar.getInstance();
		calArrival.setTime(deliveryQuery.getWarehouse().getDepartureTime());
		for (int i = 1; i < deliveryPoints.size(); i++) {
			Integer it1 = deliveryPoints.get(i);
			Integer it2 = deliveryPoints.get(i - 1);
			Intersection is = intersections.get(it1);
			
			Delivery delivery;
			ArrayList<Road> roads = getRoadsbetweenIntersections(it1, it2);
			if (i == deliveryPoints.size() - 1) {
				delivery = completeWarehouse(calArrival, is, roads);
			}
			else {
				Delivery d = findDelivery(it1);
				if (d != null) {
					delivery = completeDelivery(calArrival, d, roads);
				}
				else {
					System.out.println("Delivery not found!");
					delivery = null;
				}
			}
			route.put(it1, new ArrivalPoint(roads, delivery));
		}
		return route;
	}
	
	public void writeToTxt(String pathName) {
		String planning = this.getPlanning();
		File outfile = new File(pathName);
		PrintWriter writer;
		try {
			writer = new PrintWriter(outfile, "UTF-8");
			writer.println(planning);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String generatePlanning() {
		// Setting formats
		SimpleDateFormat full = new SimpleDateFormat("dd MMMM. yyyy", Locale.FRENCH);
		SimpleDateFormat small = new SimpleDateFormat("HH:mm", Locale.FRENCH);
		String res = "";
		// Displaying title
		Date departureTime = deliveryQuery.getWarehouse().getDepartureTime();
		String planningDate = full.format(departureTime);
		res += "Mon planning (" + planningDate + ")\n\n";
		res += "\tDépart de l'entrepôt à " + small.format(departureTime) + ". ";
		// Record for longest java call ?
		res += "Rejoindre l'intersection " + route.entrySet().iterator().next().getValue().getRoads().get(0).getOrigin() + ".\n";
		
		// Iterating over arrival points
		int deliveryCounter = 0;
		int waitingTime = 0;
		Integer origin;
		Date arrivalTime;
		for (Integer it : route.keySet()) {
			origin = route.get(it).getDelivery().getIntersection().getId();
			waitingTime = route.get(it).getDelivery().getWaitingTime();
			arrivalTime = route.get(it).getDelivery().getArrivalTime();
			
			if (deliveryCounter > 0) {
				res += "\tDépart du point de livraison à " + small.format(departureTime) + ".\n";
			}
			
			Road lastRoad = null;
			int deg = 0;
			int pos = 0;
			int roadCounter = 1;
			for (Road road : route.get(it).getRoads()) {
				if (lastRoad != null) {
					deg = getAngleBetweenRoads(lastRoad, road);
					pos = getPosition(road, lastRoad, deg);
				}
				res += "\t\t" + roadCounter + "- Prendre la " + getPlainPosition(pos) + " " + getPlainDegree(deg) + " jusqu'à l'intersection " + road.getDestination() + "\n";
				lastRoad = road;
				roadCounter++;
			}
			if (deliveryCounter != route.values().size() - 1) {
				res += "\tLivraison du point " + origin + ". Arrivée: " + small.format(arrivalTime) + ".\n";	
			}
			else {
				res += "\tFin de la tournée à " + small.format(arrivalTime);
			}
			if (waitingTime != 0) {
				res += "\tAttendre " + waitingTime + " minutes puis procéder à la livraison.\n";
			}
			res += "\n";
			deliveryCounter++;
			departureTime = route.get(it).getDelivery().getDepartureTime();
		}
		System.out.println(res);
		// Fin des instructions
		return res;
	}
	
	private Delivery completeWarehouse(Calendar calArrival, Intersection is, ArrayList<Road> roads) {
		Delivery d = new Delivery(is);
		int roadTime = getTotalTime(roads);
		calArrival.add(Calendar.SECOND, roadTime);
		d.setArrivalTime(calArrival.getTime());
		return d;
	}

	private int getTotalTime(ArrayList<Road> roads) {
		int roadTime = 0;
		for (Road r : roads) {
			roadTime += r.getTime();
		}
		return roadTime;
	}

	private Delivery completeDelivery(Calendar calArrival, Delivery d, ArrayList<Road> roads) {
		int roadTime = getTotalTime(roads);
		calArrival.add(Calendar.SECOND, roadTime);
		int duration = d.getDuration();
		Calendar calDeparture = (Calendar) calArrival.clone();
		calDeparture.add(Calendar.SECOND, duration);
		
		long waitingMilliSeconds;
		int waitingTime = 0;
		if (d.getStartSchedule() != null) {
			Calendar calTemp = Calendar.getInstance();
			calTemp.setTime(d.getStartSchedule());
			// (if arrival time before startSchedule, wait)
			if (calArrival.before(calTemp)) {
				waitingMilliSeconds = (calTemp.getTimeInMillis() - calArrival.getTimeInMillis()) / 1000;
				waitingTime = (int) (waitingMilliSeconds);
			}
		}
		d.setTimes(calDeparture.getTime(), calArrival.getTime(), waitingTime);
		return d;
	}

	private ArrayList<Road> getRoadsbetweenIntersections(Integer it1, Integer it2) {
		ArrayList<Road> roads = new ArrayList<Road>();
		ArrayList<Integer> sols = deliveryComputer.getShortestPath(it1, it2);
		//ArrayList<Integer> sols = Main.getShortestPath(it1, it2);
		
		for (int j = 0; j < sols.size() - 1; j++) {
			for (Road r : this.map.getRoadsStartingFrom(sols.get(j))) {
				if (r.getDestination().equals(sols.get(j + 1))) {
					roads.add(r);
					break;
				}
			}
		}
		return roads;
	}
	
	private Delivery findDelivery(Integer it) {
		Delivery[] deliveries = deliveryQuery.getDeliveries();
		Delivery d;
		for (int i = 0; i < deliveries.length; i++) {
			d = deliveries[i];
			if (d.getIntersection().getId().equals(it)) {
				return d;
			}
		}
		return null;
	}

	private int getPosition(Road road, Road lastRoad, int deg) {
		Point p1 = intersections.get(lastRoad.getOrigin()).getCoordinates();
		Point p2 = intersections.get(road.getOrigin()).getCoordinates();
		
		ArrayList<Road> roads = new ArrayList<Road>();
		for (Road rr : map.getRoadsStartingFrom(road.getOrigin())) {
			int threshhold = 10;
			Point p4 = intersections.get(rr.getDestination()).getCoordinates();
			int new_angle = getAngle(p1, p2, p4);
			if (deg > threshhold / 2 && deg < 90) {
				if (new_angle > threshhold / 2 && new_angle < 90) {
					roads.add(rr);
				}
			}
			if (deg > 90 && deg < (360 - threshhold / 2)) {
				if (new_angle > 90 && new_angle < (360 - threshhold / 2)) {
					roads.add(rr);
				}
			}
		}
		// Filter those NOT left or not right ok ?
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
		int pos = 1 + roads.indexOf(road);
		return pos;
	}

	private int getAngleBetweenRoads(Road lastRoad, Road road) {
		Point p1 = intersections.get(lastRoad.getOrigin()).getCoordinates();
		Point p2 = intersections.get(road.getOrigin()).getCoordinates();
		Point p3 = intersections.get(road.getDestination()).getCoordinates();
		int deg = getAngle(p1, p2, p3);
		return deg;
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
