package com.hexagone.delivery.ui;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTable;

import com.hexagone.delivery.models.Delivery;

/**
 * Panel for the tour table.
 *
 */
public class TourTablePanel extends JPanel {

	private JTable table;
	private TableModel tableModel;
	private Vector<Delivery> data;


	public void setTableData(Vector<Delivery> data) {
		tableModel = new TableModel(data);

		table.setModel(tableModel);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(27);
		table.getColumnModel().getColumn(1).setPreferredWidth(70);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(150);

		add(table.getTableHeader(), BorderLayout.PAGE_START);
		add(table, BorderLayout.CENTER);
	}

	public void selectionRow(int numberRow) {
		if (numberRow < table.getRowCount()) {
			table.setRowSelectionInterval(numberRow, numberRow);
		}
	}


	public void resetTableModel() {
		tableModel = null;

	}
	
		/**
		 * Constructor for the tour table panel
		 * 
		 */
		public TourTablePanel() {
		super();
		table = new JTable();

		setLayout(new BorderLayout());

		add(table.getTableHeader(), BorderLayout.PAGE_START);
		add(table, BorderLayout.CENTER);
	}

		public int getRowSelected() {
			return table.getSelectedRow()+1;
		}
}
