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
public class CompleteGraphComputer {
	/**
	 * The main goal of this class is to apply the Dijkstra alogrithm to obtain
	 * an adjacent matrix to then compute the most tume efficient way around the
	 * different passage points given in the deliveryQuery
	 */

	public static Integer[][] getAdjacencyMatrix(Map map, DeliveryQuery deliveryQuery) {
		/** Creation of the adjacency matrix */
		int nbPassagePoints = deliveryQuery.getPassagePointsNumber();
		Integer[][] adjacencyMatrix = new Integer[nbPassagePoints][];

		Integer[] passageIntersections = deliveryQuery.getDeliveryPassageIdentifiers();

		/** We compute the cost of going to each node from each node */
		for (int i = 0; i < passageIntersections.length; i++) {
			int numberOfIntersections = map.getIntersections().size();
			HashMap<Integer,Integer> cost = new HashMap<Integer,Integer>(numberOfIntersections);
			HashMap<Integer,Integer> previousIntersection = new HashMap<Integer,Integer>(numberOfIntersections);
						
			computeCosts(map, passageIntersections[i], previousIntersection, cost);
			
			Integer [] adjacencyLine = new Integer [nbPassagePoints];
			for (int j = 0; j < nbPassagePoints; j++)
			{
				adjacencyLine[j] = cost.get(passageIntersections[j]);
			}
			adjacencyMatrix[i] = adjacencyLine;
		}

		/** Return */
		return adjacencyMatrix;
	}

	private static void computeCosts(Map map, Integer intersection, 
			HashMap<Integer, Integer> previousIntersection, HashMap<Integer,Integer> cost) {
		/** Set of the non-visited nodes */
		HashSet<Integer> nonVisitedNodes = map.getAllIntersectionIdentifiers();
		
		/** Cost array initialisation */
		cost.put(intersection, 0);
		
		/** Beginning of the computation */
		while (!nonVisitedNodes.isEmpty()) {
			/**
			 * We select the node with the smallest 'distance' so far. In the
			 * first iteration, the origin node is selected
			 */
			intersection = smallestCost(cost);

			/** We visit this intersection */
			nonVisitedNodes.remove(intersection);

			/**
			 * we go through each of the available neighbours starting from the
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
					Integer costToDestination = cost.get(intersection) + road.getTime();

					/**
					 * If we found a shorter path towards destination, we
					 * replace the cost in the cost map
					 */
					if (!cost.containsKey(destination) ||  cost.get(destination) > costToDestination) {
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
	 *            the array from whih one wants to find the minimum
	 * @return the index of the minimum element in the array as an int
	 */
	static Integer smallestCost(HashMap<Integer,Integer> hashMap) {
		Iterator<Integer> keySetIterator = hashMap.keySet().iterator();
		Integer key = keySetIterator.next();
		Integer smallestCost = hashMap.get(key);
		while (keySetIterator.hasNext())
		{
			Integer newKey = keySetIterator.next();
			Integer newCost = hashMap.get(newKey);
			if (newCost < smallestCost)
			{
				key = newKey;
				smallestCost = newCost;
			}
		}
		return key;
	}
}
