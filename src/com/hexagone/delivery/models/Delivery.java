package com.hexagone.delivery.models;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.hexagone.delivery.xml.DateAdapter;
import com.hexagone.delivery.xml.IntersectionAdapter;

@XmlRootElement(name="livraison")
public class Delivery
{
    private Intersection intersection;

    private Date endSchedule;

    private int duration;

    private Date startSchedule;

    public Intersection getIntersection ()
    {
        return intersection;
    }

    @XmlJavaTypeAdapter(IntersectionAdapter.class)
    @XmlAttribute(name="adresse")
    public void setIntersection (Intersection intersection)
    {
        this.intersection = intersection;
    }

    public Date getEndSchedule ()
    {
        return endSchedule;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlAttribute(name="debutPlage")
    public void setEndSchedule (Date endSchedule)
    {
        this.endSchedule = endSchedule;
    }

    public int getDuration ()
    {
        return duration;
    }

    @XmlAttribute(name="duree")
    public void setDuration (int duration)
    {
        this.duration = duration;
    }

    public Date getStartSchedule ()
    {
        return startSchedule;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlAttribute(name="finPlage")
    public void setStartSchedule (Date startSchedule)
    {
        this.startSchedule = startSchedule;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [intersection = "+intersection+", endSchedule = "+endSchedule+", duration = "+duration+", startSchedule = "+startSchedule+"]";
    }
}