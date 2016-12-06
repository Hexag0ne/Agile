package com.hexagone.delivery.models;

import java.util.ArrayList;

/**
 * This class is part of the map modeling. An Arrival Point is an particular
 * point. It is either a warehouse or a delivery point. It carries several
 * information :
 * <ul>
 * <li>the delivery to which it is linked</li>
 * <li>the road to get to the arrival point</li>
 * </ul>
 */
public class ArrivalPoint {
	/** the roads to get to the arrival point */
	private ArrayList<Road> roads;

	/** delivery to which the arrival point is linked */
	private Delivery delivery;

	/**
	 * Constructor
	 * 
	 * @param r
	 *            the roads to get to the arrival point as an ArrayList of Roads
	 * @param d
	 *            the delivery to which the arrival point is linked as a
	 *            Delivery Object
	 */
	public ArrivalPoint(ArrayList<Road> r, Delivery d) {
		this.setRoads(r);
		this.setDelivery(d);
	}

	public ArrayList<Road> getRoads() {
		return roads;
	}

	public void setRoads(ArrayList<Road> roads) {
		this.roads = roads;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	@Override
	public String toString() {
		return "ArrivalPoint [roads=" + roads + ", delivery=" + delivery + "]";
	}
}
