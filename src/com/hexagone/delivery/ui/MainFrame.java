package com.hexagone.delivery.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.hexagone.delivery.control.MapPainter;
import com.hexagone.delivery.control.UserActions;
import com.hexagone.delivery.models.Delivery;

/**
 * Main window of the graphical user interface
 * 
 */
public class MainFrame extends JFrame {

	/** Controller of this instance of MainFrame */
	UserActions controller;

	/** JPanel containing all the elements in the frame */
	private JPanel allPanel;
	/** JPanel containing the top row of buttons */
	private JPanel headerPanel;
	/** JPanel containing the map drawing */
	private JPanel mapPanel;
	/** JPanel on the top right corner of the screen with buttons to navigate */
	private JPanel tourNavigationPanel;
	/** JPanel on the right side of the window, below the tourNavigationPanel */
	private TourTablePanel tourTablePanel;
	/** JPanel for searching a delivery point by Id */ 
	private SearchPanel searchPanel;

	private JPanel centerPanel;
	
	private JPanel rightPanel;
	private JPanel leftPanel;

	private JButton loadMapButton;
	private JButton loadDeliveryButton;
	private JButton computeTourButton;
	private JButton generatePlanning;

	

	/**
	 * Constructor for the main frame
	 * 
	 * @param controller
	 *            the controller implementing the UserActions to be performed
	 *            when an event occurs
	 */
	public MainFrame(UserActions controller, MapPainter painter) {
		
		this.controller = controller;
		setTitle("Delivery App");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height - 50);
		Dimension preferredSize = new Dimension(screenSize.width, screenSize.height - 50);
		setPreferredSize(preferredSize);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		allPanel = new JPanel();
		allPanel.setLayout(new BorderLayout());

		// header components
		headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(1, 5));

		loadMapButton = new JButton("Charger Plan");
		loadMapButton.addActionListener(new LoadMapListener());
		headerPanel.add(loadMapButton);

		loadDeliveryButton = new JButton("Charger Livraison");
		loadDeliveryButton.addActionListener(new LoadDeliveryListener());
		headerPanel.add(loadDeliveryButton);

		computeTourButton = new JButton("Calculer Tournée");
		computeTourButton.addActionListener(new ComputeTourListener());
		headerPanel.add(computeTourButton);

		generatePlanning = new JButton("Générer feuille de route");
		generatePlanning.addActionListener(new GeneratePlanningListener());
		headerPanel.add(generatePlanning);

		allPanel.add(headerPanel, BorderLayout.NORTH);

		centerPanel = new JPanel(new GridLayout(1, 2));

		//left side
		leftPanel = new JPanel(new BorderLayout());
		
		tourNavigationPanel = new TourNavigationPanel(controller);
		leftPanel.add(tourNavigationPanel, BorderLayout.SOUTH);

		mapPanel = new MapPanel(painter);
		leftPanel.add(mapPanel, BorderLayout.CENTER);
		
		centerPanel.add(leftPanel);
		
		//right side
		rightPanel = new JPanel(new BorderLayout());
		
		tourTablePanel = new TourTablePanel();
		rightPanel.add(tourTablePanel, BorderLayout.CENTER);
		
		searchPanel = new SearchPanel(controller);
		rightPanel.add(searchPanel, BorderLayout.NORTH);
		
		centerPanel.add(rightPanel);
		
		// centerPanel.addKeyListener(keyListener);
		centerPanel.addKeyListener(new KeyboardListenner());
		
		allPanel.add(centerPanel, BorderLayout.CENTER);

		// Set focus on center panel to detect keyboard events
		setFocusableOnCenterPanel();

		this.setContentPane(allPanel);
		this.pack();
	}

	public void resetTable() {
		tourTablePanel.resetTableModel();
	}

	public void setSidePanelsVisible(boolean visible) {
		tourNavigationPanel.setVisible(visible);
		tourTablePanel.setVisible(visible);
		searchPanel.setVisible(visible);
		this.pack();
	}

	public void setTableData(Vector<Delivery> data) {
		tourTablePanel.setTableData(data);
	}

	public void selectionRow(int step) {
		tourTablePanel.selectionRow(step);
	}

	public void setFocusableOnCenterPanel() {

		addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				centerPanel.requestFocusInWindow();
			}
		});
		setFocusable(true);
	}

	/**
	 * Class handling what to do when the user presses on the LoadMap button
	 */
	private class LoadMapListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.loadMapButtonClick();
		}
	}

	/**
	 * Class handling what to do when the user presses the loadDelivery button
	 */
	private class LoadDeliveryListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.loadDeliveryQueryButtonClick();
		}
	}

	/**
	 * Class handling what to do when the user presses on the button computeTour
	 *
	 */
	private class ComputeTourListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.computeRouteButtonClick();
		}

	}

	/**
	 * Class handles what to do when the button generatePLanning is clisked.
	 */
	private class GeneratePlanningListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.generatePlanningButtonClick();
		}

	}

	/**
	 * Class handles keyboard events.
	 */
	private class KeyboardListenner implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

			if ((keyCode == KeyEvent.VK_UP) || (keyCode == KeyEvent.VK_LEFT)) {
				controller.previousDelivery();

			}

			if ((keyCode == KeyEvent.VK_DOWN) || (keyCode == KeyEvent.VK_RIGHT)) {

				controller.nextDelivery();
			}

			if (keyCode == KeyEvent.VK_DELETE) {
				/*
				 * System.out.println("Yassine supprimer"); if(
				 * searchZone.getText()!= null){ int numberDP =
				 * Integer.parseInt(searchZone.getText()); int response=
				 * JOptionPane.showInternalConfirmDialog(all,
				 * "Voulez-vous retirer le point de livraison n°= "+numberDP,
				 * "Suppression", JOptionPane.OK_CANCEL_OPTION,
				 * JOptionPane.INFORMATION_MESSAGE); if(response ==
				 * JOptionPane.OK_OPTION ){
				 * 
				 * }
				 * 
				 * }
				 */

			}

			if (keyCode == KeyEvent.VK_M) {

			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

	}
}
