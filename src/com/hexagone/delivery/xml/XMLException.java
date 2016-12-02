/**
 * Package containing all the classes that deal with loading and parsing XML files into object of our model
 */
package com.hexagone.delivery.xml;

/**
 * This class is a custom Exception. 
 * It is meant to be used when a problem occurs when loading or parsing an XML file
 */
public class XMLException extends Exception {

	/**
	 * Constructor for the XMLException
	 * @param message the message corresponding to the error one wants to create
	 */
	public XMLException(String message) {
		super(message);
	}

}