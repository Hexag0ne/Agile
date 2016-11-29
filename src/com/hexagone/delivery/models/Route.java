package com.hexagone.delivery.models;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
		//ArrayList<Integer> deliveryPoints = deliveryComputer.getDeliveryPoints();
		ArrayList<Integer> deliveryPoints = new ArrayList<Integer>();
		deliveryPoints.add(6);deliveryPoints.add(12);deliveryPoints.add(8);deliveryPoints.add(6);
		System.out.println(deliveryPoints);
		for (int i = 0; i < deliveryPoints.size() - 1; i++) {
			Integer it1 = deliveryPoints.get(i);
			Integer it2 = deliveryPoints.get(i+1);
			// Calling Djisktra to get intermediary roads
			// returns [it1,it2] if no intermediary intersections
			ArrayList<Integer> sols = Main.getIntersectionsBetween(it1, it2);

			ArrayList<Road> roads = new ArrayList<Road>();
			for (int j = 0; j < sols.size() - 1; j++) {
				//System.out.println("Current sol (" + sols.get(j) + ")");
				for (Road r : this.map.getRoadsStartingFrom(sols.get(j))) {
					//System.out.println("Destination (" + r.getDestination() +
					//") ==? " + sols.get(j + 1));
					if (r.getDestination().equals(sols.get(j + 1))) {
						roads.add(r);
						//System.out.println(r + " added.");
						break;
					}
				}
			}
			System.out.println(it2 + " -> " + roads);
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
			for (Road r : roads.get(it)) {
				// get name of road
				String name = r.getRoadName();
				int pos = 1; // "troisième" ...
				int deg = 10;
				
				res += "\t\t\t" + "Prendre la " + getPlainPosition(pos) + " à " + getPlainDegree(deg) + " (rue " + name + ")\n";
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
		String dir = "rue"; // default value
		switch(pos) {
			case 1:	dir = "première"; break;
			case 2:	dir = "deuxième"; break;
			case 3:	dir = "troisième"; break;
			case 4:	dir = "quatrième"; break;
			case 5:	dir = "cinquième"; break;
		}
		return dir;
	}
	
	private String getPlainDegree(int deg) {
		String dir = "tout droit"; // default value
		int threshhold = 10;
		if (deg > threshhold/2 && deg < 90) {
			dir = "à droite";
		}
		if (deg > 90 && deg < (360 - threshhold/2)) {
			dir = "à gauche";
		}
		return dir;
	}
}
