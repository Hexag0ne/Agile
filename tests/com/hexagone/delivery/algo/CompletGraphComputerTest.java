package com.hexagone.delivery.algo;

import static org.junit.Assert.*;

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
	public void testIndexOf() throws Exception {
		Integer [] array = new Integer [3];
		array [0] = new Integer(0);
		array [1] = new Integer(1);
		array [2] = new Integer(2);
		
		int index0 = CompleteGraphComputer.indexOf(array, new Integer(0));
		int index1 = CompleteGraphComputer.indexOf(array, new Integer(1));
		int index2 = CompleteGraphComputer.indexOf(array, new Integer(2));
		
		assertEquals(0,index0);
		assertEquals(1,index1);
		assertEquals(2,index2);
	}

}
