package com.sozmi.dispatcher.model.navigation;

import org.osmdroid.util.GeoPoint;

import java.util.LinkedList;
import java.util.Queue;

public class Route {
    private Queue<GeoPoint> points;
    private final double time;
    private final double count_point;

    public Route(Queue<GeoPoint> points, double time, double count_point) {
        this.points = points;
        this.time = time;
        this.count_point =count_point;
    }
    public Route(Route route) {
        this.points = new LinkedList<>( route.getPoints());
        this.time = route.getTime();
        this.count_point = route.getCount_point();
    }
    public Queue<GeoPoint> getPoints() {
        return points;
    }
    public LinkedList<GeoPoint> getPointsList() {
        return new LinkedList<>(points);
    }
    public GeoPoint peekPoint() {
        return points.peek();
    }
    public GeoPoint pollPoint() {
        return points.poll();
    }
    public void setPoints(Queue<GeoPoint> points) {
        this.points = points;
    }

    public double getTime() {
        return time;
    }

    public double getCount_point() {
        return count_point;
    }
}
