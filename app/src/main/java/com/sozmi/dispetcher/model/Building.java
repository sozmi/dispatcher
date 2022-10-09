package com.sozmi.dispetcher.model;
import java.util.ArrayList;

public class Building {
    private String name;
    private TypeBuilding type;
    private ArrayList<Car> car;

    public Building(String name, TypeBuilding type, ArrayList<Car> cars) {
        setName(name);
        setType(type);
        setCar(cars);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeBuilding getType() {
        return type;
    }

    public void setType(TypeBuilding type) {
        this.type = type;
    }

    public ArrayList<Car> getCar() {
        return car;
    }

    public void setCar(ArrayList<Car> car) {
        this.car = car;
    }

    public int getImage() {
        return type.toImageId();
    }
}
