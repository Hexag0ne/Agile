package com.hexagone.delivery.algo;

import java.util.ArrayList;
import java.util.HashSet;

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
			adjacencyMatrix[i] = computeCosts(map, passageIntersections, i);
		}

		/** Return */
		return adjacencyMatrix;
	}

	private static Integer[] computeCosts(Map map, Integer[] passageIntersections, int originIndex) {
		/** Initialisation of the two piles of nodes */
		/** Set of the non-visited nodes */
		HashSet<Integer> nonVisitedNodes = map.getAllIntersectionIdentifiers();

		/** Cost array initialisation */
		Integer[] costs = new Integer[passageIntersections.length];
		for (int i = 0; i < costs.length; i++) {
			costs[i] = Integer.MAX_VALUE;
		}
		costs[originIndex] = 0;

		/** Beginning of the computation */
		while (!nonVisitedNodes.isEmpty()) {
			/**
			 * We select the node with the smallest 'distance' so far. In the
			 * first iteration, the origin node is selected
			 */
			int indexSmallest = smallestCostIndex(costs);
			Integer currentIntersection = passageIntersections[indexSmallest];

			/** We visit this intersection */
			nonVisitedNodes.remove(currentIntersection);

			/**
			 * we go through each of the available neighbours starting from the
			 * current intersection
			 */
			ArrayList<Road> neighbours = map.getRoadsStartingFrom(currentIntersection);
			for (Road road : neighbours) {
				/**
				 * We check if the destination road is still inside the
				 * non-visited nodes
				 */
				Integer destination = road.getDestination();
				if (nonVisitedNodes.contains(destination)) {
					int time = costs[indexSmallest] + road.getTime();
					int indiceDestination = indexOf(passageIntersections, destination);

					/**
					 * If we found a shorter path towards destination, we
					 * replace the cost in the cost array
					 */
					if (costs[indiceDestination] > time) {
						costs[indiceDestination] = time;
					}
				}
			}
		}

		return costs;
	}

	/**
	 * This method gives the index of the smallest element in the array
	 * 
	 * @param array
	 *            the array from whih one wants to find the minimum
	 * @return the index of the minimum element in the array as an int
	 */
	private static int smallestCostIndex(Integer[] array) {
		int index = 0;
		Integer currentMinimum = Integer.MAX_VALUE;
		for (int i = 0; i < array.length; i++) {
			if (currentMinimum > array[i]) {
				index = i;
				currentMinimum = array[i];
			}
		}
		return index;
	}

	/**
	 * This method gives the index in an array of the specified object
	 * 
	 * @param array
	 *            the array in which the object is contained
	 * @param Object
	 *            the object one is looking for its index
	 * @return
	 */
	static int indexOf(Object[] array, Object Object) {
		int index = 0;
		for (; index < array.length; index++) {
			if (array[index].equals(Object)) {
				break;
			}
		}

		return index;
	}
}
