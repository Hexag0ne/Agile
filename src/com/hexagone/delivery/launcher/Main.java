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
 * Classe qui gère le lancement de l'application. Point d'entrée de l'éxécutable
 */
public class Main {

	/**
	 * Lancement de l'application. Pas de paramètres particuliers.
	 * 
	 * @throws XMLException
	 */
	public static void main(String[] args) throws XMLException {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

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