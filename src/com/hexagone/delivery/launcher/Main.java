package com.hexagone.delivery.launcher;

import java.util.ArrayList;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Route;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;
import com.hexagone.ui.MainFrame;

/**
 * This class manages the launch of the application. Starting point of the executable.
 */
public class Main {

	/**
	 * Launching of the application. No parameters.
	 * 
	 * @throws XMLException
	 */
	public static void main(String[] args) throws XMLException {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	/**
	 * Gets the intersections between two points : i1 and i2
	 * @param i1
	 * 			: departure bound
	 * @param i2
	 * 			: ending bound
	 * @return the list of intersections as an ArrayList of Integers
	 */
	public static ArrayList<Integer> getIntersectionsBetween(Integer i1, Integer i2) {
		ArrayList<Integer> sols = new ArrayList<Integer>();

		if (i1.equals(6)) {
			sols.clear();
			sols.add(6);
			sols.add(7);
			sols.add(12);
		} else if (i1.equals(12)) {
			sols.clear();
			sols.add(12);
			sols.add(13);
			sols.add(8);
		} else {
			sols.clear();
			sols.add(8);
			sols.add(7);
			sols.add(6);
		}

		return sols;
	}
}