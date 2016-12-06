/**
 * Package containing all the classes modelling our problem.
 */
package com.hexagone.delivery.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.hexagone.delivery.xml.DateAdapter;
import com.hexagone.delivery.xml.IntersectionAdapter;

/**
 * This class models a delivery in a delivery query
 * It carries several information :
 * <ul>
 * <li>the intersection where the delivery takes place</li>
 * <li>the time point after which it is no longer possible to deliver</li>
 * <li>the time in minutes that the delivery takes</li>
 * <li>the time from which the delivery may take place</li>
 * <li>the departure time</li>
 * <li>the arrival time</li>
 * <li>the waiting time</li>
 * </ul>
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
	
	private Date departureTime;
	
	private Date arrivalTime;
	
	private int waitingTime = 0;
	
	public void setTimes(Date departure, Date arrival, int waiting) {
		this.setDepartureTime(departure);
		this.setArrivalTime(arrival);
		this.setWaitingTime(waiting);
	}

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

	@Override
	public String toString() {
		return "Delivery [intersection=" + intersection + ", endSchedule=" + endSchedule + ", duration=" + duration
				+ ", startSchedule=" + startSchedule + ", departureTime=" + departureTime + ", arrivalTime="
				+ arrivalTime + ", waitingTime=" + waitingTime + "]";
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
	
	/**
	 * This methods gives the time slot of the delivery
	 * @return the time slot as a String 
	 */
	public String getTimeslotDelivery() {
		if(this.getStartSchedule() != null){
			Calendar startScheduleCalendar = GregorianCalendar.getInstance();
			startScheduleCalendar.setTime(this.getStartSchedule());
			Calendar endScheduleCalendar = GregorianCalendar.getInstance();
			endScheduleCalendar.setTime(this.getEndSchedule());
			String timeslotDelivery = (""+startScheduleCalendar.get(Calendar.HOUR_OF_DAY)+"h"+"-"+endScheduleCalendar.get(Calendar.HOUR_OF_DAY)+"h");
			return timeslotDelivery;
		}
		else return null;
	}
	
	/**
	 * This methods gives the departure time of the delivery
	 * @return the departure time as a String 
	 */
	public String getDepartureTimeString(){
		if(this.getDepartureTime() != null){
			SimpleDateFormat small = new SimpleDateFormat("HH:mm", Locale.FRENCH);
			return (""+small.format(this.getDepartureTime()));
		}
		else return null;
	}
	
	/**
	 * This methods gives the arrival time of the delivery
	 * @return the arrival time as a String 
	 */
	public String getArrivalTimeString(){
		if(this.getDepartureTime() != null){
			SimpleDateFormat small = new SimpleDateFormat("HH:mm", Locale.FRENCH);
			return (""+small.format(this.getArrivalTime()));
			
		}
		else return null;
		
	}
	
	/**
	 * Constructs a Delivery from an intersection
	 * @param intersection 
	 */
	public Delivery(Intersection intersection) {
		this.intersection = intersection;
	}
	
	/**
	 * Default Constructor
	 */
	public Delivery() {	
	}
}