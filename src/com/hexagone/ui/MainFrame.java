package com.hexagone.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.models.ArrivalPoint;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.Route;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;



public class MainFrame extends JFrame {

	final private JPanel all;
	private MapFrame mapPanel;
	private JPanel deliveryPanel;
	private JPanel header;
	private JPanel detailPanel;
	private JPanel navigateTourPanel;
	private MapFrame tourPanel;
	private TourPanel tourDetailsPanel; 
	private MapTour mapTourPanel;
	private JPanel searchPanel;
	private JPanel mainPanel;
	private static Map map;
	private static float coefficient = 1.25f;
	private DeliveryQuery deliveryQuery;
	private Point p;
	private JButton computeTourButton;
	private static int deliveryPoint = 0;
	private Integer[] deliveryIntersections;
	private static LinkedHashMap<Integer, ArrivalPoint> tour;
	private JTextField searchZone;
	public Integer[] getDeliveryIntersections() {
		return deliveryIntersections;
	}

	public MainFrame() throws XMLException {
		super();

		// Listener for "Charger Plan" Button
		ActionListener uploadMap = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				map = new Map();
				try {
					map = XMLDeserialiser.loadMap();
					mapPanel = new MapFrame(map, null, false, coefficient, null);
					mainPanel.add(mapPanel, BorderLayout.CENTER);
					mainPanel.validate();
					mainPanel.repaint();
					all.validate();
					all.repaint();
					
				} catch (XMLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		};

		// Listener for "Charger Livraison" Button
		ActionListener uploadDelivery = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				deliveryQuery = new DeliveryQuery();
				try {
					deliveryQuery = XMLDeserialiser.loadDeliveryQuery();
					deliveryPanel = new MapFrame(map, deliveryQuery, false, coefficient, null);
					deliveryPanel.repaint();
					deliveryPanel.addMouseListener(details);
					mainPanel.remove(mapPanel);
					mainPanel.add(deliveryPanel, BorderLayout.CENTER);
					mainPanel.validate();
					mainPanel.repaint();
					all.validate();
					all.repaint();
				} catch (XMLException e1) {
					e1.printStackTrace();
				}

			}
		};

		ActionListener startListenner =new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				    deliveryPoint=0;
					mainPanel.remove(tourPanel);
					mapTourPanel = new MapTour(map, deliveryQuery, coefficient, tour, deliveryPoint);
					mainPanel.add(mapTourPanel,BorderLayout.CENTER);
					if(tourDetailsPanel != null){
						mainPanel.remove(tourDetailsPanel);
					}
					tourDetailsPanel = new TourPanel(deliveryQuery, deliveryPoint,tour); 
					mainPanel.add(tourDetailsPanel, BorderLayout.EAST);
					mainPanel.validate();
					mainPanel.repaint();
					all.validate();
					mainPanel.repaint();
				

			}
		};
		
		ActionListener nextDPListenner =new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				if(deliveryPoint < tour.size()){
					deliveryPoint++;
					mainPanel.remove(mapTourPanel);
					mapTourPanel = new MapTour(map, deliveryQuery, coefficient, tour, deliveryPoint);
					mainPanel.add(mapTourPanel,BorderLayout.CENTER);
					mainPanel.remove(tourDetailsPanel);
					tourDetailsPanel = new TourPanel(deliveryQuery, deliveryPoint,tour); 
					mainPanel.add(tourDetailsPanel, BorderLayout.EAST);
					mainPanel.validate();
					mainPanel.repaint();
					all.validate();
					mainPanel.repaint();
				}
			}
		};
		
		ActionListener precedentDPListenner =new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(deliveryPoint > 0){
					deliveryPoint--;
					mainPanel.remove(mapTourPanel);
					mapTourPanel = new MapTour(map, deliveryQuery, coefficient, tour, deliveryPoint);
					mainPanel.add(mapTourPanel,BorderLayout.CENTER);
					mainPanel.remove(tourDetailsPanel);
					tourDetailsPanel = new TourPanel(deliveryQuery, deliveryPoint,tour); 
					mainPanel.add(tourDetailsPanel, BorderLayout.EAST);
					mainPanel.validate();
					mainPanel.repaint();
					all.validate();
					mainPanel.repaint();
				}
			}
		};
		
		// Listener for searchButton
		ActionListener searchListener = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int numberDP = Integer.parseInt(searchZone.getText());
				mainPanel.remove(tourDetailsPanel);
				tourDetailsPanel = new TourPanel(deliveryQuery,tour,numberDP); 
				mainPanel.add(tourDetailsPanel, BorderLayout.EAST);
				mainPanel.validate();
				mainPanel.repaint();
				all.validate();
				mainPanel.repaint();
				
			}
			
		};
		// Listener for calcuateTour button
		ActionListener calculateTourListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tour = new LinkedHashMap<>();
				DeliveryComputer dc = new DeliveryComputer(map, deliveryQuery);
				Route r = new Route(map, deliveryQuery, dc);
				r.generateRoute();
				tour = r.getRoute();
				tourPanel = new MapFrame(map, deliveryQuery, true, coefficient, tour);
				tourPanel.repaint();
				mainPanel.remove(deliveryPanel);
				mainPanel.add(tourPanel, BorderLayout.CENTER);
				navigateTourPanel = new JPanel();
				GridLayout fl = new GridLayout(10, 1);
				navigateTourPanel.setLayout(fl);
				JButton startButton = new JButton("Commencer la tournée");
				startButton.addActionListener(startListenner );
				navigateTourPanel.add(startButton);
				JButton precedentButton = new JButton("Point de livraison précédent");
				precedentButton.addActionListener(precedentDPListenner);
				navigateTourPanel.add(precedentButton);
				JButton nextButton = new JButton("Point de livraison suivant");
				nextButton.addActionListener(nextDPListenner);
				navigateTourPanel.add(nextButton);
				searchPanel = new JPanel(new FlowLayout());
				JLabel searchLabel = new JLabel("Supprimer/Modifier un point de livraison");
				navigateTourPanel.add(searchLabel);
				searchZone = new JTextField("N° Point de livraison");
				searchZone.setSize(100, 50);
				JPanel searchZonePanel = new JPanel(new FlowLayout());
				searchZonePanel.setSize(searchPanel.getPreferredSize().width-10,searchPanel.getPreferredSize().height);
				searchZonePanel.add(searchZone);
				searchPanel.add(searchZonePanel);
				JButton searchButton = new JButton(new ImageIcon(getClass().getResource("/search.png")));
				searchButton.addActionListener(searchListener);
				JPanel searchButtonPanel = new JPanel(new FlowLayout());
				searchButtonPanel.setSize(searchPanel.getPreferredSize().width-searchZonePanel.getPreferredSize().width,searchPanel.getPreferredSize().height);
				searchButtonPanel.add(searchButton);
				searchPanel.add(searchButtonPanel);
				searchPanel.setPreferredSize(navigateTourPanel.getPreferredSize());
				searchPanel.setBackground(Color.white);
				navigateTourPanel.add(searchPanel);
				JButton addDP = new JButton("Ajouter un point de livraison");
				navigateTourPanel.add(addDP);
				navigateTourPanel.setBackground(Color.white);
				mainPanel.add(navigateTourPanel, BorderLayout.WEST);
				mainPanel.validate();
				mainPanel.repaint();
				all.validate();
				all.repaint();
			}
		};
		
		

		this.setTitle("Delivery App");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize.width, screenSize.height-50);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);

		all = new JPanel();
		all.setLayout(new BorderLayout());
		

		// header components
		header = new JPanel();
		header.setLayout(new GridLayout(1, 5));
		JButton loadMap = new JButton("Charger Plan");
		loadMap.addActionListener(uploadMap);
		header.add(loadMap);
		JButton loadDelivery = new JButton("Charger Livraison");
		loadDelivery.addActionListener(uploadDelivery);

		header.add(loadDelivery);
		computeTourButton = new JButton("Calculer Tournée");
		computeTourButton.addActionListener(calculateTourListener);
		header.add(computeTourButton);

		JButton generatePlanning = new JButton("Générer feuille de route");
		header.add(generatePlanning);
		generatePlanning.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
			}

		});

		// mainPanel components
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.addMouseListener(details);
		all.add(mainPanel,BorderLayout.CENTER);
		all.add(header, BorderLayout.NORTH);

		setFocusable(true);
		this.setContentPane(all);
	}

	/**
	 * Mouse Listener class Helps displaying details about the various delivery
	 * points
	 */
	final MouseListener details = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			p = e.getPoint();
			/**
			 * TODO Repair display of delivery points details.
			 */
			
			if (true) {
				if (detailPanel != null) {
					all.remove(detailPanel);
				}
				detailPanel = new DetailsPanel(map, deliveryQuery, p, coefficient);
				all.add(detailPanel, BorderLayout.EAST);
				all.validate();
				all.repaint();
				validate();
				repaint();
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}
	};
}
