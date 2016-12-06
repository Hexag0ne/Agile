/**
 * Package containing all the classes modelling our problem.
 */
package com.hexagone.delivery.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class models a city as an aggregate of Intersections and Roads.
 * @see Intersection
 * @see Road
 */

public class Map {

	/** HashMap of the Intersections present in the map. The key is the unique identifier of the Intersection it maps */
	private HashMap<Integer, Intersection> intersections;
	/** HashMap of the intersection identifier to the list of roads originating from this intersection */
	private HashMap<Integer, ArrayList<Road>> roads;

	/**
	 * Getter for all the Roads present in the map
	 * @return the hashMap containing the different Roads of the map
	 */
	public HashMap<Integer, ArrayList<Road>> getRoads() {
		return roads;
	}
	
	/**
	 * Allows the user to access all the roads starting from an intersection
	 * whose identifiers is provided as a parameter 
	 * @param identifier the unique identifier of the intersection
	 * @return the list of all roads originating from the intersection as an ArrayList<Road>
	 */
	public ArrayList<Road> getRoadsStartingFrom(Integer identifier) {
		return roads.get(identifier);
	}

	/**
	 * Allows the user to define in one shot all the roads of the map
	 * @param roads the hashMap<Integer,ArrayList<Road>> 
	 */
	public void setRoads(HashMap<Integer, ArrayList<Road>> roads) {
		this.roads = roads;
	}

	/**
	 * Gives all the intersection identifiers of the map
	 * @return the identifiers of the intersections in the map as a set.
	 */
	public HashSet<Integer> getAllIntersectionIdentifiers() {
		HashSet<Integer> set = new HashSet<Integer>(roads.keySet());
		return set;
	}


	/**
	 * Allows the user to add an intersection to the map.
	 * @param intersection the Intersection to be addded This method does not check if the intersection provided is 
	 * valid, that is if the intersection is needed well initialized with all its attributes initialized and if among 
	 * the intersections already part of the map, no intersection share the same identifier.
	 */
	public void addIntersection(Intersection intersection) {
		intersections.put(intersection.getId(), intersection);
	}

	/**
	 * Allows to add a road between two intersections to the map
	 * @param road the road to add to the map This method does not check if the road is valid. That is if there are 
	 * really two intersections already in the map from the origin towards the destination of the Road given as 
	 * parameter.
	 */
	public void addRoad(Road r) {
		ArrayList<Road> roadListFromOrigin = getRoadsStartingFrom(r.getOrigin());
		if (roadListFromOrigin == null) {
			roadListFromOrigin = new ArrayList<Road>();
		}
		roadListFromOrigin.add(r);
		roads.put(r.getOrigin(), roadListFromOrigin);
	}

	
	public HashMap<Integer, Intersection> getIntersections() {
		return intersections;
	}

	public void setIntersections(HashMap<Integer, Intersection> intersections) {
		this.intersections = intersections;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Map [intersections = " + getIntersections() + ", roads = " + roads + "]";
	}
	
	/**
	 * Default constructor. No intersection or road exists in the map yet.
	 */
	public Map() {
		this.intersections = new HashMap<Integer, Intersection>();
		this.roads = new HashMap<Integer, ArrayList<Road>>();
	}
}
