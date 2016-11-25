package com.hexagone.delivery.launcher;

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
		/*DeliveryQuery dq = XMLDeserialiser.loadDeliveryQuery();
		Map map = XMLDeserialiser.loadMap();
		Integer[] sols = {6, 12, 8, 6};
		Planning pl = new Planning(map, dq, sols);
		pl.generateTxt("export/planning.txt");*/

		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}