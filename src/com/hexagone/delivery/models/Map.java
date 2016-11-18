package com.hexagone.delivery.models;

import java.util.ArrayList;

public class Map {
	private  ArrayList<Intersection> intersections;
	private ArrayList<Road> roads; 
	
	public Map(){}
	
	public void addIntersection(Intersection intersection){
		intersections.add(intersection);
	}
	
	public void addRoad(Road road){
		roads.add(road);
	}
}
