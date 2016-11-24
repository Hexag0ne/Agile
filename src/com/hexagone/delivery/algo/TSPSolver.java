package com.hexagone.delivery.algo;

import java.util.ArrayList;
import java.util.Iterator;

import javax.naming.TimeLimitExceededException;

/**
 * This class provides the basic interface and implementation for the Travelling
 * Salesman Problem faced in this software
 */
public abstract class TSPSolver {

	private static final long TIMELIMIT = 30000;

	private Integer[] bestSolution;
	private Integer bestSolutionCost = Integer.MAX_VALUE;
	private Boolean timeLimitReached = false;

	private Integer[][] costs;
	private Integer[] stayTime;

	private ArrayList<Integer> unseenIntersections;
	private ArrayList<Integer> seenIntersections;

	/**
	 * Gives back the best solution found so far during the computing of the TSP
	 * problem
	 * 
	 * @return the best solution found so far
	 */
	public Integer[] getBestSolution() {
		return bestSolution;
	}

	/**
	 * Indicates if the time limit was reached during the last atempt to compute
	 * the best TSP path
	 * 
	 * @return true if the time limit was reached during the lat computation of
	 *         the best path, false otherwise.
	 */
	public Boolean timeLimitReached() {
		return timeLimitReached;
	}

	/**
	 * This method will launch the computation of a solution to the TSP problem.
	 * If the computation time exceeds the time limit, the computation will stop
	 * and the method timeLimitReached of this class will return true.
	 * 
	 * @see #TIMELIMIT
	 * @see #getBestSolution()
	 * @see #timeLimitReached
	 */
	public void computeSolution() {
		unseenIntersections = new ArrayList<Integer>(getNumberOfIntersections());
		for (int i = 1; i < getNumberOfIntersections(); i++) {
			unseenIntersections.add(new Integer(i));
		}

		seenIntersections = new ArrayList<Integer>(costs.length);
		seenIntersections.add(new Integer(0));
		branchAndBound(0, 0, System.currentTimeMillis());
	}

	/**
	 * Methode definissant le patron (template) d'une resolution par separation
	 * et evaluation (branch and bound) du TSP
	 * 
	 * @param sommetCrt
	 *            le dernier sommet visite
	 * @param coutVus
	 *            la somme des couts des arcs du chemin passant par tous les
	 *            sommets de vus + la somme des duree des sommets de vus
	 * @param tpsDebut
	 *            : moment ou la resolution a commence
	 * @param tpsLimite
	 *            : limite de temps pour la resolution
	 */
	private void branchAndBound(int sommetCrt, int coutVus, long tpsDebut) {
		/**
		 * The computation has been going on for too long, the algorithm stops
		 * here
		 */
		if (System.currentTimeMillis() - tpsDebut > TIMELIMIT) {
			timeLimitReached = true;
			return;
		}
		if (unseenIntersections.size() == 0) { // All intersections have been
												// visited
			// We add the cost to go back to the warehouse
			coutVus += costs[sommetCrt][0];
			if (coutVus < bestSolutionCost) { // We found a new better solution
												// !
				seenIntersections.toArray(bestSolution); // We store the
															// solution (the
															// order of
															// visiting)
				bestSolutionCost = coutVus;
			}
		} else if (coutVus + bound(sommetCrt, unseenIntersections, costs, stayTime) < bestSolutionCost) { // If
																											// there
																											// still
																											// is
																											// a
																											// chance
																											// of
																											// finding
																											// a
																											// better
																											// solution
																											// with
																											// this
																											// combination
			Iterator<Integer> it = iterator((Integer) sommetCrt, unseenIntersections, costs, stayTime);
			while (it.hasNext()) {
				Integer prochainSommet = it.next();
				seenIntersections.add(prochainSommet);
				unseenIntersections.remove(prochainSommet);

				/** Reecursive call */
				branchAndBound(prochainSommet, coutVus + costs[sommetCrt][prochainSommet] + stayTime[prochainSommet],
						tpsDebut);

				seenIntersections.remove(prochainSommet);
				unseenIntersections.add(prochainSommet);
			}
		}
	}

	private int getNumberOfIntersections() {
		return costs.length;
	}

	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * 
	 * @param sommetCourant
	 * @param nonVus
	 *            : tableau des sommets restant a visiter
	 * @param cout
	 *            : cout[i][j] = duree pour aller de i a j, avec 0 <= i <
	 *            nbSommets et 0 <= j < nbSommets
	 * @param duree
	 *            : duree[i] = duree pour visiter le sommet i, avec 0 <= i <
	 *            nbSommets
	 * @return une borne inferieure du cout des permutations commencant par
	 *         sommetCourant, contenant chaque sommet de nonVus exactement une
	 *         fois et terminant par le sommet 0
	 */
	protected abstract int bound(Integer sommetCourant, ArrayList<Integer> nonVus, Integer[][] cout, Integer[] duree);

	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * 
	 * @param sommetCrt
	 * @param nonVus
	 *            : tableau des sommets restant a visiter
	 * @param cout
	 *            : cout[i][j] = duree pour aller de i a j, avec 0 <= i <
	 *            nbSommets et 0 <= j < nbSommets
	 * @param duree
	 *            : duree[i] = duree pour visiter le sommet i, avec 0 <= i <
	 *            nbSommets
	 * @return un iterateur permettant d'iterer sur tous les sommets de nonVus
	 */
	protected abstract Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, Integer[][] cout,
			Integer[] duree);

	/**
	 * Constructor
	 * 
	 * @param costsAdjacencyMatrix
	 */
	public TSPSolver(Integer[][] costsAdjacencyMatrix, Integer[] stayingTime) {
		this.costs = costsAdjacencyMatrix;
		this.stayTime = stayingTime;
	}

}
