package com.hexagone.delivery.algo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;

/**
 * This class provides the basic interface and implementation for the Traveling
 * Salesman Problem faced in this software
 */
abstract class TSPSolver {

	private static final long TIMELIMIT = 30000;

	private ArrayList<Integer> bestSolution = new ArrayList<Integer>();
	private Calendar bestSolutionCost;
	private Boolean timeLimitReached = false;

	private DeliveryQuery deliveryQuery;
	private Double[][] costs;
	private Integer[] stayTime;

	private ArrayList<Integer> unseenIntersections;
	private ArrayList<Integer> seenIntersections;

	/**
	 * Gives back the best solution found so far during the computing of the TSP
	 * problem
	 * 
	 * @return the best solution found so far
	 */
	public ArrayList<Integer> getBestSolution() {
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
		Calendar pathCost = GregorianCalendar.getInstance();
		pathCost.setTime(deliveryQuery.getWarehouse().getDepartureTime());
		branchAndBound(0, pathCost, System.currentTimeMillis());
	}

	/**
	 * This methods defines the template of a solution (obtained by branch and
	 * bound of TSP)
	 * 
	 * @param currentIntersection
	 *            the current intersection
	 * @param pathCost
	 *            the cost of the path as a Calendar Object
	 * @param tpsDebut
	 *            : the time of the beginning of the calculation
	 * 
	 */
	private void branchAndBound(int currentIntersection, Calendar pathCost, long tpsDebut) {
		/**
		 * The computation has been going on for too long, the algorithm stops
		 */

		if (System.currentTimeMillis() - tpsDebut > TIMELIMIT) {
			timeLimitReached = true;
			return;
		}

		if (unseenIntersections.size() == 0) { // All intersections have been
												// visited
			// We add the cost to go back to the warehouse
			pathCost.add(Calendar.SECOND, Integer.valueOf(costs[currentIntersection][0].intValue()));
			if (bestSolutionCost == null || pathCost.compareTo(bestSolutionCost) < 0) { // We
																						// found
																						// a
																						// new
																						// better
																						// solution
				// !
				bestSolution.clear();
				// We store the solution (the order of visiting)
				for (Integer i : seenIntersections) {
					bestSolution.add(i);
				}

				bestSolutionCost = pathCost;
			}
		} else if (betterPathPossible(pathCost, currentIntersection, unseenIntersections)) {
			// If there still is a chance of finding a better solution with this
			// combination
			Iterator<Integer> it = iterator((Integer) currentIntersection, unseenIntersections, costs, stayTime);
			while (it.hasNext()) {
				Integer prochainSommet = it.next();
				seenIntersections.add(prochainSommet);
				unseenIntersections.remove(prochainSommet);

				/** Recursive call */
				Calendar timeReachingNextIntersection = (Calendar) pathCost.clone();
				timeReachingNextIntersection.add(Calendar.SECOND,
						Integer.valueOf(costs[currentIntersection][prochainSommet].intValue()));

				// If we arrive before the opening window, we wait there
				// try / catch because of no opening window case
				try {
					Date openingNextSchedule = deliveryQuery.getDeliveries()[prochainSommet - 1].getStartSchedule();
					Calendar nextSchedule = GregorianCalendar.getInstance();
					nextSchedule.setTime(openingNextSchedule);
					if (openingNextSchedule != null && timeReachingNextIntersection.compareTo(nextSchedule) < 0) {
						timeReachingNextIntersection.setTime(openingNextSchedule);
					}
				} catch (NullPointerException e) {

				}

				timeReachingNextIntersection.add(Calendar.SECOND, stayTime[prochainSommet]);

				branchAndBound(prochainSommet, timeReachingNextIntersection, tpsDebut);

				seenIntersections.remove(prochainSommet);
				unseenIntersections.add(prochainSommet);
			}
		}
	}

	private int getNumberOfIntersections() {
		return costs.length;
	}

	/**
	 * Method computing if a better path than the current best solution is
	 * possible. It also checks if upon arriving on the currentIntersection, the
	 * delivery does not overflow the end of the delivery on that intersection
	 * 
	 * @param pathCost
	 *            the cost so far till the currentIntersection
	 * @param currentIntersection
	 *            the current Intersection being visited
	 * @param unseenIntersections
	 *            the Array of intersections yet to visit
	 * @return true if it is worth keeping the computation going
	 */
	private boolean betterPathPossible(Calendar pathCost, int currentIntersection,
			ArrayList<Integer> unseenIntersections) {
		Calendar bestTimePossible = GregorianCalendar.getInstance();
		bestTimePossible.setTime(pathCost.getTime());
		bestTimePossible.add(Calendar.SECOND, bound(currentIntersection, unseenIntersections, costs, stayTime));
		boolean costPotentiallySmaller = bestSolutionCost == null || bestSolutionCost.compareTo(bestTimePossible) > 0;

		boolean noTimeWindowMissed = true;

		if (!(currentIntersection == 0)) { // i.e. if we are not at the
											// warehouse.

			try {
				Delivery[] deliveries = deliveryQuery.getDeliveries();
				// TODO add a try/catch ?
				Calendar deliveryWindowEnd = GregorianCalendar.getInstance();

				deliveryWindowEnd.setTime(deliveries[currentIntersection - 1].getEndSchedule());

				noTimeWindowMissed = pathCost.compareTo(deliveryWindowEnd) < 0; // True
																				// if
																				// arrivalTime
																				// before
																				// deliverWindowEnd

			} catch (NullPointerException e) {
			}
		}

		return costPotentiallySmaller && noTimeWindowMissed;
	}

	/**
	 * This method gives the lower bound of the permutations cost
	 * 
	 * @param the intersection at which the algorithm is currently stopped
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
	protected abstract int bound(Integer currentIntersection, ArrayList<Integer> nonVus, Double[][] cout, Integer[] duree);

	/**
	 * This method provides an iterator for nonVus
	 * 
	 * @param currentIntersection the intersection at which the user is currently stopped
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
	protected abstract Iterator<Integer> iterator(Integer currentIntersection, ArrayList<Integer> nonVus, Double[][] cout,
			Integer[] duree);

	/**
	 * Constructor
	 * 
	 * @param costsAdjacencyMatrix
	 *            the adjacency matrix of the graph. costsAdjacencyMatrix[i][j]
	 *            represents the cost going from i to j.
	 * @param deliveryQuery
	 *            the deliveries to make.
	 */
	public TSPSolver(Double[][] costsAdjacencyMatrix, DeliveryQuery deliveryQuery) {
		costs = costsAdjacencyMatrix;

		this.deliveryQuery = deliveryQuery;

		Delivery[] deliveries = deliveryQuery.getDeliveries();
		int lenght = deliveryQuery.getDeliveryPassageIdentifiers().length;

		stayTime = new Integer[lenght];
		int i = 1;
		for (Delivery d : deliveries) {
			stayTime[i] = d.getDuration();
			i++;
		}
	}

}
