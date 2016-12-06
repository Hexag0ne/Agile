/**
 * 
 */
package com.hexagone.delivery.models;

import java.awt.Point;

/**
 * This class models an intersection of the map It carries several information :
 * <ul>
 * <li>the coordinates</li>
 * <li>the identifier</li>
 * </ul>
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
		this.setCoordinates(new Point(x, y));
	}

	@Override
	public String toString() {
		return "Intersection [coordinates = " + getCoordinates() + ", id = " + id + "]";
	}

	public Point getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}
}
