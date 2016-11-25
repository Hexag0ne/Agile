package com.hexagone.delivery.algo;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class TSPv1Test {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testComputation() {
		Double[][] costs = new Double [3][3];
		costs[0][0] = 0.0;
		costs[0][1] = 1.0;
		costs[0][2] = 2.0;
		costs[1][0] = 2.0;
		costs[1][1] = 0.0;
		costs[1][2] = 1.0;
		costs[2][0] = 1.0;
		costs[2][1] = 2.0;
		costs[2][2] = 0.0;
		
		//Expected path is 0 -> 1 -> 2 -> 0
		Integer [] stay = new Integer[3];
		stay[0] = 0;
		stay[1] = 0;
		stay[2] = 0;
		
		TSPSolverV1 solver = new TSPSolverV1(costs, stay);
		
		solver.computeSolution();
		
		ArrayList<Integer> bestPath = solver.getBestSolution();
		
		assertEquals(new Integer(0), bestPath.get(0));
		assertEquals(new Integer(1), bestPath.get(1));
		assertEquals(new Integer(2), bestPath.get(2));
	}

}
