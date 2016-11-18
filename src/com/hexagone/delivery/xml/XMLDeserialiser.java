package com.hexagone.delivery.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XMLDeserialiser {
	public static Plan loadMap() throws XMLException{
		return (Plan) load();
	}
	
	public static DeliveryQuery loadDeliveryQuery() throws XMLException{
		return (DeliveryQuery) load();
	}
	
	private static Object load() throws XMLException{
		File xml = XMLLoader.getInstance().open();
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("demandeDeLivraisons")) {
           
        }
        else {
        	throw new XMLException("Document non conforme");
        }
	}
}
