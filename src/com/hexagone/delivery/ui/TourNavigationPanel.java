package com.hexagone.delivery.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.hexagone.delivery.control.UserActions;

/**
 * Panel for the tour navigation.
 */
public class TourNavigationPanel extends JPanel {

	UserActions controller;

	private JButton nextDeliveryButton;
	private JButton previousDeliveryButton;


	/**
	 * Constructor for the tour navigation panel
	 * 
	 * @param controller
	 *            the controller implementing the UserActions to be performed
	 *            when an event occurs
	 * 
	 */
	public TourNavigationPanel(UserActions controller) {
		this.controller = controller;

		this.setLayout(new GridLayout(1, 2));

		previousDeliveryButton = new JButton("Point de livraison précédent");
		previousDeliveryButton.addActionListener(new actionPreviousDeliveryListener());
		this.add(previousDeliveryButton);

		nextDeliveryButton = new JButton("Point de livraison suivant");
		nextDeliveryButton.addActionListener(new actionNextDeliveryListener());
		this.add(nextDeliveryButton);

		/*
		
		searchPanel = new JPanel(new FlowLayout());
		JLabel searchLabel = new JLabel("Supprimer/Modifier un point de livraison");

		add(searchLabel);
		searchZone = new JTextField("N° Point de livraison");
		searchZone.setSize(100, 50);
		searchZonePanel = new JPanel(new FlowLayout());
		searchZonePanel.setSize(searchPanel.getPreferredSize().width - 10, searchPanel.getPreferredSize().height);
		searchZonePanel.add(searchZone);
		searchPanel.add(searchZonePanel);
		searchButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("resources/search.png")));
		// searchButton.addActionListener(searchListener);
		searchButtonPanel = new JPanel(new FlowLayout());
		searchButtonPanel.setSize(searchPanel.getPreferredSize().width - searchZonePanel.getPreferredSize().width,
				searchPanel.getPreferredSize().height);
		searchButtonPanel.add(searchButton);
		searchPanel.add(searchButtonPanel);
		searchPanel.setPreferredSize(getPreferredSize());
		searchPanel.setBackground(Color.white);
		add(searchPanel);
		addDP = new JButton("Ajouter un point de livraison");
		add(addDP);
		setBackground(Color.white);
		
		*/
	}

	private class actionNextDeliveryListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.nextDelivery();
		}
	}

	/**
	 * Class handling what to do when the user wants to look at the previous
	 * delivery
	 */
	private class actionPreviousDeliveryListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.previousDelivery();
		}
	}
}
