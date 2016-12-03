package com.hexagone.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.hexagone.delivery.models.Delivery;


public class TableModel extends AbstractTableModel {

	private static Vector<String> columnNames = new Vector<String>();
	private Vector<Delivery> data;

	public TableModel(Vector<Delivery> data) {
		this.data=data;
		columnNames.addElement("N°");
		columnNames.addElement("Durée");
		columnNames.addElement("Plage horaire");
		columnNames.addElement("Temps d'arrivée");
		columnNames.addElement("Temps de départ");
		columnNames.addElement("Temps d'attente");

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
                value = dp.getDuration();
                break;
            case 2:
                value = dp.getTimeslotDelivery();
                break;
            case 3:
                value = dp.getArrivalTime();
                break;
            case 4:
                value = dp.getDepartureTime();
                break;
            case 5:
                value = dp.getWaitingTime();
                break;
                
        }

        return value;
	}

}
