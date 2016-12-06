package com.hexagone.delivery.control;

import java.awt.Graphics;

/**
 * This class is an interface that allows us to draw the map
 */
public interface MapPainter {

	public void draw(Graphics g, float scale);
}
