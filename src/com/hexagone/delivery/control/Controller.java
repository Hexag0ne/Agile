package com.hexagone.delivery.control;

import java.awt.Graphics;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.ui.MainFrame;

public class Controller implements UserActions, MapPainter {

	/** Element of model Map loaded by the user */
	private Map map;
	/** Deliveries chosen by the user */
	private DeliveryQuery deliveryQuery;
	/** Problem solver */
	private DeliveryComputer computer;
	
	/** Elements of the interface */
	private MainFrame mainFrame;
	
	/** Current state of the application */
	private ControllerActions currentState;
	
	/** An instance of each of the State the application can be in */
	private static final ControllerActions LOADMAP_STATE = new LoadMapState();
	private static final ControllerActions LOADDELIVERY_STATE = new LoadDeliveryState();
	private static final ControllerActions COMPUTE_STATE = new ComputeState();
	
	public Controller(){
		mainFrame = new MainFrame(this, this);
		currentState = nextState();
		
		mainFrame.setVisible(true);
	}

	/**
	 * Entry point for the application
	 * @param args
	 */
	public static void main(String[] args) {
		new Controller();
	}

	@Override
	public void loadMapButtonClick() {
		this.map = currentState.loadMap();
		this.currentState = nextState();
	}

	@Override
	public void loadDeliveryQueryButtonClick() {
		this.deliveryQuery = currentState.loadDeliveryQuery();
		this.currentState = nextState();
	}

	@Override
	public void computeRouteButtonClick() {
		this.computer = currentState.computeDelivery(map, deliveryQuery);
		this.currentState = nextState();
	}

	@Override
	public void generatePlanningButtonClick() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * This method updates the state of the application depending on the state of the model variables
	 * As the state changes, the display may vary a lot. This method also calls for a repaint of the mainFrame.
	 */
	private ControllerActions nextState() {
		ControllerActions nextState = LOADMAP_STATE;
		
		if (map != null)
		{
			nextState = LOADDELIVERY_STATE;
		}
		if (deliveryQuery != null)
		{
			nextState = COMPUTE_STATE;
		}
		
		mainFrame.repaint();
		
		return nextState;
	}

	@Override
	public void draw(Graphics g, float scale) {
		currentState.DrawMap(g, scale, map, deliveryQuery);
	}
}
