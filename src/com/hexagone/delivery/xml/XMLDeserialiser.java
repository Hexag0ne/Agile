package com.hexagone.delivery.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;

public class XMLDeserialiser {
	public static Map loadMap() throws XMLException, ParserConfigurationException, SAXException, IOException{
		return (Map) load();
	}
	
	public static DeliveryQuery loadDeliveryQuery() throws XMLException, JAXBException {
		File xml = XMLFileOpener.getInstance().open();
		JAXBContext jaxbContext = JAXBContext.newInstance(DeliveryQuery.class);
		javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		DeliveryQuery dq = (DeliveryQuery) jaxbUnmarshaller.unmarshal(xml);
		return dq;
	}
	
	private static Object load() throws XMLException, ParserConfigurationException, SAXException, IOException {
		File xml = XMLFileOpener.getInstance().open();
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("reseau")) {
        	return buildMap(racine);
        }
        else {
        	throw new XMLException("Document not valid.");
        }
	}
	
	private static Map buildMap(Element racine) throws XMLException{
		Map map = new Map();
		NodeList intersections = racine.getElementsByTagName("noeud");
		for(int i = 0; i<intersections.getLength();i++){
			map.addIntersection(createIntersection((Element) intersections.item(i)));
		}
		
		NodeList roads = racine.getElementsByTagName("troncon");
		for(int i = 0; i<roads.getLength();i++){
			map.addIntersection(createIntersection((Element) intersections.item(i)));
		}
		
		return map;
	}
	
	private static Intersection createIntersection(Element elementXML){
		
		int id = Integer.parseInt(elementXML.getAttribute("id"));
		int x = Integer.parseInt(elementXML.getAttribute("x"));
		int y = Integer.parseInt(elementXML.getAttribute("y"));
		Intersection intersection = new Intersection(id, x, y);
		
		return intersection;
		
	}
}
