package com.hexagone.delivery.algo;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CompletGraphComputerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSmallestCost() throws Exception {
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>(2);
		map.put(new Integer(0), new Integer(Integer.MAX_VALUE));
		map.put(new Integer(45), new Integer(5));
		
		Integer test = CompleteGraphComputer.smallestCost(map);
		
		assertEquals(new Integer(45),test);
	}

}
