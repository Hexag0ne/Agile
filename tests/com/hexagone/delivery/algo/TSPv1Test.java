package com.hexagone.delivery.algo;

import static org.junit.Assert.*;

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
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
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
		Map map = new Map();
		map.addIntersection(new Intersection(0, 0, 0));
		map.addIntersection(new Intersection(1, 1, 0));
		map.addIntersection(new Intersection(2, 1, 1));
		map.addIntersection(new Intersection(3, 0, 1));
		map.addIntersection(new Intersection(4, 5, 0));
		map.addIntersection(new Intersection(5, 0, 19));
		map.addIntersection(new Intersection(6, 1, 14));
		map.addIntersection(new Intersection(7, 0, 1));
		map.addIntersection(new Intersection(8, 0, 16));
		map.addIntersection(new Intersection(9, 1, 7));
		map.addIntersection(new Intersection(10, 1, 14));
		map.addIntersection(new Intersection(11, 0, 5));
		map.addIntersection(new Intersection(12, 0,11));
		map.addIntersection(new Intersection(13, 1, 4));
		map.addIntersection(new Intersection(14, 0, 7));
		map.addIntersection(new Intersection(15, 0, 2));
		map.addIntersection(new Intersection(16, 0, 9));
		
		map.addRoad(new Road(0, 1, 1, 1, "r0")); //time cost is 0.36 for each road
		map.addRoad(new Road(1, 2, 2, 1, "r1"));
		map.addRoad(new Road(2, 3, 4, 1, "r2"));
		map.addRoad(new Road(3, 4, 5, 1, "r3"));
		map.addRoad(new Road(4, 5, 7, 1, "r4"));
		map.addRoad(new Road(5, 6, 7, 1, "r5"));
		map.addRoad(new Road(6, 7, 8, 1, "r6"));
		map.addRoad(new Road(7, 8, 9, 1, "r7"));
		map.addRoad(new Road(8, 9, 9, 1, "r8"));
		map.addRoad(new Road(9, 10, 9, 1, "r9"));
		map.addRoad(new Road(10, 11, 9, 1, "r10"));
		map.addRoad(new Road(11, 12, 2, 1, "r11"));
		map.addRoad(new Road(12, 13, 10, 1, "r12"));
		map.addRoad(new Road(13, 14, 6, 1, "r13"));
		map.addRoad(new Road(14, 15, 6, 1, "r14"));
		map.addRoad(new Road(15, 16, 8, 1, "r15"));
		map.addRoad(new Road(16, 0, 8, 1, "r16"));
		
		map.addRoad(new Road(9, 2, 1, 1, "r17")); //time cost is 0.36 for each road
		map.addRoad(new Road(9, 5, 2, 1, "r18"));
		map.addRoad(new Road(5, 9, 4, 1, "r19"));
		map.addRoad(new Road(4, 8, 5, 1, "r20"));
		map.addRoad(new Road(8, 4, 7, 1, "r21"));
		map.addRoad(new Road(3, 15, 7, 1, "r22"));
		map.addRoad(new Road(15, 6, 8, 1, "r23"));

		
		DeliveryQuery deliv = new DeliveryQuery();
		
		Warehouse w = new Warehouse();
		w.setIntersection(new Intersection(0,0,0));
		w.setDepartureTime(df.parse("11/29/15 8:05 AM, PDT"));
		Delivery [] deliveryArray = new Delivery [16];
		deliveryArray [0] = new Delivery();
		deliveryArray [0].setIntersection(new Intersection(1, 1, 0));
		deliveryArray [0].setStartSchedule(df.parse("11/29/15 8:15 AM, PDT"));
		deliveryArray [0].setDuration(5);
		deliveryArray [1] = new Delivery();
		deliveryArray [1].setIntersection(new Intersection(2,1,1));
		deliveryArray [1].setEndSchedule(df.parse("11/29/15 8:20 AM, PDT"));
		deliveryArray [1].setDuration(5);
		deliveryArray [2] = new Delivery();
		deliveryArray [2].setIntersection(new Intersection(3,0,1));
		deliveryArray [2].setEndSchedule(df.parse("11/29/15 8:25 AM, PDT"));
		deliveryArray [2].setDuration(5);
		deliveryArray [3] = new Delivery();
		deliveryArray [3].setIntersection(new Intersection(4,5,0));
		deliveryArray [3].setEndSchedule(df.parse("11/29/15 8:30 AM, PDT"));
		deliveryArray [3].setDuration(5);
		deliveryArray [4] = new Delivery();
		deliveryArray [4].setIntersection(new Intersection(5,0,19));
		deliveryArray [4].setEndSchedule(df.parse("11/29/15 8:40 AM, PDT"));
		deliveryArray [4].setDuration(5);
		deliveryArray [5] = new Delivery();
		deliveryArray [5].setIntersection(new Intersection(6,0,18));
		deliveryArray [5].setEndSchedule(df.parse("11/29/15 8:50 AM, PDT"));
		deliveryArray [5].setDuration(5);
		deliveryArray [6] = new Delivery();
		deliveryArray [6].setIntersection(new Intersection(7,1,14));
		deliveryArray [6].setEndSchedule(df.parse("11/29/15 9:10 AM, PDT"));
		deliveryArray [6].setDuration(5);
		deliveryArray [7] = new Delivery();
		deliveryArray [7].setIntersection(new Intersection(8,0,16));
		deliveryArray [7].setEndSchedule(df.parse("11/29/15 9:15 AM, PDT"));
		deliveryArray [7].setDuration(5);
		deliveryArray [8] = new Delivery();
		deliveryArray [8].setIntersection(new Intersection(9,1,7));
		deliveryArray [8].setEndSchedule(df.parse("11/29/15 9:20 AM, PDT"));
		deliveryArray [8].setDuration(5);
		deliveryArray [9] = new Delivery();
		deliveryArray [9].setIntersection(new Intersection(10, 1, 14));
		deliveryArray [9].setStartSchedule(df.parse("11/29/15 9:30 AM, PDT"));
		deliveryArray [9].setDuration(5);
		deliveryArray [10] = new Delivery();
		deliveryArray [10].setIntersection(new Intersection(11,0,5));
		deliveryArray [10].setEndSchedule(df.parse("11/29/15 9:40 AM, PDT"));
		deliveryArray [10].setDuration(5);
		deliveryArray [11] = new Delivery();
		deliveryArray [11].setIntersection(new Intersection(12,0,11));
		deliveryArray [11].setEndSchedule(df.parse("11/29/15 9:50 AM, PDT"));
		deliveryArray [11].setDuration(5);
		deliveryArray [12] = new Delivery();
		deliveryArray [12].setIntersection(new Intersection(13,1,4));
		deliveryArray [12].setEndSchedule(df.parse("11/29/15 10:10 AM, PDT"));
		deliveryArray [12].setDuration(5);
		deliveryArray [13] = new Delivery();
		deliveryArray [13].setIntersection(new Intersection(14,0,7));
		deliveryArray [13].setEndSchedule(df.parse("11/29/15 10:15 AM, PDT"));
		deliveryArray [13].setDuration(5);
		deliveryArray [14] = new Delivery();
		deliveryArray [14].setIntersection(new Intersection(15,0,2));
		deliveryArray [14].setEndSchedule(df.parse("11/29/15 10:20 AM, PDT"));
		deliveryArray [14].setDuration(5);
		deliveryArray [15] = new Delivery();
		deliveryArray [15].setIntersection(new Intersection(16,0,9));
		deliveryArray [15].setEndSchedule(df.parse("11/29/15 10:30 AM, PDT"));
		deliveryArray [15].setDuration(5);
		
		/**
		 * As the start schedule time is after the end schedule time, it should create a timeout. 
		 */
		
		deliv.setWarehouse(w);
		deliv.setDelivery(deliveryArray);
		
		DeliveryComputer deliveryComputer = new DeliveryComputer(map, deliv);
		deliveryComputer.getDeliveryPoints();
			
		//Expected checkTimeout() is true
		assertTrue(deliveryComputer.checkTimeout());
		
	}
	
	@Test
	public void testNotEmptySolution() throws ParseException {
		Map map = new Map();
		map.addIntersection(new Intersection(0, 0, 0));
		map.addIntersection(new Intersection(1, 1, 0));
		map.addIntersection(new Intersection(2, 1, 1));
		map.addIntersection(new Intersection(3, 0, 1));
		
		map.addRoad(new Road(0, 1, 1, 1, "r0")); //time cost is 0.36 for each road
		map.addRoad(new Road(1, 2, 1, 1, "r1"));
		map.addRoad(new Road(2, 3, 1, 1, "r2"));
		map.addRoad(new Road(3, 0, 1, 1, "r3"));
		
		DeliveryQuery deliv = new DeliveryQuery();
		
		Warehouse w = new Warehouse();
		w.setIntersection(new Intersection(0,0,0));
		w.setDepartureTime(df.parse("11/29/15 8:0 AM, PDT"));
		Delivery [] deliveryArray = new Delivery [2];
		deliveryArray [0] = new Delivery();
		deliveryArray [0].setIntersection(new Intersection(1, 1, 0));
		deliveryArray [1] = new Delivery();
		deliveryArray [1].setIntersection(new Intersection(3,0,1));
		
		deliv.setWarehouse(w);
		deliv.setDelivery(deliveryArray);

		DeliveryComputer deliveryComputer = new DeliveryComputer(map, deliv);
		deliveryComputer.getDeliveryPoints();
		
		// NotEmptySolution should return false, because a path has been calculated
		assertTrue(deliveryComputer.checkNotEmptySolution());
	}

}
