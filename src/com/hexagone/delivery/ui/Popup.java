package com.hexagone.delivery.ui;

import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class centralizes popup needs for the application
 */
public class Popup {

	/**
	 * Opens a pop-up dialog with the message given as parameter
	 * 
	 * @param message
	 *            the message to be displayed
	 */
	public static void showInformation(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	public static void showInformation(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showInformationWithScroll(String message, String title) {
		JTextArea textArea = new JTextArea(message);
		JScrollPane scrollPane = new JScrollPane(textArea);  
		textArea.setLineWrap(true);  
		textArea.setWrapStyleWord(true); 
		scrollPane.setPreferredSize( new Dimension( 600, 400 ) );
		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showError(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
