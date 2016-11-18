/**
 * 
 */
package com.hexagone.delivery.models;

/**
 * @author patrick
 *
 */
public class Road {

	private int lenght;
	private int speed;
	private String roadName;
	private Intersection origin;
	private Intersection destination;
	
	public Road(Intersection origin, Intersection destination, int lenght, int speed){
		this.origin = origin;
		this.destination = destination;
		this.lenght = lenght;
		this.speed = speed;
		
	}
}
