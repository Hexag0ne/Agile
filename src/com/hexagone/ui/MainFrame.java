package com.hexagone.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;


public class MainFrame extends JFrame {

	final private JPanel all;
	private JPanel mapPanel;
	private JPanel header;
	private JPanel detail;
	private Map map;
	
	MainFrame() throws XMLException, ParserConfigurationException, SAXException, IOException {
		super();

		
		
		ActionListener uploadMap = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				map = new Map();
				try {
					map= XMLDeserialiser.loadMap();
					mapPanel= new MapFrame(map);
					all.add(mapPanel,BorderLayout.CENTER);
					all.validate();
                    all.repaint();
				} catch (XMLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
				
			}
		};

		this.setTitle("Delivery App"); 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize.width, screenSize.height);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		

		all = new JPanel();
		all.setLayout(new BorderLayout());
		all.setBackground(Color.WHITE);
		

		header=new JPanel();
		header.setLayout(new GridLayout(2, 2));
		JLabel mapLabel = new JLabel("Plan: ");
		header.add(mapLabel);
		JButton loadMap = new JButton("Charger");
		loadMap.addActionListener(uploadMap);
		header.add(loadMap);
		JLabel deliveryLabel = new JLabel("Livraison: ");
		header.add(deliveryLabel);
		JButton loadDelivery = new JButton("Charger");
		header.add(loadDelivery);

		
		
		

		//detail =new JPanel();

		all.add(header, BorderLayout.NORTH);
		//all.add(mapPanel,BorderLayout.WEST);
		//all.add(detail, BorderLayout.EAST);

		this.setContentPane(all);
	}


	public static void main(String[] args) throws XMLException, ParserConfigurationException, SAXException, IOException {

		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

	}



}
