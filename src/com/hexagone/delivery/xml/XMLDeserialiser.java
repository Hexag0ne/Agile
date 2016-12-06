/**
 * Package containing all the classes that deal with loading and parsing XML files into object of our model
 */
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
	
	/**
	 * This method allows the user to select an XML file on the file system and convert it to a Map object the 
	 * application is going to use afterwards. 
	 * Note that if the file selected by the user isn't that of a Map, this method will yield an XMLException.
	 * @return a properly formed Map object
	 * @throws XMLException in case of a parsing problem or if the user cancels the file selection
	 * @see com.hexagone.delivery.models.Map
	 */
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

	/**
	 * This method allows the user to select a XML file on the file system and convert it to a DeliveryQuery that will
	 * then be used in the application.
	 * In case the user cancels the file selection or selects an improperly formated file, this method will throw an 
	 * XMLException.
	 * @return a properly formated DeliveryQuery
	 * @throws XMLException
	 * @see DeliveryQuery
	 */
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
		
		if (map.getAllIntersectionIdentifiers().size() < 2 || map.getRoads().size() < 2)
		{
			throw new XMLException("Not a valid map !");
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
