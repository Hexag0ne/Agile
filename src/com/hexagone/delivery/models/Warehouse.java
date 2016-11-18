package com.hexagone.delivery.models;

public class Warehouse
{
    private String address;

    private String departureTime;

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getDepartureTime ()
    {
        return departureTime;
    }

    public void setDepartureTime (String departureTime)
    {
        this.departureTime = departureTime;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [address = "+address+", departureTime = "+departureTime+"]";
    }
}