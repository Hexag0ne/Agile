package com.hexagone.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.hexagone.delivery.algo.CompleteGraphComputer;
import com.hexagone.delivery.algo.TSPSolverV1;
import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;
import com.hexagone.ui.MapFrame;

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
	// a panel with the map, a scroll bar, a search bar and a zoom button
	private JPanel mainPanel;
	private static Map map;
	private static int coefficient = 2;
	private DeliveryQuery deliveryQuery;
	private Point p;
	private JButton computeTourButton;
	private static int deliveryPoint = 0;
	private Integer[] deliveryIntersections;
	private static LinkedHashMap<Integer, ArrayList<Road>> tour;
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
				if(deliveryPoint < tour.size()){
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
		// Listener for calcuateTour button
		ActionListener calculateTourListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tour = new LinkedHashMap<>();
				ArrayList<Road> road21 = new ArrayList<>();
				road21.add(new Road(21, 16));
				road21.add(new Road(16, 11));
				road21.add(new Road(11, 12));
				road21.add(new Road(12, 13));
				tour.put(21, road21);

				ArrayList<Road> road13 = new ArrayList<>();
				road13.add(new Road(13, 8));
				road13.add(new Road(8, 7));
				road13.add(new Road(7, 2));
				road13.add(new Road(2, 3));
				road13.add(new Road(3, 4));
				road13.add(new Road(4, 9));
				tour.put(13, road13);

				ArrayList<Road> road9 = new ArrayList<>();
				road9.add(new Road(9, 4));
				road9.add(new Road(4, 3));
				tour.put(9, road9);

				ArrayList<Road> road3 = new ArrayList<>();
				road3.add(new Road(3, 2));
				road3.add(new Road(2, 1));
				tour.put(3, road3);

				ArrayList<Road> road1 = new ArrayList<>();
				road1.add(new Road(1, 0));
				road1.add(new Road(0, 5));
				road1.add(new Road(5, 10));
				road1.add(new Road(10, 11));
				road1.add(new Road(11, 16));
				road1.add(new Road(16, 21));
				tour.put(1, road1);

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
				JButton nextButton = new JButton("Point de livraison suivant");
				nextButton.addActionListener(nextDPListenner);
				navigateTourPanel.add(nextButton);
				JButton precedentButton = new JButton("Point de livraison précédent");
				precedentButton.addActionListener(precedentDPListenner);
				navigateTourPanel.add(precedentButton);
				mainPanel.add(navigateTourPanel, BorderLayout.WEST);
				mainPanel.validate();
				mainPanel.repaint();
				all.validate();
				all.repaint();
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
		

		// mainPanel components
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.addMouseListener(details);
		// JScrollBar vbar=new JScrollBar(JScrollBar.VERTICAL, 30, 20, 0, 300);
		// vbar.setUnitIncrement(2);
		// vbar.setBlockIncrement(1);
		// mainPanel.add(vbar, BorderLayout.EAST);

		all.add(mainPanel);
		all.add(header, BorderLayout.NORTH);


		setFocusable(true);
		this.setContentPane(all);
	}

	/**
	 * This class provides the reaction to be performed upon clicking on the
	 * "Calculer Tournée" button
	 */
	

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
			// Boolean b = MapFrame.checkPoint(p);
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
