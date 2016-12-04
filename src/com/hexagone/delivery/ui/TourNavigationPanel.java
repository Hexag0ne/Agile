package com.hexagone.delivery.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TourNavigationPanel extends JPanel {

	private JButton beginDeliveryButton;
	private JButton nextDeliveryButton;
	private JButton previousDeliveryButton;
	
	private JPanel searchPanel;
	private JTextField searchZone;
	
	
	public TourNavigationPanel() {
		this.setLayout(new GridLayout(10, 1));
		
		beginDeliveryButton = new JButton("Commencer la tournée");
		this.add(beginDeliveryButton);
		
		previousDeliveryButton = new JButton("Point de livraison précédent");
		this.add(previousDeliveryButton);
		
		nextDeliveryButton = new JButton("Point de livraison suivant");
		this.add(nextDeliveryButton);
		
		searchPanel = new JPanel(new FlowLayout());
		JLabel searchLabel = new JLabel("Supprimer/Modifier un point de livraison");
		
		add(searchLabel);
		searchZone = new JTextField("N° Point de livraison");
		searchZone.setSize(100, 50);
		JPanel searchZonePanel = new JPanel(new FlowLayout());
		searchZonePanel.setSize(searchPanel.getPreferredSize().width-10,searchPanel.getPreferredSize().height);
		searchZonePanel.add(searchZone);
		searchPanel.add(searchZonePanel);
		JButton searchButton = new JButton(new ImageIcon(getClass().getResource("/search.png")));
		//searchButton.addActionListener(searchListener);
		JPanel searchButtonPanel = new JPanel(new FlowLayout());
		searchButtonPanel.setSize(searchPanel.getPreferredSize().width-searchZonePanel.getPreferredSize().width,searchPanel.getPreferredSize().height);
		searchButtonPanel.add(searchButton);
		searchPanel.add(searchButtonPanel);
		searchPanel.setPreferredSize(getPreferredSize());
		searchPanel.setBackground(Color.white);
		add(searchPanel);
		JButton addDP = new JButton("Ajouter un point de livraison");
		add(addDP);
		setBackground(Color.white);

	}
}
