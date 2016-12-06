package com.hexagone.delivery.launcher;

import com.hexagone.delivery.control.Controller;

/**
 * This class manages the launch of the application. Starting point of the
 * executable.
 */
public class Main {

	/**
	 * Launching of the application. No parameters.
	 */

	public static void main(String[] args) {
		new Controller();
	}
}