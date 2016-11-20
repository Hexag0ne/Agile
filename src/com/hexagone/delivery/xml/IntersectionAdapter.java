package com.hexagone.delivery.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.hexagone.delivery.models.Intersection;

public class IntersectionAdapter extends XmlAdapter<String, Intersection> {

	public Intersection unmarshal(String v)
	{
		return new Intersection(Integer.parseInt(v));
	}

	@Override
	public String marshal(Intersection v) {
		return v.toString();
	}
}