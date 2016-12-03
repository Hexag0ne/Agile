/**
 * Package containing all the classes modelling our problem.
 */
package com.hexagone.delivery.models;


import com.hexagone.delivery.xml.DateAdapter;
import com.hexagone.delivery.xml.IntersectionAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This class models a delivery in a delivery query
 */
@XmlRootElement(name = "livraison")
public class Delivery {
	/** The intersection where the delivery takes place */
	private Intersection intersection;

	/** The time point after which it is no longer possible to deliver */
	private Date endSchedule;

	/** The time in minutes that the delivery takes */
	private int duration;

	/** The time from which the delivery may take place */
	private Date startSchedule;

	/**
	 * Gives back the intersection object at which the delivery takes place.
	 * @return the intersection object where this delivery takes place
	 */
	public Intersection getIntersection() {
		return intersection;
	}

	/**
	 * Allows to set the intersection at which the delivery is going to take place
	 * @param intersection the intersection at which this delivery is going to take place
	 */
	@XmlJavaTypeAdapter(IntersectionAdapter.class)
	@XmlAttribute(name = "adresse")
	public void setIntersection(Intersection intersection) {
		this.intersection = intersection;
	}

	/**
	 * Gives bask the end of the delivery possibility if any.
	 * In case there is no time limit to make the delivery, this method will return null
	 * @return the Date at which the delivery point closes
	 */
	public Date getEndSchedule() {
		return endSchedule;
	}

	/**
	 * Allows the user to define an end for the delivery
	 * @param endSchedule the date at which it will not be possible to deliver annymore
	 */
	@XmlJavaTypeAdapter(DateAdapter.class)
	@XmlAttribute(name = "finPlage")
	public void setEndSchedule(Date endSchedule) {
		this.endSchedule = endSchedule;
	}

	/**
	 * Allows the user to get the time the delivery is going to take.
	 * @return the number of minutes necessary to make the delivery
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Allows the user to define the time needed to make the delivery, that is how long one needs to stay at the 
	 * delivery point
	 * @param duration the time needed to make the delivery in minutes
	 */
	@XmlAttribute(name = "duree")
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Allows the user to get the Date at which it becomes possible to make the delivery
	 * @return the date at which the delivery point opens
	 */
	public Date getStartSchedule() {
		return startSchedule;
	}

	/**
	 * Allows the user to define a time at which the delivery point is going to open.
	 * @param startSchedule the moment at which the user is going to be able to begin ist delivery 
	 */
	@XmlJavaTypeAdapter(DateAdapter.class)
	@XmlAttribute(name = "debutPlage")
	public void setStartSchedule(Date startSchedule) {
		this.startSchedule = startSchedule;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Delivery [intersection = " + intersection + ", endSchedule = " + endSchedule + ", duration = "
				+ duration + ", startSchedule = " + startSchedule + "]";
	}
}