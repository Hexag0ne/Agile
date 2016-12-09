package com.hexagone.delivery.control;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.hexagone.delivery.models.Delivery;

public class ModifyPanel extends JPanel {
	
	private JTextField endScheduleTextField;
	private JTextField startScheduleTextField;
	private JTextField durationTextField;
	
	public ModifyPanel(Delivery d) {
		super();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width/5, screenSize.height/5);
		Dimension preferredSize = new Dimension(screenSize.width/5, screenSize.height/8);
		setPreferredSize(preferredSize);
		add(new JLabel("Durée de livraison: "));
		durationTextField = new JTextField(""+(d.getDuration()/60));
		add(durationTextField);
		add(Box.createVerticalStrut(15));
		add(new JLabel("Début de la plage de livraison: "));
		startScheduleTextField= new JTextField(d.getStartScheduleeString());
		add(startScheduleTextField);
		add(Box.createHorizontalStrut(15));
		add(new JLabel("Fin de la plage de livraison:"));
		endScheduleTextField = new JTextField(d.getEndScheduleString());
		add(endScheduleTextField);
		
	}
	
	public String getEndScheduleTextField() {
		return endScheduleTextField.getText();
	}

	public String getStartScheduleTextField() {
		return startScheduleTextField.getText();
	}

	public String getDurationTextField() {
		return durationTextField.getText();
	}

	
	

}
