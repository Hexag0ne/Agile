package com.hexagone.delivery.models;

public class Delivery
{
    private String address;

    private String duration;

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [address = "+address+", duration = "+duration+"]";
    }
}
		