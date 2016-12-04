package com.hexagone.delivery.control;

import java.awt.Graphics;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Route;
import com.hexagone.delivery.ui.MainFrame;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;

public class NavigateState implements ControllerActions {

	private MainFrame frame;

	private int step;

	@Override
	public Map loadMap() {
		try {
			return XMLDeserialiser.loadMap();
		} catch (XMLException e) {
			//TODO Exception popup for the user ?
			return null;
		}
	}

	@Override
	public DeliveryQuery loadDeliveryQuery() {
		try {
			return XMLDeserialiser.loadDeliveryQuery();
		} catch (XMLException e) {
			//TODO Exception popup for the user ?
			return null;
		}
	}

	@Override
	public Route computeDelivery(Map map, DeliveryQuery delivery) {
		DeliveryComputer computer = new DeliveryComputer(map, delivery);
		computer.getDeliveryPoints();
		
		return new Route(map, delivery, computer);
	}

	@Override
	public void DrawMap(Graphics g, float scale, Map m, DeliveryQuery delivery, Route route) {
		// TODO Auto-generated method stub

	}
	
	public void startTour(){
		step = 0;
		frame.repaint();
	}
	
	public void nextDelivery(int maxValue){
		step ++;
		if(step > maxValue){
			step = maxValue;
		}
			
		frame.repaint();
	}
	
	public void previousDelivery(){
		if(step > 0){
			step --;
		}
		
		frame.repaint();
	}
	
	
	public NavigateState(MainFrame frame){
		this.frame = frame;
		step = 0;
	}

}
