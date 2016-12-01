package com.hexagone.delivery.algo;

import java.util.ArrayList;

import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;

public class DeliveryComputer {

	private DeliveryQuery deliveryQuery;
	private ArrayList<Integer> deliveryIntersections;
	
	private CompleteGraphComputer graphComputer;
	private TSPSolver tspSolver;

	/**
	 * 
	 * This methods computes the adjacency matrix
	 * 
	 * @param map
	 *            the map in which the problem takes place
	 * @param deliveryQuery
	 *            the delivery query
	 * 
	 * @return deliveryIntersections the list of intersections of the delivery
	 * 
	 * 
	 */
	public ArrayList<Integer> getDeliveryPoints() {
		if (deliveryIntersections.isEmpty()) {
			Double[][] costsAdjacencyMatrix = graphComputer.getAdjacencyMatrix();
			
			tspSolver = new TSPSolverV1(costsAdjacencyMatrix, deliveryQuery);
			tspSolver.computeSolution();

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
	 * Constructor of the DeliveryComputer
	 * @param map the map of the problem
	 * @param deliveryQuery the deliveryQuery to make on the 
	 */
	public DeliveryComputer(Map map, DeliveryQuery deliveryQuery) {
		this.deliveryQuery = deliveryQuery;
		this.deliveryIntersections = new ArrayList<Integer>();
		
		this.graphComputer = new CompleteGraphComputer(map, deliveryQuery);
	}
}
