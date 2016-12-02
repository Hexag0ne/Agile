package com.hexagone.delivery.models;

import java.util.ArrayList;

public class ArrivalPoint {
	private ArrayList<Road> roads;
	
	private Delivery delivery;
	
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
}
