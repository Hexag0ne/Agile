package com.hexagone.delivery.algo;

import java.util.ArrayList;
import java.util.Iterator;

import com.hexagone.delivery.models.DeliveryQuery;

/**
 * This class extends TSPSolver. It provides the methods needed to get the lower
 * bound of the permutations cost and to get an iterator on unseen intersections
 * 
 */
class TSPSolverV1 extends TSPSolver {

	/**
	 * Allows to create an instance of TSP solver to solve the Traveling
	 * Salesman Problem
	 * 
	 * @param costsAdjacencyMatrix
	 *            the adjacency matrix of the graph. costsAdjacencyMatrix[i][j]
	 *            represents the cost going from i to j.
	 * @param deliveryQuery
	 *            the deliveries to make.
	 */
	public TSPSolverV1(Double[][] costsAdjacencyMatrix, DeliveryQuery deliveryQuery) {
		super(costsAdjacencyMatrix, deliveryQuery);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hexagone.delivery.algo.TSPSolver#bound(java.lang.Integer,
	 * java.util.ArrayList, java.lang.Integer[][], java.lang.Integer[])
	 */

	/**
	 * This method gives the lower bound of the permutations cost
	 * 
	 * @param sommetCourant
	 * @param nonVus
	 *            : table of the unvisited intersections
	 * @param cout
	 *            : cout[i][j] = duration to go from i to j with 0 <= i <
	 *            nbSommets and 0 <= j < nbSommets
	 * @param duree
	 *            : duree[i] = duration to visit intersection i, with 0 <= i <
	 *            nbSommets
	 * @return a lower bound of the permutations cost starting with
	 *         sommetCourant, including each 'nonVus' intersection exactly once
	 *         and ending with intersection 0
	 */
	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, Double[][] cout, Integer[] duree) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hexagone.delivery.algo.TSPSolver#iterator(java.lang.Integer,
	 * java.util.ArrayList, java.lang.Integer[][], java.lang.Integer[])
	 */

	/**
	 * This method provides an iterator for nonVus
	 * 
	 * @param sommetCrt
	 * @param nonVus
	 *            : table of the intersections that have not been visited yet
	 * @param cout
	 *            : cout[i][j] = duration to go from i to j, with 0 <= i <
	 *            nbSommets and 0 <= j < nbSommets
	 * @param duree
	 *            : duree[i] = duration to visit intersection i, with 0 <= i
	 *            <nbSommets
	 * @return an iterator that allows us to iterate on all of the 'nonVus'
	 *         intersections
	 */
	@Override
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, Double[][] cout,
			Integer[] duree) {
		ArrayList<Integer> arrayList = (ArrayList<Integer>) nonVus.clone();
		return arrayList.iterator();
	}

}
