package com.hexagone.delivery.xml;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {
	private DateFormat df = new SimpleDateFormat("H:m:s");

	public Date unmarshal(String v) throws ParseException {
		return df.parse(v);
	}

	@Override
	public String marshal(Date v) throws Exception {
		return v.toString();
	}
}