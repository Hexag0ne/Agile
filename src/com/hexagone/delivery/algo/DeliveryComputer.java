package com.hexagone.delivery.algo;

import java.util.ArrayList;

import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.algo.TSPSolver;
import com.hexagone.delivery.algo.CompleteGraphComputer;

public class DeliveryComputer {

	private Map map;
	private DeliveryQuery deliveryQuery;
	private ArrayList<Integer> deliveryIntersections;
	private boolean executed;

	public DeliveryComputer(Map map, DeliveryQuery deliveryQuery) {
		this.map = map;
		this.deliveryQuery = deliveryQuery;
		this.executed = false;
	}

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
	public ArrayList<Integer> getDeliveryPoints(Map map, DeliveryQuery deliveryQuery,
			ArrayList<Integer> deliveryIntersections) {
		if (executed) {
			Double[][] costsAdjacencyMatrix = CompleteGraphComputer.getAdjacencyMatrix(map, deliveryQuery);

			Delivery[] deliveries = deliveryQuery.getDeliveries();
			int lenght = deliveryQuery.getDeliveryPassageIdentifiers().length;

			Integer[] stayingTime = new Integer[lenght];
			int i = 1;
			for (Delivery d : deliveries) {
				stayingTime[i] = d.getDuration();
				i++;
			}
			TSPSolverV1 tspSolver = new TSPSolverV1(costsAdjacencyMatrix, stayingTime);
			tspSolver.computeSolution();

			ArrayList<Integer> order = tspSolver.getBestSolution();
			deliveryIntersections = new ArrayList<Integer>();

			deliveryIntersections.set(0, deliveryQuery.getWarehouse().getIntersection().getId());
			deliveryIntersections.set(lenght, deliveryQuery.getWarehouse().getIntersection().getId());
			for (int j = 1; j < lenght; j++) {
				deliveryIntersections.set(j, deliveries[order.get(j) - 1].getIntersection().getId());

			}
			executed = true;
		}
		return deliveryIntersections;
	}
}
