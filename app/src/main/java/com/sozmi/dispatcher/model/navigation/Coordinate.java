package com.sozmi.dispatcher.model.navigation;

import org.osmdroid.util.GeoPoint;

import java.util.Random;

public class Coordinate {
    public static GeoPoint getLocationInLatLngRad(double radiusInMeters, GeoPoint currentLocation) {
        double x0 = currentLocation.getLongitude();
        double y0 = currentLocation.getLatitude();

        Random random = new Random();

        // Convert radius from meters to degrees.
        double radiusInDegrees = radiusInMeters / 111320f;

        // Get a random distance and a random angle.
        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        // Get the x and y delta values.
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Compensate the x value.
        double new_x = x / Math.cos(Math.toRadians(y0));

        double foundLatitude;
        double foundLongitude;

        foundLatitude = y0 + y;
        foundLongitude = x0 + new_x;

        GeoPoint copy = new GeoPoint(currentLocation);
        copy.setLatitude(foundLatitude);
        copy.setLongitude(foundLongitude);
        return copy;
    }
}
