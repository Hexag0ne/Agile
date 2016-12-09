package com.hexagone.delivery.models;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.hexagone.delivery.xml.DateAdapter;
import com.hexagone.delivery.xml.IntersectionAdapter;

/**
 * This class models a warehouse It carries several information :
 * <ul>
 * <li>its intersection</li>
 * <li>its departure time</li>
 * </ul>
 */

@XmlRootElement(name = "entrepot")
public class Warehouse {
	private Intersection intersection;

	private Date departureTime;

	public Intersection getIntersection() {
		return intersection;
	}

	@XmlJavaTypeAdapter(IntersectionAdapter.class)
	@XmlAttribute(name = "adresse")
	public void setIntersection(Intersection intersection) {
		this.intersection = intersection;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	@XmlAttribute(name = "heureDepart")
	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	@Override
	public String toString() {
		return "WareHouse [intersection = " + intersection + ", departureTime = " + departureTime + "]";
	}
}