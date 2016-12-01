package com.hexagone.delivery.algo;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.Warehouse;

public class CompletGraphComputerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSmallestCost() throws Exception {
		HashMap<Integer,Double> map = new HashMap<Integer,Double>(2);
		HashSet<Integer> keys = new HashSet<>();
		map.put(new Integer(0), new Double(Integer.MAX_VALUE));
		map.put(new Integer(45), new Double(5));
		keys.add(new Integer(45));
		
		Integer test = CompleteGraphComputer.smallestCost(map, keys);
		
		assertEquals(new Integer(45),test);
	}
	
	@Test
	public void testDijkstra() throws Exception {
		/** Map in a square shape */		
		Map map = new Map();
		map.addIntersection(new Intersection(0, 0, 0));
		map.addIntersection(new Intersection(1, 1, 0));
		map.addIntersection(new Intersection(2, 1, 1));
		map.addIntersection(new Intersection(3, 0, 1));
		
		map.addRoad(new Road(0, 1, 1, 1, "r0"));
		map.addRoad(new Road(1, 2, 1, 1, "r1"));
		map.addRoad(new Road(2, 3, 1, 1, "r2"));
		map.addRoad(new Road(3, 0, 1, 1, "r3"));
		
		HashMap<Integer,Integer> prevInter = new HashMap<Integer,Integer>();
		HashMap<Integer,Double> cost = new HashMap<Integer,Double>();
		
		CompleteGraphComputer graphComputer = new CompleteGraphComputer(map, null);
		
		graphComputer.computeCosts(new Integer(0), prevInter, cost);
		
		// Checking the times needed to go to each intersections
		assertEquals(new Double(0), cost.get(new Integer(0)));
		assertEquals(new Double(0.36), cost.get(new Integer(1)));
		assertEquals(new Double(0.72), cost.get(new Integer(2)));
		assertEquals(new Double(1.08), cost.get(new Integer(3)));
		// Checking the path to go to the various intersecitons
		assertEquals(new Integer(0), prevInter.get(new Integer(1))); //To go to 1, come from 0
		assertEquals(new Integer(1), prevInter.get(new Integer(2)));
		assertEquals(new Integer(2), prevInter.get(new Integer(3)));
		
		// Map modification to expect different resulsts
		map.addRoad(new Road(0, 3, 1, 1, "r3"));
		map.addRoad(new Road(3, 2, 1, 1, "r2"));
		
		prevInter.clear();
		cost.clear();
		
		graphComputer = new CompleteGraphComputer(map, null);
		graphComputer.computeCosts(0, prevInter, cost);
		
		assertEquals(new Double(0), cost.get(new Integer(0)));
		assertEquals(new Double(0.36), cost.get(new Integer(1)));
		assertEquals(new Double(0.72), cost.get(new Integer(2)));
		assertEquals(new Double(0.36), cost.get(new Integer(3)));
		
		assertEquals(new Integer(0), prevInter.get(new Integer(1)));
		assertEquals(new Integer(0), prevInter.get(new Integer(3)));
		
		// Draw situation : coming from 1 or 3 yields the same cost.
		// The 
		assertTrue(prevInter.get(new Integer(2)).equals(new Integer(1)) || 
				prevInter.get(new Integer(2)).equals(new Integer(3)));
				
	}
	
	/**
	 * Test of the method getAdjacencyMatrix 
	 * @throws Exception
	 * @see {@link CompleteGraphComputer}{@link #testGetAdjacencyMatrix()}
	 */
	@Test
	public void testGetAdjacencyMatrix() throws Exception {
		Map map = new Map();
		Intersection startInter = new Intersection(0, 0, 0);
		map.addIntersection(startInter);
		map.addIntersection(new Intersection(1, 1, 0));
		map.addIntersection(new Intersection(2, 1, 1));
		map.addIntersection(new Intersection(3, 0, 1));
		
		map.addRoad(new Road(0, 1, 1, 1, "r0"));
		map.addRoad(new Road(1, 2, 1, 1, "r1"));
		map.addRoad(new Road(2, 3, 1, 1, "r2"));
		map.addRoad(new Road(3, 0, 1, 1, "r3"));
		
		DeliveryQuery delivery = new DeliveryQuery();
		
		Warehouse w = new Warehouse();
		w.setIntersection(startInter);
		Delivery [] deliveryArray = new Delivery [2];
		deliveryArray [0] = new Delivery();
		deliveryArray [0].setIntersection(new Intersection(1, 1, 0));
		deliveryArray [1] = new Delivery();
		deliveryArray [1].setIntersection(new Intersection(3,0,1));
		
		delivery.setWarehouse(w);
		delivery.setDelivery(deliveryArray);
		
		CompleteGraphComputer computer = new CompleteGraphComputer(map, delivery);
		Double [][] adjacencyMatrix = computer.getAdjacencyMatrix();
		
		assertEquals(adjacencyMatrix[0][0], new Double(0.0));
		
	}
}
