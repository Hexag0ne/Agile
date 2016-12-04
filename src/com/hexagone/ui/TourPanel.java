package com.hexagone.ui;

import java.awt.BorderLayout;
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
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.hexagone.delivery.models.ArrivalPoint;
import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.Warehouse;

public class TourPanel extends JPanel {

	private DeliveryQuery deliveryQuery;
	private int deliveryPoint;
	private LinkedHashMap<Integer, ArrivalPoint> tour;
	private static Vector<Delivery> data;
	private static JTable table;
	public TourPanel(int deliveryPoint,LinkedHashMap<Integer, ArrivalPoint> tour){

		
		this.deliveryPoint=deliveryPoint;
		this.tour=tour;

		
		data= new Vector<Delivery>();
		
		Set<Entry<Integer, ArrivalPoint>> entrySet= tour.entrySet();
		Iterator<Entry<Integer, ArrivalPoint>> iterator= entrySet.iterator();
		Entry<Integer, ArrivalPoint> entry=null;
		
		while(iterator.hasNext()){
			entry=iterator.next();
			data.addElement(entry.getValue().getDelivery());
			break;

        }
		if(table == null){
			table = new JTable(new TableModel(data));
			table.setFillsViewportHeight(true);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(27);
			table.getColumnModel().getColumn(1).setPreferredWidth(40);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(3).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(5).setPreferredWidth(100);
		}
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		if(deliveryPoint<table.getRowCount()){
			table.setRowSelectionInterval(deliveryPoint,deliveryPoint);
		}
		setLayout(new BorderLayout());
		add(table.getTableHeader(), BorderLayout.PAGE_START);
		add(table, BorderLayout.CENTER);
		
		
		
	}
	
	public TourPanel(DeliveryQuery deliveyQuery,LinkedHashMap<Integer, ArrivalPoint> tour, int searchDP){

		super();
		this.deliveryQuery=deliveyQuery;
		this.tour=tour;

		// number of the row of the delivery point searched
		int rowNumber=0;
		data= new Vector<Delivery>();
		
		Delivery[] deliveries = deliveryQuery.getDeliveries();
		
		Set<Entry<Integer, ArrivalPoint>> entrySet= tour.entrySet();
		Iterator<Entry<Integer, ArrivalPoint>> iterator= entrySet.iterator();
		Entry<Integer, ArrivalPoint> entry=null;
		int i=0;
		while(iterator.hasNext()){
			entry=iterator.next();
			for(Delivery d: deliveries){
				if((d.getIntersection().getId()).equals(entry.getKey())){
				    i++;
					data.addElement(entry.getValue().getDelivery());
					if((d.getIntersection().getId()).equals(searchDP)){
						rowNumber=i-1;
					}
					break;

				}
			}
        }
		if(table == null){
			table = new JTable(new TableModel(data));
			table.setFillsViewportHeight(true);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(27);
			table.getColumnModel().getColumn(1).setPreferredWidth(40);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(3).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(5).setPreferredWidth(100);
		}
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		if(deliveryPoint<table.getRowCount()){
			if(rowNumber !=0 ){
				table.setRowSelectionInterval(rowNumber,rowNumber);
			}
			
		}
		setLayout(new BorderLayout());
		add(table.getTableHeader(), BorderLayout.PAGE_START);
		add(table, BorderLayout.CENTER);
		
		
	}
}
