/**
 * 
 */
package com.hexagone.delivery.models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author patrick
 *
 */
public class RoadTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTime() {
		Road r = new Road(new Integer(0), new Integer(1), 1.0, 1.0, "v0");
		
		assertEquals(new Double(0.36), r.getTime());
	}

}
