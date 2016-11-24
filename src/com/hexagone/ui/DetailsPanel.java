package com.hexagone.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;

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

	public DetailsPanel(Map map, DeliveryQuery deliveryQuery, Point p) {
		// TODO Auto-generated constructor stub
		super();
		this.map = map;
		this.deliveryQuery = deliveryQuery;
		this.p = p;
		GridLayout fl = new GridLayout(5, 1);
		setLayout(fl);
		setBackground(Color.WHITE);

		int adresse = 999999999, duration = 0;
		Date startSchedule = null, endSchedule = null;

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
			Double dppx = (planPoint.getX()) / 2;
			Integer ippx = dppx.intValue();
			Double dppy = (planPoint.getY()) / 2;
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

		JLabel titleLabel = new JLabel("Détails:");
		this.add(titleLabel);
		JLabel adresseLabel = new JLabel("Adresse :" + adresse);
		this.add(adresseLabel);
		JLabel durationLabel = new JLabel("Durée :" + duration);
		this.add(durationLabel);
		if (startSchedule != null) {
			JLabel startScheduleLabel = new JLabel("Date de départ: " + startSchedule.toString());
			this.add(startScheduleLabel);
		}
		if (endSchedule != null) {
			JLabel endScheduleLabel = new JLabel("Date de départ: " + endSchedule.toString());
			this.add(endScheduleLabel);
		}

	}

}
