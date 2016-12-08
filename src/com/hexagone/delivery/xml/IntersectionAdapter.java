/**
 * Package containing all the classes that deal with loading and parsing XML files into object of our model
 */
package com.hexagone.delivery.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.hexagone.delivery.models.Intersection;

/**
 * Class used when parsing the DeliveryQueries into our model
 */
public class IntersectionAdapter extends XmlAdapter<String, Intersection> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Intersection unmarshal(String v) {
		return new Intersection(Integer.parseInt(v));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(Intersection v) {
		return v.toString();
	}
}