package com.hexagone.delivery.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;

/**
 * This class provides the various methods allowing the user to pick a map or a
 * delivery file on its file system and load it into the application as a part
 * of the model
 * 
 * @see com.hexagone.delivery.models
 */
public class XMLDeserialiser {
	public static Map loadMap() throws XMLException {
		File xml = XMLFileOpener.getInstance().open();
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = docBuilder.parse(xml);
			Element racine = document.getDocumentElement();
			return buildMap(racine);
		} catch (Exception e) {
			throw new XMLException("Could not parse Map xml file.");
		}
	}

	public static DeliveryQuery loadDeliveryQuery() throws XMLException {
		File xml = XMLFileOpener.getInstance().open();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(DeliveryQuery.class);
			javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (DeliveryQuery) jaxbUnmarshaller.unmarshal(xml);
		} catch (JAXBException e) {
			throw new XMLException("Could not parse Delivery Query xml file.");
		}
	}

	/**
	 * This method creates a Map from the root node of a properly formed xml
	 * document.
	 * 
	 * @param racine
	 *            the root node of the xml maps file
	 * @return Map object corresponding to the node given as parameter
	 * @throws XMLException
	 * @see Map
	 */
	private static Map buildMap(Element root) throws XMLException {
		Map map = new Map();
		NodeList intersections = root.getElementsByTagName("noeud");
		for (int i = 0; i < intersections.getLength(); i++) {
			map.addIntersection(createIntersection((Element) intersections.item(i)));
		}

		NodeList roads = root.getElementsByTagName("troncon");
		for (int i = 0; i < roads.getLength(); i++) {
			map.addRoad(createRoad((Element) roads.item(i)));
		}

		return map;
	}

	/**
	 * This method creates an Intersection instance, given the appropriate xml
	 * node.
	 * 
	 * @param elementXML
	 *            the node corresponding to an intersection
	 * @return an intersection object corresponding to the information given in
	 *         the node provided as parameter
	 * @see Intersection
	 */
	private static Intersection createIntersection(Element elementXML) {
		int id = Integer.parseInt(elementXML.getAttribute("id"));
		int x = Integer.parseInt(elementXML.getAttribute("x"));
		int y = Integer.parseInt(elementXML.getAttribute("y"));
		Intersection intersection = new Intersection(id, x, y);
		return intersection;
	}

	/**
	 * This method creates a Road instance, given the appropriate xml node.
	 * 
	 * @param elementXML
	 *            the node corresponding to a road
	 * @return a road corresponding to the xml node provided as parameter
	 */
	private static Road createRoad(Element elementXML) {
		Integer origin = Integer.parseInt(elementXML.getAttribute("origine"));
		Integer destination = Integer.parseInt(elementXML.getAttribute("destination"));
		int length = Integer.parseInt(elementXML.getAttribute("longueur"));
		int speed = Integer.parseInt(elementXML.getAttribute("vitesse"));
		String name = elementXML.getAttribute("nomRue");
		return new Road(origin, destination, length, speed, name);
	}
}
