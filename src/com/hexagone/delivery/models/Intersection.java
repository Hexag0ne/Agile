/**
 * 
 */
package com.hexagone.delivery.models;

import java.awt.Point;

/**This class represents an intersection of the map
 * @author patrick
 *
 */
public class Intersection {
	private Point coordinates;
	private int id;
	
	public Intersection(int id, int x, int y){
		this.id = id;
		coordinates = new Point(x,y);
	}

}
