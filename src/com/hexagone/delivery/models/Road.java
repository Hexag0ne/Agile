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
 * Note that if a real road allows cars to travel both ways, it will me modelized as <strong>two different roads
 * </strong> in the map.
 */
public class Road {

	/** Length of the road between the two intersections in <strong>decimeters</strong> */
	private int length;
	/** Average speed at which cars travel down this road in <strong>Km/h</strong> */
	private int speed;
	/** Name of the Road */
	private String roadName;
	/** Unique identifier of the node from which the road spans */
	private Integer origin;
	/** Unique identifier of the towards which the road goes */
	private Integer destination;
	
	/**
	 * Allows the user to get a copy of the origin intersection of the road.
	 * @return a copy of the origin intersection identifier
	 */
	public Integer getOrigin()
	{
		return new Integer(origin);
	}
	
	public Road(Integer origin, Integer destination, int length, int speed, String name){
		this.origin = origin;
		this.destination = destination;
		this.length = length;
		this.speed = speed;
		this.roadName = name;
	}
}
