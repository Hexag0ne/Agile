package com.hexagone.delivery.control;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import com.hexagone.delivery.algo.DeliveryComputer;
import com.hexagone.delivery.models.Delivery;
import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Intersection;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.models.Road;
import com.hexagone.delivery.models.RouteHelper;
import com.hexagone.delivery.models.Warehouse;
import com.hexagone.delivery.ui.Popup;
import com.hexagone.delivery.xml.NoFileChosenException;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;

/**
 * This class allows us to draw the map and the points of the delivery on top of
 * it when the state is COMPUTE_STATE
 */
public class ComputeState implements ControllerActions {

    /**
     * Opens a FileChooser that lets the user pick an XML file on the file
     * system.
     */
    @Override
    public Map loadMap() {
        try {
            return XMLDeserialiser.loadMap();
        } catch (XMLException e) {
            Popup.showInformation("Le fichier choisi n'est pas un plan valide.");
            return null;
        } catch (NoFileChosenException e) {
            return null;
        }
    }

    /**
     * This method allows to load a delivery query from a XML file
     */
    @Override
    public DeliveryQuery loadDeliveryQuery() {
        try {
            return XMLDeserialiser.loadDeliveryQuery();
        } catch (XMLException e) {
            Popup.showInformation("Le fichier choisi n'est pas une livraison valide.");
            return null;
        } catch (NoFileChosenException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.hexagone.delivery.control.ControllerActions#computeDelivery(com.hexagone.delivery.models.Map, com.hexagone.delivery.models.DeliveryQuery)
     */
    @Override
    public RouteHelper computeDelivery(Map map, DeliveryQuery delivery) {
        DeliveryComputer computer = new DeliveryComputer(map, delivery);
        computer.compute();

        if (!computer.checkNotEmptySolution()){
        	if (computer.checkTimeout()){
        		Popup.showInformation("Aucune solution n'a été trouvée \ndans le temps imparti", "Temps de calcul écoulé");
        		return null;
        	} else {
        		Popup.showInformation("Il n'existe aucune tournée satisfaisante.", "Tournée impossible");
        		return null;
        	}
        } else if (computer.checkTimeout()) {
        	Popup.showInformation("La tournée calculée n'est peut-être pas optimale.", "Temps de calcul écoulé");
        }
        computer.getDeliveryPoints();
        return new RouteHelper(map, delivery, computer);
    }

    /*
     * (non-Javadoc)
     * @see com.hexagone.delivery.control.ControllerActions#generatePlanning(com.hexagone.delivery.models.RouteHelper)
     */
    @Override
    public void generatePlanning(RouteHelper routeHelper) {
        JOptionPane.showMessageDialog(null, "Veuillez calculez la tournée.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    /*
     * (non-Javadoc)
     * @see com.hexagone.delivery.control.ControllerActions#DrawMap(java.awt.Graphics, float, com.hexagone.delivery.models.Map, com.hexagone.delivery.models.DeliveryQuery, com.hexagone.delivery.models.RouteHelper)
     */
    @Override
    public void DrawMap(Graphics g, float scale, Map map, DeliveryQuery deliveryQuery, RouteHelper routeHelper) {

        // Painting the map
        // Painting the roads first
        HashMap<Integer,Intersection> intersections = map.getIntersections();
        Set<Integer> roads = new HashSet<Integer>();
        roads = (map.getRoads()).keySet();

        for (int j : roads) {
            ArrayList<Road> roadsFromI = new ArrayList<Road>();
            roadsFromI = map.getRoads().get(j);
            for (Road r : roadsFromI) {
                g.setColor(Color.BLACK);
                Graphics2D g2 = (Graphics2D) g;
                Point destination = intersections.get(r.getOrigin()).getCoordinates();
                Point origine = intersections.get(r.getDestination()).getCoordinates();
                Line2D lin = new Line2D.Float(((origine.x) / scale) + 5, ((origine.y) / scale) + 5,
                        ((destination.x) / scale) + 5, ((destination.y) / scale) + 5);
                // g2.setStroke(new BasicStroke(2));
                g2.draw(lin);
            }
        }
        // Painting the of the map
        for (Intersection i : intersections.values()) {
            Point p = new Point();
            p = i.getCoordinates();
            g.setColor(Color.BLUE);
            g.fillOval((int) (((p.x)) / scale), (int) (((p.y)) / scale), 10, 10);
        }

        // Drawing the deliveryQuery
        Warehouse warehouse = deliveryQuery.getWarehouse();
        Delivery[] deliveries = deliveryQuery.getDeliveries();

        Intersection intersectionWarehouse = warehouse.getIntersection();
        Point pointWarehouse = intersections.get(intersectionWarehouse.getId()).getCoordinates();
        // Draw Warehouse
        g.setColor(Color.RED);
        g.fillOval((int) (((pointWarehouse.x)) / scale) - 2, (int) (((pointWarehouse.y)) / scale) - 2, 14, 14);
        g.drawString("Entrepôt", (int) (((pointWarehouse.x)) / scale + 5), (int) (((pointWarehouse.y)) / scale));

        // Draw Delivery points
        Point pointDelivery;
        g.setColor(new Color(20, 200, 20));
        for (Delivery d : deliveries) {
            pointDelivery = intersections.get(d.getIntersection().getId()).getCoordinates();
            g.fillOval((int) (((pointDelivery.x)) / scale) - 1, (int) (((pointDelivery.y)) / scale) - 1, 12, 12);
        }
    }

}
