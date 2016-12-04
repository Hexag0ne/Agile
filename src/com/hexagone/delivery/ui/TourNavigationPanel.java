package com.hexagone.delivery.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.hexagone.delivery.control.UserActions;

public class TourNavigationPanel extends JPanel {

	UserActions controller;
	
	private JButton beginDeliveryButton;
	private JButton nextDeliveryButton;
	private JButton previousDeliveryButton;
	
	private JPanel searchPanel;
	private JTextField searchZone;
	private JPanel searchZonePanel;
	private JButton searchButton;
	private JPanel searchButtonPanel;
	private JButton addDP;
	
	
	public TourNavigationPanel(UserActions controller) {
		this.controller = controller;
		
		this.setLayout(new GridLayout(10, 1));
		
		beginDeliveryButton = new JButton("Commencer la tournée");
		beginDeliveryButton.addActionListener(new actionStartTourListener());
		this.add(beginDeliveryButton);
		
		previousDeliveryButton = new JButton("Point de livraison précédent");
		previousDeliveryButton.addActionListener(new actionPreviousDeliveryListener());
		this.add(previousDeliveryButton);
		
		nextDeliveryButton = new JButton("Point de livraison suivant");
		previousDeliveryButton.addActionListener(new actionNextDeliveryListener());
		this.add(nextDeliveryButton);
		
		searchPanel = new JPanel(new FlowLayout());
		JLabel searchLabel = new JLabel("Supprimer/Modifier un point de livraison");
		
		add(searchLabel);
		searchZone = new JTextField("N° Point de livraison");
		searchZone.setSize(100, 50);
		searchZonePanel = new JPanel(new FlowLayout());
		searchZonePanel.setSize(searchPanel.getPreferredSize().width-10,searchPanel.getPreferredSize().height);
		searchZonePanel.add(searchZone);
		searchPanel.add(searchZonePanel);
		searchButton = new JButton(new ImageIcon(getClass().getResource("/search.png")));
		//searchButton.addActionListener(searchListener);
		searchButtonPanel = new JPanel(new FlowLayout());
		searchButtonPanel.setSize(searchPanel.getPreferredSize().width-searchZonePanel.getPreferredSize().width,searchPanel.getPreferredSize().height);
		searchButtonPanel.add(searchButton);
		searchPanel.add(searchButtonPanel);
		searchPanel.setPreferredSize(getPreferredSize());
		searchPanel.setBackground(Color.white);
		add(searchPanel);
		addDP = new JButton("Ajouter un point de livraison");
		add(addDP);
		setBackground(Color.white);
	}
	
	private class actionStartTourListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.startNavigationButtonClick();
		}
	}
	
	private class actionNextDeliveryListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.nextDelivery();
		}
	}
	
	private class actionPreviousDeliveryListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.previousDelivery();
		}
	}
}
