package com.hexagone.delivery.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;

/**
 * This class provides the algorithms needed to compute the complete time graph
 * between the different delivery points of a map. It delivers this information
 * as an adjacency matrix (2D array in our case) It does not perform any check
 * on the validity of the input parameters.
 */
class CompleteGraphComputer {
	/**
	 * The main goal of this class is to apply the Dijkstra algorithm to obtain
	 * an adjacent matrix to then compute the most time efficient way around the
	 * different passage points given in the deliveryQuery
	 */

	private Map map;
	private DeliveryQuery deliveryQuery;

	private Double[][] adjacencyMatrix;
	private HashMap<Integer, HashMap<Integer, Integer>> previousIntersection;

	/**
	 * Gives back an arrayList of the intersections that one needs to follow to
	 * get the shortest path between origin and destination. Method getAdjacency
	 * matrix has to be called at least once before calling this method. The
	 * origin and destination parameters have to be identifiers of intersections
	 * present in the deliveryQuery this was constructed with
	 * 
	 * @param origin
	 *            the origin intersection where one starts its journey
	 * @param destination
	 *            the destination where one wants to go
	 * @return an arrayList of integers containing in the correct order :
	 *         origin, [other intersections,] destination
	 */
	public ArrayList<Integer> getIntersectionPath(Integer origin, Integer destination) {
		ArrayList<Integer> intersections = new ArrayList<Integer>();

		intersections.add(0, destination);

		HashMap<Integer, Integer> prev = previousIntersection.get(origin);

		Integer currentIntersection = prev.get(destination);
		while (!currentIntersection.equals(origin)) {
			intersections.add(0, currentIntersection);
			currentIntersection = prev.get(currentIntersection);
		}
		intersections.add(0, currentIntersection);

		return intersections;
	}

	/**
	 * Gives back the adjacency matrix of the Map / Delivery query combination
	 * given in the constructor of the game
	 * 
	 * @return the adjacency matrix as a 2D array of Double. The time unit is
	 *         second
	 */
	public Double[][] getAdjacencyMatrix() {
		if (adjacencyMatrix != null) {
			return adjacencyMatrix;
		}

		/** Creation of the adjacency matrix */
		int nbPassagePoints = deliveryQuery.getPassagePointsNumber();
		adjacencyMatrix = new Double[nbPassagePoints][];
		previousIntersection = new HashMap<>();

		Integer[] passageIntersections = deliveryQuery.getDeliveryPassageIdentifiers();

		/** We compute the cost of going to each node from each node */
		for (int i = 0; i < passageIntersections.length; i++) {
			int numberOfIntersections = map.getIntersections().size();
			HashMap<Integer, Double> cost = new HashMap<Integer, Double>(numberOfIntersections);
			previousIntersection.put(passageIntersections[i], new HashMap<Integer, Integer>(numberOfIntersections));

			computeCosts(passageIntersections[i], previousIntersection.get(passageIntersections[i]), cost);

			/** We store the previous node information for later use */

			Double[] adjacencyLine = new Double[nbPassagePoints];
			for (int j = 0; j < nbPassagePoints; j++) {
				adjacencyLine[j] = cost.get(passageIntersections[j]);
			}
			adjacencyMatrix[i] = adjacencyLine;
			cost.clear();
		}

		/** Return */
		return adjacencyMatrix;
	}

	/**
	 * Allows to compute the costs of going from intersection 'Intersection' to
	 * all the other points in the map The result is stored in the HashMap cost.
	 * The HashMap previous stores the Intersection from which one needs to come
	 * from to go by the shortest path.
	 * 
	 * @param map
	 *            the map in which the problem takes place
	 * @param intersection
	 *            the starting intersection identifier
	 * @param previousIntersection
	 *            hashMap that will contain for each Intersection the
	 *            Intersection one needs to come from
	 * @param cost
	 *            the hashMap that will contain the costs of going from
	 *            intersection to each node
	 */
	void computeCosts(Integer intersection, HashMap<Integer, Integer> previousIntersection,
			HashMap<Integer, Double> cost) {
		/** Set of the non-visited nodes */
		HashSet<Integer> nonVisitedNodes = map.getAllIntersectionIdentifiers();

		/** Cost array initialization */
		cost.put(intersection, new Double(0));

		/** Beginning of the computation */
		while (!nonVisitedNodes.isEmpty()) {
			/**
			 * We select the node with the smallest 'distance' so far. In the
			 * first iteration, the origin node is selected
			 */
			intersection = smallestCost(cost, nonVisitedNodes);

			/** We visit this intersection */
			nonVisitedNodes.remove(intersection);

			/**
			 * we go through each of the available neighbors starting from the
			 * current intersection
			 */
			ArrayList<Road> neighbours = map.getRoadsStartingFrom(intersection);
			for (Road road : neighbours) {
				/**
				 * We check if the destination road is still inside the
				 * non-visited nodes
				 */
				Integer destination = road.getDestination();
				if (nonVisitedNodes.contains(destination)) {
					Double costToDestination = cost.get(intersection) + road.getTime();

					/**
					 * If we found a shorter path towards destination, we
					 * replace the cost in the cost map
					 */
					if (!cost.containsKey(destination) || cost.get(destination) > costToDestination) {
						cost.put(destination, costToDestination);
						previousIntersection.put(destination, intersection);
					}
				}
			}
		}
	}

	/**
	 * This method gives the key of the smallest element in the array
	 * 
	 * @param array
	 *            the array from which one wants to find the minimum
	 * @return the index of the minimum element in the array as an integer
	 */
	static Integer smallestCost(HashMap<Integer, Double> hashMap, HashSet<Integer> keyCandidates) {
		Iterator<Integer> keySetIterator = keyCandidates.iterator();
		Integer key = keySetIterator.next();
		Double smallestCost = hashMap.getOrDefault(key, Double.MAX_VALUE);
		while (keySetIterator.hasNext()) {
			Integer newKey = keySetIterator.next();
			Double newCost = hashMap.getOrDefault(newKey, Double.MAX_VALUE);
			if (newCost < smallestCost) {
				key = newKey;
				smallestCost = newCost;
			}
		}
		return key;
	}

	/**
	 * Constructor for the CompleteGraphComputer
	 * 
	 * @param map
	 *            the map on which the deliveries are going to take place
	 * @param deliveryQuery
	 *            the deliveryQuery to make on the map
	 */
	public CompleteGraphComputer(Map map, DeliveryQuery deliveryQuery) {
		this.map = map;
		this.deliveryQuery = deliveryQuery;
	}

}
