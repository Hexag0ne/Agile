package com.hexagone.delivery.models;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class models a planning (feuille de route)
 * It is the result of all calculations made from a map and a delivery query
 * to provide a planning for a delivery person
 */
public class Planning {
	private static final String PATH_NAME = "export/planning.txt";

	/*
	 * List of intersections given by TSP
	 */
	private Integer[] intersections;
	
	/*
	 * List of roads traveled by for solution
	 */
	private Road[] roads;
	
	private Map map;
	
	/*
	 * Returns the total time a planning will take for delivery man
	 */
	public int getTotalTime() {
		int totalTime = 0;
		for (Road road : roads) {
			totalTime += road.getTime();
		}
		return totalTime;
	}
	
	public Planning(Integer[] intersections, Map map) {
		this.intersections = intersections;
		this.map = map;
		this.roads = this.calculateRoads();
	}
	
	private Road[] calculateRoads() {
		for (Integer inter : intersections) {
			System.out.println("");
		}
		return null;
	}
	
	public Road[] getRoads() {
		return this.roads;
	}

	public void generateTxt() {
		File outfile = new File(PATH_NAME);
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
	 * String representation of a planning
	 * Example:
	 * 		Mon planning (22 Nov. 2016)
	 * 			Entrepôt 1
	 * 				Arrivée: 14h00. Départ: 14h10. Adresse: Intersection 2.
	 * 				Itinéraire:
	 * 					Prendre l'intersection 1
	 * 					Suivre la route h0
	 * 					Aller à l'intersection 2
	 * 			Livraison A:
	 * 				Arrivée: 14h20. Départ: 14h40. Adresse: Intersection 3.
	 * 				Itinéraire:
	 * 					Aller à l'intersection 1
	 * 					Aller à l'intersection 2
	 * 					Suivre la route v2
	 * 			Livraison B:
	 * 				Arrivée: 15h20. Départ: 15h40. Adresse: Intersection 6.
	 * 				Itinéraire:
	 * 					Aller à l'intersection 5
	 * 					Suivre la route v0
	 * 					Aller à l'intersection 6
	 * 			Entrepôt ABC:
	 * 				15h40
	 */
	@Override
	public String toString() {
		Date date = new Date();
		SimpleDateFormat full = new SimpleDateFormat("dd MMMM. yyyy");
		SimpleDateFormat small = new SimpleDateFormat("HH:mm");
		String res = "";
		// Displaying title
		res += "Mon planning (" + full.format(date) + ")\n";
		// Iterate over EACH delivery (with first and last of warehouse type)
		String type, startDate, endDate, origin;
		// Iter 1
		type = "Livraison"; // or "Livraison"
		startDate = small.format(date);
		endDate = small.format(date);
		origin = "3";
		res += "\t" + type + "\n"; 
		res += "\t\tArrivée: " + startDate + ". Départ: " + endDate + ". Adresse: Intersection " + origin + ".\n";
		// Iterate over EACH instruction given
			String instruction, name;
			// Iter 1
			instruction = "Prendre l'intersection"; // or "Suivre la route"
			name = "1";
			res += "\t\t\t" + instruction + " " + name + "\n";
			// Iter 2
			instruction = "Suivre la route"; // or "Suivre la route"
			name = "1";
			res += "\t\t\t" + instruction + " " + name + "\n";
			// Fin des instructions
		return res;
	}
}