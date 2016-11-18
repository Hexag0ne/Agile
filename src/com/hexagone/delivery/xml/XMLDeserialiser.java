package com.hexagone.delivery.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;

public class XMLDeserialiser {
	public static Map loadMap() throws XMLException, ParserConfigurationException, SAXException, IOException{
		return (Map) load();
	}
	
	public static DeliveryQuery loadDeliveryQuery() throws XMLException, ParserConfigurationException, SAXException, IOException{
		return (DeliveryQuery) load();
	}
	
	private static Object load() throws XMLException, ParserConfigurationException, SAXException, IOException{
		File xml = XMLFileOpener.getInstance().open();
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("demandeDeLivraisons")) {
           return buildDeliveryQuery(racine);
        }
        else if (racine.getNodeName().equals("reseau")) {
        	return buildMap(racine);
        }
        else {
        	throw new XMLException("Document non conforme");
        }
	}
	
	private static DeliveryQuery buildDeliveryQuery(Element elem) throws XMLException {
		return new DeliveryQuery();
	}
	
	private static Map buildMap(Element elem) throws XMLException{
		return new Map();
	}
}
