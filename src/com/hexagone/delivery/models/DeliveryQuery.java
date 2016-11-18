package com.hexagone.delivery.models;

public class DeliveryQuery
{
    private DemandeDeLivraisons demandeDeLivraisons;

    public DemandeDeLivraisons getDemandeDeLivraisons ()
    {
        return demandeDeLivraisons;
    }

    public void setDemandeDeLivraisons (DemandeDeLivraisons demandeDeLivraisons)
    {
        this.demandeDeLivraisons = demandeDeLivraisons;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [demandeDeLivraisons = "+demandeDeLivraisons+"]";
    }
}