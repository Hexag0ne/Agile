package com.hexagone.delivery.control;

import java.awt.Graphics;
import java.util.LinkedHashMap;
import java.util.Vector;

import com.hexagone.delivery.models.ArrivalPoint;
import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.RouteHelper;
import com.hexagone.delivery.ui.MainFrame;

/**
 * This class provides control methods for the drawing of the map
 * 
 */
public class Controller implements UserActions, MapPainter {

	/** Element of model Map loaded by the user */
	private Map map;
	/** Deliveries chosen by the user */
	private DeliveryQuery deliveryQuery;
	/** Problem solution */
	private RouteHelper routeHelper;

	/** Elements of the interface */
	private MainFrame mainFrame;

	/** Current state of the application */
	private ControllerActions currentState;

	/** An instance of each of the State the application can be in */
	private ControllerActions LOADMAP_STATE;
	private ControllerActions LOADDELIVERY_STATE;
	private ControllerActions COMPUTE_STATE;
	private NavigateState NAVIGATE_STATE;

	/** Constructor */
	public Controller() {
		LOADMAP_STATE = new LoadMapState();
		LOADDELIVERY_STATE = new LoadDeliveryState();
		COMPUTE_STATE = new ComputeState();
	}
	
	public void launch() {
		mainFrame = new MainFrame(this, this);
		NAVIGATE_STATE = new NavigateState(mainFrame);
		currentState = nextState();
		mainFrame.setSidePanelsVisible(false);
		mainFrame.setVisible(true);	
	}

	/**
	 * Loads the map on button click
	 * 
	 * @see com.hexagone.delivery.control.UserActions#loadMapButtonClick()
	 */
	@Override
	public void loadMapButtonClick() {
		Map newMap = currentState.loadMap();
		if (newMap != null) {
			deliveryQuery = null; // Change of map -> we discard the deliveryQuery
			routeHelper = null;
			map = newMap;
			mainFrame.resetTable();
		}
		this.currentState = nextState();
	}

	/**
	 * Loads a delivery query on button click
	 * 
	 * @see com.hexagone.delivery.control.UserActions#loadDeliveryQueryButtonClick()
	 */
	@Override
	public void loadDeliveryQueryButtonClick() {
		DeliveryQuery deliv = currentState.loadDeliveryQuery();
		if (deliv != null) {
			deliveryQuery = deliv;
			routeHelper = null;
			mainFrame.resetTable();
		}
		this.currentState = nextState();
	}

	/**
	 * Computes a route on button click
	 * 
	 * @see com.hexagone.delivery.control.UserActions#computeRouteButtonClick()
	 */
	@Override
	public void computeRouteButtonClick() {
		mainFrame.resetTable();
		this.routeHelper = currentState.computeDelivery(map, deliveryQuery);
		this.currentState = nextState();
	}

	/**
	 * Generates a planning on button click
	 * 
	 * @see com.hexagone.delivery.control.UserActions#generatePlanningButtonClick()
	 */
	@Override
	public void generatePlanningButtonClick() {
		// TODO Auto-generated method stub

	}

	/**
	 * Moves to the next delivery
	 * 
	 * @see com.hexagone.delivery.control.UserActions#nextDelivery()
	 */
	@Override
	public void nextDelivery() {
		NAVIGATE_STATE.nextDelivery(routeHelper.getRoute().size());

	}

	/**
	 * Moves to the previous delivery
	 * 
	 * @see com.hexagone.delivery.control.UserActions#previousDelivery()
	 */
	@Override
	public void previousDelivery() {
		NAVIGATE_STATE.previousDelivery();

	}

	/**
	 * This method updates the state of the application depending on the state
	 * of the model variables As the state changes, the display may vary a lot.
	 * This method also calls for a repaint of the mainFrame.
	 */
	private ControllerActions nextState() {
		ControllerActions nextState = LOADMAP_STATE;

		if (map != null) {
			nextState = LOADDELIVERY_STATE;
			mainFrame.setSidePanelsVisible(false);
		}
		if (deliveryQuery != null && map != null) {
			nextState = COMPUTE_STATE;
			mainFrame.setSidePanelsVisible(false);
		}
		if (deliveryQuery != null && map != null && routeHelper != null) {
			nextState = NAVIGATE_STATE;
			NAVIGATE_STATE.startTour();

			Vector<Delivery> data = new Vector<Delivery>();
			LinkedHashMap<Integer, ArrivalPoint> lhp = routeHelper.getRoute();
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

	/**
	 * Draws the map from current state
	 */
	@Override
	public void draw(Graphics g, float scale) {
		currentState.DrawMap(g, scale, map, deliveryQuery, routeHelper);
	}

}
