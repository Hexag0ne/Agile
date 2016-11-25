package com.hexagone.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Warehouse;

public class DetailsPanel extends JPanel {

	private static Map map;
	private static DeliveryQuery deliveryQuery;
	private static Point p;

	public DetailsPanel(Map map, DeliveryQuery deliveryQuery, Point p, int coefficient) {
		// TODO Auto-generated constructor stub
		super();
		this.map = map;
		this.deliveryQuery = deliveryQuery;
		this.p = p;
		GridLayout fl = new GridLayout(5, 1);
		setLayout(fl);
		setBackground(Color.WHITE);

		int adresse = 999999999, duration = 0;
		Date startSchedule = null, endSchedule = null, departureTime=null;

		Double dx = p.getX();
		Integer px = dx.intValue();
		Double dy = p.getY();
		Integer py = dy.intValue();
		Point pIntegerCoordinates = new Point(px, py);
		ArrayList<Intersection> intersections = new ArrayList<Intersection>();
		intersections = map.getIntersections();
		Warehouse warehouse = deliveryQuery.getWarehouse();
		Delivery[] deliveries = deliveryQuery.getDelivery();
		Point planPoint;
		int idPoint = 999999999;
		for (Intersection in : intersections) {
			planPoint = in.getCoordinates();
			Double dppx = (planPoint.getX())/coefficient;
			Integer ippx = dppx.intValue();
			Double dppy = (planPoint.getY())/coefficient;
			Integer ippy = dppy.intValue();
			if ((px < (ippx + 10)) && (px > (ippx - 10)) && (py < (ippy + 10)) && (py > (ippy - 10))) {
				idPoint = in.getId();
				break;
			}

		}

		for (Delivery d : deliveries) {
			int deliveryId = d.getIntersection().getId();
			if (idPoint < 999999999) {
				if (deliveryId == idPoint) {
					adresse = idPoint;
					duration = d.getDuration();
					startSchedule = d.getStartSchedule();
					endSchedule = d.getEndSchedule();
				}
			}

		}
		
		int idWarehouse = warehouse.getIntersection().getId();
		if(idPoint == idWarehouse){
			adresse = idPoint;
			departureTime= warehouse.getDepartureTime();
			
		}
		
		if(adresse != 999999999){
			JLabel titleLabel = new JLabel("Détails:  ");
			this.add(titleLabel);
			JLabel adresseLabel = new JLabel("Adresse : " + adresse+"  ");
			this.add(adresseLabel);
		}
		
		if(duration !=0){
			JLabel durationLabel = new JLabel("Durée : " + duration+" min");
			this.add(durationLabel);
		}
		
		if (startSchedule != null) {
			Calendar startCalendar = GregorianCalendar.getInstance();
			startCalendar.setTime(startSchedule);
			JLabel startScheduleLabel = new JLabel("Début de la plage horaire: " + startCalendar.get(Calendar.HOUR_OF_DAY)+"h"+startCalendar.get(Calendar.MINUTE)+" ");
			this.add(startScheduleLabel);
		}
		if (endSchedule != null) {
			Calendar endCalendar = GregorianCalendar.getInstance();
			endCalendar.setTime(endSchedule);
			JLabel endScheduleLabel = new JLabel("Fin de la plage horaire: " + endCalendar.get(Calendar.HOUR_OF_DAY)+"h"+endCalendar.get(Calendar.MINUTE)+" ");
			this.add(endScheduleLabel);
		}
		if(departureTime != null){
			Calendar departureCalendar = GregorianCalendar.getInstance();
			departureCalendar.setTime(departureTime);
			JLabel departureLabel= new JLabel("Date de départ: "+departureCalendar.get(Calendar.HOUR_OF_DAY)+"h"+departureCalendar.get(Calendar.MINUTE)+"  ");
			this.add(departureLabel);
		}

	}

}
