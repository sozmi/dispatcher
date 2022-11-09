package com.sozmi.dispatcher.model.navigation;

import org.osmdroid.util.GeoPoint;

public class HaversineAlgorithm {

    private static final double _eQuatorialEarthRadius = 6378.1370D;
    private static final double _d2r = (Math.PI / 180D);

    public static int HaversineInM(GeoPoint p1, GeoPoint p2) {
        return (int) (1000D * HaversineInKM(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude()));
    }
    public static int HaversineInM(double lat1, double long1, double lat2, double long2) {
        return (int) (1000D * HaversineInKM(lat1, long1, lat2, long2));
    }

    public static double HaversineInKM(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));

        return _eQuatorialEarthRadius * c;
    }

}
