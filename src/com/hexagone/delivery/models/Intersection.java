/**
 * 
 */
package com.hexagone.delivery.models;

import java.awt.Point;

/**
 * This class represents an intersection of the map
 * 
 * @author patrick
 */
public class Intersection {
	private Point coordinates;
	private Integer id;

	/**
	 * Allows the user to get a copy of the intersection's identifier.
	 * 
	 * @return an Integer copy of the identifier.
	 */
	public Integer getId() {
		return new Integer(id);
	}

	public Intersection(int id) {
		this.id = id;
	}

	public Intersection(int id, int x, int y) {
		this(id);
		this.coordinates = new Point(x, y);
	}

	@Override
	public String toString() {
		return "Intersection [coordinates = " + coordinates + ", id = " + id + "]";
	}
}
