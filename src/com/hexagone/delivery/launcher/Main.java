package com.hexagone.delivery.launcher;

import com.hexagone.delivery.control.Controller;

/**
 * Classe qui gère le lancement de l'application. Point d'entrée de l'éxécutable
 */
public class Main {

	/**
	 * Lancement de l'application. Pas de paramètres particuliers.
	 * 
	 * @throws XMLException
	 */
	public static void main(String[] args) {
		new Controller();
	}	
}