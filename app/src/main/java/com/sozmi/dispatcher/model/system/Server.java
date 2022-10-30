package com.sozmi.dispatcher.model.system;

import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.StatusCar;
import com.sozmi.dispatcher.model.objects.StatusTask;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.objects.TypeBuilding;
import com.sozmi.dispatcher.model.objects.TypeCar;
import com.sozmi.dispatcher.model.objects.TypeIconTask;
import com.sozmi.dispatcher.model.objects.TypeGroupTask;
import com.sozmi.dispatcher.model.system.DataController;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Server {
    private static final ArrayList<Building> buildings =new ArrayList<>();
    private static final ArrayList<Task> tasks =new ArrayList<>();


    public static ArrayList<Building> getBuildings(){
        return buildings;
    }
    public static ArrayList<Task> getTasks(){
        addTask();
        return tasks;
    }

    public static void addTask(){
        tasks.add(new Task(new GeoPoint(56.1,34.1),"Пожар в гостинице", TypeGroupTask.personal, 500, TypeIconTask.epidemic, StatusTask.executed));
        tasks.add(new Task(new GeoPoint(56.01,34.01),"Пожар в доме", TypeGroupTask.group, 500, TypeIconTask.epidemic, StatusTask.wait));
        tasks.add(new Task(new GeoPoint(56.01,34.01),"Пожар в доме", TypeGroupTask.group, 500, TypeIconTask.epidemic, StatusTask.not_executed));

    }
    public static boolean isAuth(){
        return false;
    }

    public static boolean addBuild(String name, GeoPoint point, TypeBuilding typeBuilding){
        if(typeBuilding.toCost()>getMoney())
            return false;
        setMoney(getMoney()-typeBuilding.toCost());
        Building b =new Building(name,typeBuilding, point);
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
        Car car =new Car(typeCar.name(), typeCar, StatusCar.Available,building.getPoint());
        building.addCar(car);
        return true;
    }


}

