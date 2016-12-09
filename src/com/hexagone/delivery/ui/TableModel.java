package com.hexagone.delivery.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.hexagone.delivery.models.Delivery;

/**
 * Model of a table. Extends AbstractTableModel
 *
 */

public class TableModel extends AbstractTableModel {

	private Vector<String> columnNames;
	private Vector<Delivery> data;

	public TableModel(Vector<Delivery> data) {
		this.columnNames = new Vector<String>(); 
		this.data = data;
		columnNames.addElement("N°");
		columnNames.addElement("Durée(min)");
		columnNames.addElement("Heure d'arrivée");
		columnNames.addElement("Heure de départ");
		if (this.hasSchedule()) {
			columnNames.addElement("Plage horaire");
			columnNames.addElement("Temps d'attente (min)");
		}
	}
	
	public boolean hasSchedule() {
		for (Delivery d : data) {
			if (d.getStartSchedule() != null) {
				return true;
			}
		}
		return false;
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

	/**
	 * Gets the value at a row Index and a column Index
	 * 
	 * @param rowIndex
	 *            : index of the row as an Integer
	 * @param columnIndex
	 *            : index of the column as an Integer
	 * @return the value Object
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = "??";
		Delivery dp = data.get(rowIndex);
		if (columnIndex == 0) {
			value = dp.getIntersection().getId();
		}
		if (columnIndex == 1) {
			value = (dp.getDuration()) / 60;
		}
		if (columnIndex == 2) {
			value = dp.getArrivalTimeString();
		}
		if (columnIndex == 3) {
			value = dp.getDepartureTimeString();
		}
		if (this.hasSchedule()) {
			if (columnIndex == 4) {
				value = dp.getTimeslotDelivery();
			}
			if (columnIndex == 5) {
				value = dp.getWaitingTime();
			}	
		}
		return value;
	}

}
