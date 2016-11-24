package com.hexagone.delivery.launcher;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

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
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static void main(String[] args) throws XMLException {
		/*
		 * DeliveryQuery dq = XMLDeserialiser.loadDeliveryQuery(); Map map =
		 * XMLDeserialiser.loadMap(); Integer[] sols = {0, 6, 12}; Planning pl =
		 * new Planning(sols, map); System.out.println(pl.getRoads());
		 * pl.generateTxt();
		 */

		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}