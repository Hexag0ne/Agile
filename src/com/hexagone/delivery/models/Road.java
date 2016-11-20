/**
 * 
 */
package com.hexagone.delivery.models;

/**
 * This class is part of the map modelling. 
 * A Road is a link between two Intersections
 * It carries several information :
 * <ul>
 * <li>The two intersections it is linking</li>
 * <li>it's name</li>
 * <li>it's length</li>
 * <li>the average speed motorists travel on this road</li>
 * </ul> 
 */
public class Road {

	private int length;
	private int speed;
	private String roadName;
	private Intersection origin;
	private Intersection destination;
	
	public Road(Intersection origin, Intersection destination, int length, int speed){
		this.origin = origin;
		this.destination = destination;
		this.length = length;
		this.speed = speed;
	}
}
