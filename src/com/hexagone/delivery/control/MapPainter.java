package com.hexagone.delivery.control;

import java.awt.Graphics;

/**
 * This class is an interface that allows the user interface to call the controller to draw the map.
 */
public interface MapPainter {

	/**
	 * Requests the class that implements this interface to draw the map whith the G graphics
	 * @param g the graphics object associated to the place where the Map is to be drawn
	 * @param scale a scaling factor to display the map
	 */
	public void draw(Graphics g, float scale);
}
