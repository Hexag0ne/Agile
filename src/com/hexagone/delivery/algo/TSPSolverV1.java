package com.hexagone.delivery.algo;

import java.util.ArrayList;
import java.util.Iterator;

public class TSPSolverV1 extends TSPSolver {

	/**
	 * 
	 * @param costsAdjacencyMatrix
	 * @param stayingTime
	 */
	public TSPSolverV1(Integer[][] costsAdjacencyMatrix, Integer[] stayingTime) {
		super(costsAdjacencyMatrix, stayingTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hexagone.delivery.algo.TSPSolver#bound(java.lang.Integer,
	 * java.util.ArrayList, java.lang.Integer[][], java.lang.Integer[])
	 */
	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, Integer[][] cout, Integer[] duree) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hexagone.delivery.algo.TSPSolver#iterator(java.lang.Integer,
	 * java.util.ArrayList, java.lang.Integer[][], java.lang.Integer[])
	 */
	@Override
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, Integer[][] cout,
			Integer[] duree) {
		return nonVus.iterator();
	}

}
