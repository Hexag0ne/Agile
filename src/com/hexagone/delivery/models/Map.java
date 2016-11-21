package com.hexagone.delivery.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
	private  ArrayList<Intersection> intersections;
	private HashMap<Integer,ArrayList<Road>> roads; 
	
	/**
	 * Default constructor.
	 * No intersection or road exists in the map yet.
	 */
	public Map(){
		intersections = new ArrayList<Intersection>();
		roads = new HashMap<Integer,ArrayList<Road>>();
	}
	
	/**
	 * Allows the user to add an intersection to the map.
	 * @param intersection the Intersection to be addded
	 * This method does not check if the intersection provided is valid, that is if the intersection is needed well 
	 * initialized with all its attributes initialized and if among the intersections already part of the map, no 
	 * intersection share the same identifier.
	 */
	public void addIntersection(Intersection intersection){
		intersections.add(intersection);
	}
	
	/**
	 * Allows to add a road between two intersections to the map
	 * @param road the road to add to the map
	 * This method does not check if the road is valid. That is if there are really two intersections already in the map
	 * from the origin towards the destination of the Road given as parameter. 
	 */
	public void addRoad(Road r){
		ArrayList<Road> roadListFromOrigin = roads.get(r.getOrigin());
		if (roadListFromOrigin == null) {
			roadListFromOrigin = new ArrayList<Road>();
		}
		roadListFromOrigin.add(r);
		roads.put(r.getOrigin(), roadListFromOrigin);
	}
}
