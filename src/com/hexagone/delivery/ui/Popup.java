package com.hexagone.delivery.ui;

import javax.swing.JOptionPane;

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

	public static void showError(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
