package com.hexagone.delivery.algo;

import java.util.ArrayList;

import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;

/**
 * This class provides the algorithms needed to calculate the delivery points,
 * to get the shortest path of the delivery. It also checks that there is no
 * empty solution and no timeout.
 * 
 */

public class DeliveryComputer {

	private DeliveryQuery deliveryQuery;
	private ArrayList<Integer> deliveryIntersections;

	private CompleteGraphComputer graphComputer;
	private TSPSolver tspSolver;

	
	public void compute(){
		Double[][] costsAdjacencyMatrix = graphComputer.getAdjacencyMatrix();

		tspSolver = new TSPSolverV1(costsAdjacencyMatrix, deliveryQuery);
		tspSolver.computeSolution();
	}
	
	/**
	 * 
	 * This methods calculates the intersections of the delivery in the right
	 * order.
	 * 
	 * @param map
	 *            the map in which the problem takes place
	 * @param deliveryQuery
	 *            the delivery query
	 * 
	 * @return deliveryIntersections the list of intersections of the delivery
	 *         in order
	 * 
	 * 
	 */
	public ArrayList<Integer> getDeliveryPoints() {
		if (deliveryIntersections.isEmpty()) {
			
			ArrayList<Integer> order = tspSolver.getBestSolution();

			Delivery[] deliveries = deliveryQuery.getDeliveries();
			int length = deliveryQuery.getDeliveryPassageIdentifiers().length;

			deliveryIntersections = new ArrayList<Integer>();
			deliveryIntersections.add(deliveryQuery.getWarehouse().getIntersection().getId());
			for (int j = 1; j < length; j++) {
				deliveryIntersections.add(deliveries[order.get(j) - 1].getIntersection().getId());
			}
			deliveryIntersections.add(deliveryQuery.getWarehouse().getIntersection().getId());
		}
		return deliveryIntersections;
	}

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
	public ArrayList<Integer> getShortestPath(Integer origin, Integer destination) {
		return graphComputer.getIntersectionPath(origin, destination);
	}

	/**
	 * Constructor of the DeliveryComputer
	 * 
	 * @param map
	 *            the map of the problem
	 * @param deliveryQuery
	 *            the deliveryQuery to make on the
	 */
	public DeliveryComputer(Map map, DeliveryQuery deliveryQuery) {
		this.deliveryQuery = deliveryQuery;
		this.deliveryIntersections = new ArrayList<Integer>();

		this.graphComputer = new CompleteGraphComputer(map, deliveryQuery);
	}

	/**
	 * 
	 * Check if the path has been calculated (ie. if bestSolution is not empty)
	 * 
	 * @return True if bestSolution is not empty
	 * 
	 **/
	public boolean checkNotEmptySolution() {
		return !tspSolver.getBestSolution().isEmpty();
	}

	/**
	 * 
	 * Check if the time limit has been reached
	 * 
	 * @return True if there is a time out (ie. the time limit has been reached)
	 * 
	 **/
	public boolean checkTimeout() {
		return tspSolver.timeLimitReached();
	}

}
