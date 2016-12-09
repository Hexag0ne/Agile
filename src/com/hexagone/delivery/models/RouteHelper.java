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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.Map.Entry;

import com.hexagone.delivery.algo.DeliveryComputer;

/**
 * This class is part of the map modeling. A Route carries several information :
 * <ul>
 * <li>the arrival points</li>
 * <li>the map</li>
 * <li>the delivery query</li>
 * <li>the delivery computer</li>
 * </ul>
 * Note that if a real road allows cars to travel both ways, it will be modeled
 * as <strong>two different roads </strong> in the map.
 */
public class RouteHelper {

	private LinkedHashMap<Integer, ArrivalPoint> route;

	private Map map;

	private DeliveryQuery deliveryQuery;

	private DeliveryComputer deliveryComputer;

	private String planning;

	private HashMap<Integer, Intersection> intersections;

	private int oldDuration;

	private static String ONE_INDENT = "        ";
	private static String TWO_INDENT = "                ";
	
	public RouteHelper(Map map, DeliveryQuery dq, DeliveryComputer dc) {
		this.map = map;
		this.deliveryQuery = dq;
		this.deliveryComputer = dc;
		this.intersections = map.getIntersections();
		this.route = generateRoute();
		this.planning = generatePlanning();
	}

	public boolean routeFound(){
		return deliveryComputer.checkNotEmptySolution();
	}

	public boolean computationTimeOut(){
		return deliveryComputer.checkTimeout();
	}

	public LinkedHashMap<Integer, ArrivalPoint> getRoute() {
		return route;
	}

	public String getPlanning() {
		return planning;
	}

	/**
	 * Generates a route, from the calculation of the intersections of the map,
	 * the delivery points, the roads between two intersections
	 * 
	 */
	public LinkedHashMap<Integer, ArrivalPoint> generateRoute() {
		LinkedHashMap<Integer, ArrivalPoint> route = new LinkedHashMap<Integer, ArrivalPoint>();
		HashMap<Integer, Intersection> intersections = map.getIntersections();

		ArrayList<Integer> deliveryPoints = deliveryComputer.getDeliveryPoints();
		Integer[] its = { 0, 8, 12, 22, 5, 0 };
		ArrayList<Integer> deliveryPoints2 = new ArrayList<Integer>();
		deliveryPoints2.addAll(Arrays.asList(its));


		Calendar calArrival = Calendar.getInstance();
		oldDuration = 0;
		calArrival.setTime(deliveryQuery.getWarehouse().getDepartureTime());
		for (int i = 1; i < deliveryPoints.size(); i++) {
			Integer it1 = deliveryPoints.get(i);
			Integer it2 = deliveryPoints.get(i - 1);
			Intersection is = intersections.get(it1);

			Delivery delivery;
			// TODO erreur dans l'appel. Les paramètres sont inversés !!!
			ArrayList<Road> roads = getRoadsbetweenIntersections(it1, it2);
			if (i == deliveryPoints.size() - 1) {
				delivery = completeWarehouse(calArrival, is, roads);
			} else {
				Delivery d = findDelivery(it1);
				if (d != null) {
					delivery = completeDelivery(calArrival, d, roads);
				} else {
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
		res += ONE_INDENT + "Départ de l'entrepôt à " + small.format(departureTime) + ". ";
		// Record for longest java call ?
		res += "Rejoindre l'intersection " + route.entrySet().iterator().next().getValue().getRoads().get(0).getOrigin()
				+ ".\n";

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
				res += ONE_INDENT + "Départ du point de livraison à " + small.format(departureTime) + ".\n";
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
				res += TWO_INDENT + roadCounter + "- Prendre la " + getPlainPosition(pos) + " " + getPlainDegree(deg)
				+ " jusqu'à l'intersection " + road.getDestination() + "\n";
				lastRoad = road;
				roadCounter++;
			}
			if (deliveryCounter != route.values().size() - 1) {
				res += ONE_INDENT + "Livraison du point " + origin + ". Arrivée: " + small.format(arrivalTime) + ".\n";
			} else {
				res += ONE_INDENT + "Fin de la tournée à " + small.format(arrivalTime);
			}
			if (waitingTime != 0) {
				res += ONE_INDENT + "Attendre " + waitingTime + " minutes puis procéder à la livraison.\n";
			}
			res += "\n";
			deliveryCounter++;
			departureTime = route.get(it).getDelivery().getDepartureTime();
		}
		// Fin des instructions
		return res;
	}

	/**
	 * Completes a particular Arrival Point : a warehouse. Sets the arrival time
	 * of the delivery
	 * 
	 * @param calArrival
	 *            : calendar corresponding to the arrival
	 * @param is
	 *            : current intersection as an Intersection Object
	 * @param roads
	 *            : roads of the route as an ArrayList of Road
	 * @return the delivery Object
	 */
	private Delivery completeWarehouse(Calendar calArrival, Intersection is, ArrayList<Road> roads) {
		Delivery d = new Delivery(is);
		int roadTime = getTotalTime(roads);
		calArrival.add(Calendar.SECOND, roadTime);
		d.setArrivalTime(calArrival.getTime());
		return d;
	}

	/**
	 * Calculates the total time of the road
	 * 
	 * @param roads
	 *            : roads of the route as an ArrayList of Road
	 * @return the total time as an Integer
	 */
	private int getTotalTime(ArrayList<Road> roads) {
		int roadTime = 0;
		for (Road r : roads) {
			roadTime += r.getTime();
		}
		return roadTime;
	}

	/**
	 * Completes the delivery for an arrival point.
	 * 
	 * @param calArrival
	 *            : calendar corresponding to the arrival
	 * @param d
	 *            : current delivery
	 * @param roads
	 *            : roads of the route as an ArrayList of Road
	 * @return the delivery
	 */
	private Delivery completeDelivery(Calendar calArrival, Delivery d, ArrayList<Road> roads) { 
		int roadTime = getTotalTime(roads);
		calArrival.add(Calendar.SECOND, oldDuration);
		// Adding road time to arrival time
		calArrival.add(Calendar.SECOND, roadTime);
		int duration = d.getDuration();
		// Departure Time = Arrival Time
		Calendar calDeparture = (Calendar) calArrival.clone();
		// Departure Time += duration
		calDeparture.add(Calendar.SECOND, duration);

		long waitingSeconds;
		int waitingTime = 0;
		if (d.getStartSchedule() != null) {
			Calendar calTemp = Calendar.getInstance();
			calTemp.setTime(d.getStartSchedule());
			// (if arrival time before startSchedule, wait)
			if (calArrival.before(calTemp)) {
				waitingSeconds = (calTemp.getTimeInMillis() - calArrival.getTimeInMillis()) / 1000;
				waitingTime = (int) (waitingSeconds / 60);
				calDeparture.add(Calendar.SECOND, (int) waitingSeconds);
			}
		}
		oldDuration = waitingTime*60 + duration;
		d.setTimes(calDeparture.getTime(), calArrival.getTime(), waitingTime);
		return d;
	}

	/**
	 * Gets the roads in order between two intersections
	 * 
	 * @param it1
	 *            : identifier of the first intersection
	 * @param it2
	 *            : identifier of the 2nd intersection
	 * @return the roads found as an ArrayList of Road
	 */
	private ArrayList<Road> getRoadsbetweenIntersections(Integer it1, Integer it2) {
		ArrayList<Road> roads = new ArrayList<Road>();
		ArrayList<Integer> sols = deliveryComputer.getShortestPath(it1, it2);
		// ArrayList<Integer> sols = Main.getShortestPath(it1, it2);

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

	/**
	 * Finds a delivery with the identifier of its intersection
	 * 
	 * @param it
	 *            : identifier of the intersection
	 * @return the delivery as a Delivery Object
	 */
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

	/**
	 * Interprets a position and gives its number and suffix
	 * 
	 * @param pos
	 *            : position as an Integer
	 * @return the plain position as a String : number of the position and
	 *         suffix
	 */
	private String getPlainPosition(int pos) {
		String res = "";
		String num = String.valueOf(pos); // default value
		if (pos > 1) {
			res += num;
			res += "ème ";
		}
		if (pos == 1) {
			res += num;
			res += "ère ";
		}
		res += "rue";
		return res;
	}

	/**
	 * Interprets a degree and gives its direction
	 * 
	 * @param deg
	 *            : degree as an Integer
	 * @return the plain degree as a String : the direction depending on the
	 *         angle
	 */
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

	/**
	 * Returns the angle between three points (marked as p1/p2/p3)
	 * 
	 * @param p0,
	 *            p1, p2 : Points that we want to get the angle of
	 * @return the angle as an Integer
	 */
	public int getAngle(Point p0, Point p1, Point p2) {
		double b = Math.pow(p1.x - p0.x, 2) + Math.pow(p1.y - p0.y, 2);
		double a = Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
		double c = Math.pow(p2.x - p0.x, 2) + Math.pow(p2.y - p0.y, 2);
		double res = Math.acos((a + b - c) / Math.sqrt(4 * a * b));
		res = res * 180 / Math.PI;
		return (int) res;
	}

	/**
	 * Returns the rank of a delivery point 
	 * 
	 * @param idDP: the delivery point id 
	 */

	public int getRankDP(int idDP){

		int dpPosition=0;
		Set<Integer> entrySet= route.keySet();
		Iterator<Integer> iterator= entrySet.iterator();
		int i=0;
		while(iterator.hasNext()){
			i++;
			if((iterator.next()).equals(idDP)){
				dpPosition=i-1;
			}
		}

		return dpPosition;
	}

	public int getIdbyRank(int rankDP) {

		int dpId=0;
		Set<Integer> entrySet= route.keySet();
		Iterator<Integer> iterator= entrySet.iterator();
		int i=0;
		while(iterator.hasNext()){
			i++;
			if(i==rankDP){
				dpId = iterator.next();
				break;
			}
			iterator.next();
		}

		return dpId;
	}

}
