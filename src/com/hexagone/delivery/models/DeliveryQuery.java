package com.hexagone.delivery.models;

public class DeliveryQuery
{
    private Warehouse warehouse;

    private Delivery[] delivery;

    public Warehouse getWarehouse ()
    {
        return warehouse;
    }

    public void setWarehouse (Warehouse warehouse)
    {
        this.warehouse = warehouse;
    }

    public Delivery[] getDelivery ()
    {
        return delivery;
    }

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