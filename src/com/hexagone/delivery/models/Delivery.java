package com.hexagone.delivery.models;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.hexagone.delivery.xml.DateAdapter;
import com.hexagone.delivery.xml.IntersectionAdapter;
import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader;

/**
 * This class models a delivery in a delivery query
 */
@XmlRootElement(name = "livraison")
public class Delivery {
	
	public Delivery(Intersection intersection) {
		this.intersection = intersection;
	}
	
	public Delivery() {
		
	}

	private Intersection intersection;

	private Date endSchedule;

	private int duration;

	private Date startSchedule;
	
	private Date departureTime;
	
	private Date arrivalTime;
	
	private int waitingTime = 0;
	
	public void setTimes(Date departure, Date arrival, int waiting) {
		this.setDepartureTime(departure);
		this.setArrivalTime(arrival);
		this.setWaitingTime(waiting);
	}

	public Intersection getIntersection() {
		return intersection;
	}

	@XmlJavaTypeAdapter(IntersectionAdapter.class)
	@XmlAttribute(name = "adresse")
	public void setIntersection(Intersection intersection) {
		this.intersection = intersection;
	}

	public Date getEndSchedule() {
		return endSchedule;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	@XmlAttribute(name = "finPlage")
	public void setEndSchedule(Date endSchedule) {
		this.endSchedule = endSchedule;
	}

	public int getDuration() {
		return duration;
	}

	@XmlAttribute(name = "duree")
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Date getStartSchedule() {
		return startSchedule;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	@XmlAttribute(name = "debutPlage")
	public void setStartSchedule(Date startSchedule) {
		this.startSchedule = startSchedule;
	}

	@Override
	public String toString() {
		return "Delivery [intersection = " + intersection + ", endSchedule = " + endSchedule + ", duration = "
				+ duration + ", startSchedule = " + startSchedule + "]";
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
}