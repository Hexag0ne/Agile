package com.hexagone.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.Warehouse;

public class TourPanel extends JPanel {

	private DeliveryQuery deliveryQuery;
	private int deliveryPoint;
	private LinkedHashMap<Integer, ArrayList<Road>> tour;
	public TourPanel(DeliveryQuery deliveyQuery, int deliveryPoint,LinkedHashMap<Integer, ArrayList<Road>> tour){

		super();
		this.deliveryQuery=deliveyQuery;
		this.deliveryPoint=deliveryPoint;
		this.tour=tour;

		GridLayout fl = new GridLayout(8, 1);
		setLayout(fl);
		setBackground(Color.WHITE);
		
		int adresse = 999999999, duration = 0;
		Date startSchedule = null, endSchedule = null, departureTimeWarehouse = null,arrivalTime= null,departureTime= null,waitingTime= null;

		int i=0;
		Boolean founded=false;
		Delivery[] deliveries = deliveryQuery.getDeliveries();
		
		Set<Entry<Integer, ArrayList<Road>>> entrySet= tour.entrySet();
		Iterator<Entry<Integer, ArrayList<Road>>> iterator= entrySet.iterator();
		Entry<Integer, ArrayList<Road>> entry=null;
		while(i<deliveryPoint+1 && iterator.hasNext()){
			entry=iterator.next();
			i++;
        }
		
		for(Delivery d: deliveries){
			if((d.getIntersection().getId()).equals(entry.getKey())){
				adresse=d.getIntersection().getId();
				duration=d.getDuration();
				startSchedule=d.getStartSchedule();
				endSchedule=d.getEndSchedule();
				founded=true;
				break;

			}
		}
		if(founded == false){
			if((deliveyQuery.getWarehouse().getIntersection().getId()).equals(entry.getKey())){
				adresse= deliveyQuery.getWarehouse().getIntersection().getId();
				departureTimeWarehouse=deliveyQuery.getWarehouse().getDepartureTime();
			}
		}






		JLabel adresseLabel = new JLabel("Adresse: " + adresse);
		add(adresseLabel);
		if(duration != 0){
			JLabel durationLabel = new JLabel("Durée: "+duration+" min");
			add(durationLabel);
		}

		if(departureTimeWarehouse != null){
			Calendar departureTimeWarehouseCalendar = GregorianCalendar.getInstance();
			departureTimeWarehouseCalendar.setTime(departureTimeWarehouse);
			JLabel departureTimeWarehouseLabel = new JLabel("Heure du départ de l'entrepôt: "+departureTimeWarehouseCalendar.get(Calendar.HOUR_OF_DAY)+"h"+departureTimeWarehouseCalendar.get(Calendar.MINUTE)+"min");
			add(departureTimeWarehouseLabel);
		}
		if(startSchedule!=null){
			Calendar startScheduleCalendar = GregorianCalendar.getInstance();
			startScheduleCalendar.setTime(startSchedule);
			Calendar endScheduleCalendar = GregorianCalendar.getInstance();
			endScheduleCalendar.setTime(endSchedule);
			JLabel timeslotDelivery = new JLabel("Plage horaire: "+startScheduleCalendar.get(Calendar.HOUR_OF_DAY)+"h"+"-"+endScheduleCalendar.get(Calendar.HOUR_OF_DAY)+"h");
			add(timeslotDelivery);
			JLabel arrivalTimeLabel = new JLabel("Temps d'arrivée: ");
			add(arrivalTimeLabel);
			JLabel departureTimeLabel = new JLabel("Temps de départ: ");
			add(departureTimeLabel);
			JLabel waitingTimeLabel = new JLabel("Temps d'attente: ");
			add(waitingTimeLabel);
		}

		



	}
}
