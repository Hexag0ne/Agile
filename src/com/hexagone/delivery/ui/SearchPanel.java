package com.hexagone.delivery.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.hexagone.delivery.control.UserActions;

public class SearchPanel extends JPanel {

	private UserActions controller;
	private int idDP;
	private JTextField searchZone;

	public SearchPanel(UserActions controller) {
		super();
		this.controller=controller;
		
		setLayout(new FlowLayout());
		
		JLabel searchLabel = new JLabel("Rechercher un point de livraison");
		add(searchLabel);
		
		searchZone = new JTextField();
		searchZone.setSize(100, 50);
		searchZone.setColumns(5);
		JPanel searchZonePanel = new JPanel(new FlowLayout());
		searchZonePanel.setSize(getPreferredSize().width - 10, getPreferredSize().height);
		searchZonePanel.add(searchZone);
		add(searchZonePanel);
		ImageIcon i = new ImageIcon(getClass().getClassLoader().getResource("resources/search.png"));
		JButton searchButton = new JButton(i);
		searchButton.setPreferredSize(new Dimension(i.getIconWidth(), i.getIconHeight()));
		searchButton.addActionListener(new SearchListener());
		JPanel searchButtonPanel = new JPanel(new FlowLayout());
		searchButtonPanel.setSize(i.getIconWidth(),i.getIconHeight());
		searchButtonPanel.add(searchButton);
		add(searchButtonPanel);
		setPreferredSize(getPreferredSize());
		setBackground(Color.white);
		

	}
	
	private class SearchListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			idDP = Integer.parseInt(searchZone.getText());
			controller.searchDP(idDP);
		}
	}


}
