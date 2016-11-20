/**
 * 
 */
package com.hexagone.delivery.models;

/**
 * @author patrick
 *
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
