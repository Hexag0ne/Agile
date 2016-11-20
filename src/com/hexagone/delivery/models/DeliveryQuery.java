package com.hexagone.delivery.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="demandeDeLivraisons")
public class DeliveryQuery
{
    private Warehouse warehouse;

    private Delivery[] delivery;

    public Warehouse getWarehouse ()
    {
        return warehouse;
    }

    @XmlElement(name="entrepot")
    public void setWarehouse (Warehouse warehouse)
    {
        this.warehouse = warehouse;
    }
    
    public Delivery[] getDelivery ()
    {
        return delivery;
    }

    @XmlElement(name="livraison")
    public void setDelivery (Delivery[] delivery)
    {
        this.delivery = delivery;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [warehouse = "+warehouse+", delivery = "+delivery+"]";
    }
}