package com.hexagone.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;

public class MapFrame extends JPanel{
    
	private Map map;
	
	MapFrame(Map map){
		super();
		this.map=map;
		FlowLayout fl= new FlowLayout();
		setLayout(fl);
		setBackground(Color.WHITE);
	}

	public void paint(Graphics g)
	{   
		ArrayList<Intersection> intersections = new ArrayList<Intersection>(); 
		intersections = map.getIntersections();
		Set<Integer> roads = new HashSet<Integer>();
		roads = (map.getRoads()).keySet();
		
		//Cet entier permet de rétrécir le graphe
		int coefficient=2;
		for(Intersection i:intersections){
			Point p = new Point();
			p = i.getCoordinates();
			g.setColor(Color.BLUE);
			g.fillOval(((p.x))/coefficient,((p.y))/coefficient,10,10);
		}
		for(int j: roads){
			ArrayList<Road> roadsFromI = new ArrayList<Road>();
			roadsFromI = map.getRoads().get(j);
			for(Road r: roadsFromI){
				g.setColor(Color.BLACK);
				Graphics2D g2 = (Graphics2D) g;
				Point destination = null;
				Point origine = null;
				for(Intersection in:intersections){
					if((in.getId()).equals(r.getOrigin())){
						origine = in.getCoordinates();
						break;
					}
				}
				for(Intersection in:intersections){
					if((in.getId()).equals(r.getDestination())){
						destination = in.getCoordinates();
						break;
					}
				}
				Line2D lin = new Line2D.Float(((origine.x)/coefficient)+5, ((origine.y)/coefficient)+5, ((destination.x)/coefficient)+5, ((destination.y)/coefficient)+5);
		        g2.draw(lin);
			}
		}
		
	}
}
