/**
 * Package containing all the classes that deal with loading and parsing XML files into object of our model
 */
package com.hexagone.delivery.xml;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Class used when converting a Date stored in an XML file to a Date object
 */
public class DateAdapter extends XmlAdapter<String, Date> {

	/** The format of the date we need to parse */
	private DateFormat df = new SimpleDateFormat("H:m:s");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Date unmarshal(String v) throws ParseException {
		return df.parse(v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(Date v) throws Exception {
		return v.toString();
	}
}