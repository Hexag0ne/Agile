package com.hexagone.delivery.models;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class models a delivery query
 */
@XmlRootElement(name = "demandeDeLivraisons")
public class DeliveryQuery {
	private Warehouse warehouse;

	private Delivery[] deliveries;

	/**
	 * Gives the number of different passage points in the delivery query.
	 * 
	 * @return The number of passage points, i.e. one (for the warehouse) plus
	 *         the number of deliveries to make.
	 */
	public int getPassagePointsNumber() {
		return 1 + deliveries.length;
	}

	/**
	 * Gives back the different identifiers of the passage points of a delivery
	 * query in an Integer array. The first element of this array (i.e. index 0)
	 * is the warehouse identifier
	 * 
	 * @return the different node identifiers of the deliveryQuery
	 */
	public Integer[] getDeliveryPassageIdentifiers() {
		Integer[] tab = new Integer[getPassagePointsNumber()];
		tab[0] = warehouse.getIntersection().getId();
		for (int i = 1; i < tab.length; i++) {
			tab[i] = deliveries[i - 1].getIntersection().getId();
		}

		return tab;
	}
	
	public Delivery[] getDeliveries() {
		return deliveries;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	@XmlElement(name = "entrepot")
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@XmlElement(name = "livraison")
	public void setDelivery(Delivery[] deliveries) {
		this.deliveries = deliveries;
	}

	@Override
	public String toString() {
		return "DeliveryQuery [warehouse=" + warehouse + ", delivery=" + Arrays.toString(deliveries) + "]";
	}
}