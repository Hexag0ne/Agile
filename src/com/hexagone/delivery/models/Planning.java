package com.hexagone.delivery.models;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

/**
 * This class models a planning (feuille de route) It is the result of all
 * calculations made from a map and a delivery query to provide a planning for a
 * delivery person
 */
public class Planning {

	private Integer[] intersections;

	/*
	 * List of roads traveled by for solution
	 */
	private LinkedHashMap<Integer, ArrayList<Road>> roads;

	private DeliveryQuery deliveryQuery;

	private Map map;

	/*
	 * Returns the total time a planning will take for delivery man
	 */
	public int getTotalTime() {
		int totalTime = 0;
		for (ArrayList<Road> road_list : roads.values()) {
			for (Road r : road_list) {
				totalTime += r.getTime();
			}
		}
		return totalTime;
	}

	public Planning(Map map, DeliveryQuery dq, Integer[] intersections) {
		this.map = map;
		this.deliveryQuery = dq;
		this.intersections = intersections;
	}

	private LinkedHashMap<Integer, ArrayList<Road>> calculateRoads() {
		LinkedHashMap<Integer, ArrayList<Road>> final_roads = new LinkedHashMap<Integer, ArrayList<Road>>();
		for (int i = 0; i < intersections.length - 1; i++) {
			Integer it1 = intersections[i];
			Integer it2 = intersections[i + 1];
			// Calling Djisktra to get intermediary roads
			// returns [it1,it2] if no intermediary intersections
			// ArrayList<Integer> sols = getIntersectionsBetween(it1, it2);

			ArrayList<Integer> sols = new ArrayList<Integer>();

			ArrayList<Road> roads = new ArrayList<Road>();
			for (int j = 0; j < sols.size() - 1; j++) {
				// System.out.println("Current sol (" + sols.get(j) + ")");
				for (Road r : map.getRoadsStartingFrom(sols.get(j))) {
					// System.out.println("Destination (" + r.getDestination() +
					// ") ==? " + sols.get(j + 1));
					if (r.getDestination().equals(sols.get(j + 1))) {
						roads.add(r);
						// System.out.println(r + " added.");
						break;
					}
				}
			}
			System.out.println(it2 + " -> " + roads);
			final_roads.put(it2, roads);
		}
		return final_roads;
	}

	public void generateTxt(String pathName) {
		File outfile = new File(pathName);
		PrintWriter writer;
		try {
			writer = new PrintWriter(outfile, "UTF-8");
			writer.println(this);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * String representation of a planning Example: Mon planning (22 Nov. 2016)
	 * Entrepôt 1 Arrivée: 14h00. Départ: 14h10. Adresse: Intersection 2.
	 * Itinéraire: Prendre l'intersection 1 Suivre la route h0 Aller à
	 * l'intersection 2 Livraison A: Arrivée: 14h20. Départ: 14h40. Adresse:
	 * Intersection 3. Itinéraire: Aller à l'intersection 1 Aller à
	 * l'intersection 2 Suivre la route v2 Livraison B: Arrivée: 15h20. Départ:
	 * 15h40. Adresse: Intersection 6. Itinéraire: Aller à l'intersection 5
	 * Suivre la route v0 Aller à l'intersection 6 Entrepôt ABC: 15h40 N.B: The
	 * structured representation was flattened by Eclipse formatting.
	 */
	@Override
	public String toString() {
		// Formatting stuff
		Calendar calStart = Calendar.getInstance();
		// set hour, minutes, seconds and millis at departure
		calStart.setTime(deliveryQuery.getWarehouse().getDepartureTime());
		// Setting formats
		SimpleDateFormat full = new SimpleDateFormat("dd MMMM. yyyy");
		SimpleDateFormat small = new SimpleDateFormat("HH:mm");
		String res = "";
		// Getting necessary objects
		LinkedHashMap<Integer, ArrayList<Road>> roads = calculateRoads();
		Delivery[] deliveries = deliveryQuery.getDeliveries();
		// Displaying title
		String planningDate = full.format(calStart.getTime());
		res += "Mon planning (" + planningDate + ")\n";
		res += "\tDépart de l'entrepôt. Départ: " + small.format(calStart.getTime()) + ".\n";
		// Iterate over each delivery point (with last being warehouse)
		int inc = 0;
		Integer origin = null;
		String instruction = null;
		Calendar calEnd = null;
		int waitingTime = -1;
		for (Integer it : roads.keySet()) {
			// Adding road time
			int roadTime = 10; // Random offset to showcase that "Arrivée" "adds
								// up time"
			for (Road r : roads.get(it)) {
				roadTime += r.getTime();
			}
			calStart.add(Calendar.SECOND, roadTime);
			instruction = "Livraison";
			// Searching for deliveries if delivery point
			boolean deliveryFound = false;
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
			for (Road r : roads.get(it)) {
				// get name of road
				String name = r.getRoadName();
				res += "\t\t\t" + "Suivre la route" + " " + name + "\n";
			}
			if (waitingTime != -1) {
				res += "\t\t\t" + "Attendre" + waitingTime + " minutes/n.";
			}
		}
		System.out.println(res);
		// Fin des instructions
		return res;
	}
}