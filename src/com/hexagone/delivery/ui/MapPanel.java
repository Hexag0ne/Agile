package com.hexagone.delivery.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

import com.hexagone.delivery.control.MapPainter;

public class MapPanel extends JPanel {

	MapPainter painter;
	private static final float SCALE = 1.25f;
	
	/**
	 * Constructor for the MapPanel
	 * @param painter painter class which is going to ensure the painting of the map is done properly
	 */
	public MapPanel(MapPainter painter)
	{
		super();
		this.painter = painter;
		
		setLayout(new FlowLayout());
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return new Dimension(screenSize.width, screenSize.height+10);
	}
	
	/**
	 * Calls the painter to draw the map 
	 */
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		painter.draw(g,SCALE);
	}
	
}
