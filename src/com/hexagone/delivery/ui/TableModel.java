package com.hexagone.delivery.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.hexagone.delivery.models.Delivery;


public class TableModel extends AbstractTableModel {

	private static Vector<String> columnNames = new Vector<String>();
	private Vector<Delivery> data;

	public TableModel(Vector<Delivery> data) {
		this.data=data;
		columnNames.addElement("N°");
		columnNames.addElement("Durée(min)");
		columnNames.addElement("Plage horaire");
		columnNames.addElement("Heure d'arrivée");
		columnNames.addElement("Heure de départ");
		columnNames.addElement("Temps d'attente (min)");

	}
	@Override
	 public String getColumnName(int col) {
	        return columnNames.get(col);
	    }
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.size();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = "??";
		Delivery dp = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                value = dp.getIntersection().getId();
                break;
            case 1:
                value = (dp.getDuration())/60;
                break;
            case 2:
                value = dp.getTimeslotDelivery();
                break;
            case 3:
                value = dp.getArrivalTimeString();
                break;
            case 4:
                value = dp.getDepartureTimeString();
                break;
            case 5:
                value = dp.getWaitingTime();
                break;
                
        }

        return value;
	}

}
