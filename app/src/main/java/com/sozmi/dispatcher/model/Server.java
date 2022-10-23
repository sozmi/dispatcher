package com.sozmi.dispatcher.model;

import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.Status;
import com.sozmi.dispatcher.model.objects.TypeBuilding;
import com.sozmi.dispatcher.model.objects.TypeCar;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Server {
    private static final ArrayList<Building> buildings =new ArrayList<>();

    public static ArrayList<Building> getBuildings(){
        return buildings;
    }

    public static boolean isAuth(){
        return false;
    }

    public static boolean addBuild(String name, GeoPoint point, TypeBuilding typeBuilding){
        if(typeBuilding.toCost()>getMoney())
            return false;
        setMoney(getMoney()-typeBuilding.toCost());
        Building b =new Building(name,typeBuilding, new ArrayList<>(),point);
        buildings.add(b);
        return true;
    }
    private static int money=150000;
    public static int getMoney(){
        return money;
    }
    public static void setMoney(int m){
        money=m;
        DataController.setData(money+" руб.");
    }

    public static boolean addCar(Building building, TypeCar typeCar){
        if(typeCar.toCost()>getMoney()){return false;}
        setMoney(getMoney()-typeCar.toCost());
        Car car =new Car(typeCar.name(), typeCar, Status.Available);
        building.addCar(car);
        return true;
    }
}

