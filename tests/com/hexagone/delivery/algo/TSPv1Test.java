package com.hexagone.delivery.algo;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Warehouse;

public class TSPv1Test {

	SimpleDateFormat df;
	
	@Before
	public void setUp() throws Exception {
		df = new SimpleDateFormat();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testComputationNoTimeFrame() throws ParseException {
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
		
		DeliveryQuery deliv = new DeliveryQuery();
		
		Warehouse w = new Warehouse();
		w.setIntersection(new Intersection(42,0,0));
		w.setDepartureTime(df.parse("11/29/15 8:0 AM, PDT"));
		Delivery [] deliveryArray = new Delivery [2];
		deliveryArray [0] = new Delivery();
		deliveryArray [0].setIntersection(new Intersection(1, 1, 0));
		deliveryArray [1] = new Delivery();
		deliveryArray [1].setIntersection(new Intersection(3,0,1));
		
		deliv.setWarehouse(w);
		deliv.setDelivery(deliveryArray);
		
		
		TSPSolverV1 solver = new TSPSolverV1(costs, deliv);
		
		solver.computeSolution();
		
		ArrayList<Integer> bestPath = solver.getBestSolution();
		
		//Expected path is 0 -> 1 -> 2
		assertEquals(new Integer(0), bestPath.get(0));
		assertEquals(new Integer(1), bestPath.get(1));
		assertEquals(new Integer(2), bestPath.get(2));
	}
	
	@Test
	public void testComputationWithTimeFrame() throws ParseException
	{
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
		
		DeliveryQuery deliv = new DeliveryQuery();
		
		Warehouse w = new Warehouse();
		w.setIntersection(new Intersection(42,0,0));
		w.setDepartureTime(df.parse("11/29/15 8:0 AM, PDT"));
		Delivery [] deliveryArray = new Delivery [2];
		deliveryArray [0] = new Delivery();
		deliveryArray [0].setIntersection(new Intersection(1, 1, 0));
		deliveryArray [0].setStartSchedule(df.parse("11/29/15 8:30 AM, PDT"));
		deliveryArray [0].setDuration(5);
		deliveryArray [1] = new Delivery();
		deliveryArray [1].setIntersection(new Intersection(3,0,1));
		deliveryArray [1].setEndSchedule(df.parse("11/29/15 8:30 AM, PDT"));
		deliveryArray [1].setDuration(5);
		
		
		// Checking how the Calendar works
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(deliveryArray[0].getStartSchedule());
		
		assertEquals(8, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(30, calendar.get(Calendar.MINUTE));
		calendar.add(Calendar.MINUTE, 40);
		assertEquals(9, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(10, calendar.get(Calendar.MINUTE));
		
		calendar.setTime(deliveryArray[0].getStartSchedule());
		assertEquals(8, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(30, calendar.get(Calendar.MINUTE));
		// End of calendar Checking
		
		/**
		 * With this combination of time constraints, the only passage possible is to go to the second delivery point
		 * first and then to the first one
		 */
		
		deliv.setWarehouse(w);
		deliv.setDelivery(deliveryArray);
		
		
		TSPSolverV1 solver = new TSPSolverV1(costs, deliv);
		
		solver.computeSolution();
		
		ArrayList<Integer> bestPath = solver.getBestSolution();
		
		//Expected path is 0 -> 2 -> 1
		assertEquals(new Integer(0), bestPath.get(0));
		assertEquals(new Integer(2), bestPath.get(1));
		assertEquals(new Integer(1), bestPath.get(2));
		
	}
	
	@Test
	public void testCheckTimeout() throws ParseException
	{
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
		
		DeliveryQuery deliv = new DeliveryQuery();
		
		Warehouse w = new Warehouse();
		w.setIntersection(new Intersection(42,0,0));
		w.setDepartureTime(df.parse("11/29/15 8:00 AM, PDT"));
		Delivery [] deliveryArray = new Delivery [2];
		deliveryArray [0] = new Delivery();
		deliveryArray [0].setIntersection(new Intersection(1, 1, 0));
		deliveryArray [0].setStartSchedule(df.parse("11/29/15 8:05 AM, PDT"));
		deliveryArray [0].setDuration(5);
		deliveryArray [1] = new Delivery();
		deliveryArray [1].setIntersection(new Intersection(3,0,1));
		deliveryArray [1].setEndSchedule(df.parse("11/29/15 8:00 AM, PDT"));
		deliveryArray [1].setDuration(5);
		
		
		/**
		 * As the start schedule time is after the end schedule time, it should create a timeout. 
		 */
		
		deliv.setWarehouse(w);
		deliv.setDelivery(deliveryArray);
		
		
		TSPSolverV1 solver = new TSPSolverV1(costs, deliv);
		solver.computeSolution();
			
		//Expected getTimeout() is true
		assertEquals(getTimeout(), true);
		
	}
	
	@Test
	public void testNotEmptySolution() throws ParseException {
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
		
		DeliveryQuery deliv = new DeliveryQuery();
		
		Warehouse w = new Warehouse();
		w.setIntersection(new Intersection(42,0,0));
		w.setDepartureTime(df.parse("11/29/15 8:0 AM, PDT"));
		Delivery [] deliveryArray = new Delivery [2];
		deliveryArray [0] = new Delivery();
		deliveryArray [0].setIntersection(new Intersection(1, 1, 0));
		deliveryArray [1] = new Delivery();
		deliveryArray [1].setIntersection(new Intersection(3,0,1));
		
		deliv.setWarehouse(w);
		deliv.setDelivery(deliveryArray);
		
		
		TSPSolverV1 solver = new TSPSolverV1(costs, deliv);
		
		solver.computeSolution();
		
		ArrayList<Integer> bestPath = solver.getBestSolution();
		
		// NotEmptySolution should return false, because a path has been calculated
		assertEquals(NotEmptySolution(), false);
	}

}
