package com.sozmi.dispatcher.model.objects;

import org.osmdroid.util.GeoPoint;

public class Task {
    private GeoPoint point;
    private String name;
    private TypeTask type;
    private int baseCost;

    public Task(GeoPoint point, String name, TypeTask type, int baseCost) {
        setPoint(point);
        setName(name);
        setType(type);
        setBaseCost(baseCost);
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeTask getType() {
        return type;
    }

    public void setType(TypeTask type) {
        this.type = type;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(int baseCost) {
        this.baseCost = baseCost;
    }
}
