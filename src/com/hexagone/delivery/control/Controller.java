package com.hexagone.delivery.control;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import com.hexagone.delivery.models.ArrivalPoint;
import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Route;
import com.hexagone.delivery.ui.MainFrame;

public class Controller implements UserActions, MapPainter {

	/** Element of model Map loaded by the user */
	private Map map;
	/** Deliveries chosen by the user */
	private DeliveryQuery deliveryQuery;
	/** Problem solution */
	private Route route;
	
	/** Elements of the interface */
	private MainFrame mainFrame;
	
	/** Current state of the application */
	private ControllerActions currentState;
	
	/** An instance of each of the State the application can be in */
	private ControllerActions LOADMAP_STATE;
	private ControllerActions LOADDELIVERY_STATE;
	private ControllerActions COMPUTE_STATE;
	private NavigateState NAVIGATE_STATE;
	
	public Controller() {
		LOADMAP_STATE = new LoadMapState();
		LOADDELIVERY_STATE = new LoadDeliveryState();
		COMPUTE_STATE = new ComputeState();
		
		mainFrame = new MainFrame(this, this);
		NAVIGATE_STATE = new NavigateState(mainFrame);
		currentState = nextState();
		mainFrame.setSidePanelsVisible(false);
		
		mainFrame.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.hexagone.delivery.control.UserActions#loadMapButtonClick()
	 */
	@Override
	public void loadMapButtonClick() {
		Map newMap = currentState.loadMap();
		if (newMap != null){
			deliveryQuery = null; //Change of map -> we discard the deliveryQuery
			route = null;
			map = newMap;
		}
		this.currentState = nextState();
	}

	/*
	 * (non-Javadoc)
	 * @see com.hexagone.delivery.control.UserActions#loadDeliveryQueryButtonClick()
	 */
	@Override
	public void loadDeliveryQueryButtonClick() {
		DeliveryQuery deliv = currentState.loadDeliveryQuery();
		if (deliv != null) {
			deliveryQuery = deliv;
			route = null;
		}
		this.currentState = nextState();
	}

	/*
	 * (non-Javadoc)
	 * @see com.hexagone.delivery.control.UserActions#computeRouteButtonClick()
	 */
	@Override
	public void computeRouteButtonClick() {
		this.route = currentState.computeDelivery(map, deliveryQuery);
		this.currentState = nextState();
	}

	/*
	 * (non-Javadoc)
	 * @see com.hexagone.delivery.control.UserActions#generatePlanningButtonClick()
	 */
	@Override
	public void generatePlanningButtonClick() {
		currentState.generatePlanning(route);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.hexagone.delivery.control.UserActions#nextDelivery()
	 */
	@Override
	public void nextDelivery() {
		NAVIGATE_STATE.nextDelivery(route.getRoute().size());
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.hexagone.delivery.control.UserActions#previousDelivery()
	 */
	@Override
	public void previousDelivery() {
		NAVIGATE_STATE.previousDelivery();
		
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
			mainFrame.setSidePanelsVisible(false);
		}
		if (deliveryQuery != null && map != null)
		{
			nextState = COMPUTE_STATE;
			mainFrame.setSidePanelsVisible(false);
		}
		if (deliveryQuery != null && map != null && route != null) {
			nextState = NAVIGATE_STATE;
			NAVIGATE_STATE.startTour();
			
			Vector<Delivery> data = new Vector<Delivery>();
			LinkedHashMap<Integer,ArrivalPoint> lhp = route.getRoute();
			for (Integer it : lhp.keySet()) {
				data.add(lhp.get(it).getDelivery());
			}
			
			mainFrame.setTableData(data);
			mainFrame.setSidePanelsVisible(true);
		}
		
		mainFrame.repaint();
		mainFrame.setFocusableOnCenterPanel();
		return nextState;
	}

	@Override
	public void draw(Graphics g, float scale) {
		currentState.DrawMap(g, scale, map, deliveryQuery, route);
	}


	
}
