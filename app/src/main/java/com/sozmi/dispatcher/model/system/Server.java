package com.sozmi.dispatcher.model.system;

import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.CarCheck;
import com.sozmi.dispatcher.model.objects.Requirement;
import com.sozmi.dispatcher.model.objects.StatusCar;
import com.sozmi.dispatcher.model.objects.StatusTask;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.objects.TypeBuilding;
import com.sozmi.dispatcher.model.objects.TypeCar;
import com.sozmi.dispatcher.model.objects.TypeGroupTask;
import com.sozmi.dispatcher.model.objects.TypeTask;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Server {
    private static final ArrayList<Building> buildings = new ArrayList<>();
    private static final ArrayList<Task> tasks = new ArrayList<>();
    private static final ArrayList<Car> inMovement = new ArrayList<>();
    private static final User user = new User(1, 150000);
    private static int carIndex = 0;

    public static ArrayList<Building> getBuildings() {
        return buildings;
    }

    public static ArrayList<Task> getTasks() {
        return tasks;
    }

    public static void addTask() {
        ArrayList<Requirement> requirements =new ArrayList<>();
        requirements.add(new Requirement(1,TypeCar.police));
        requirements.add(new Requirement(3,TypeCar.fireTrack));
        requirements.add(new Requirement(1,TypeCar.ambulance));
        tasks.add(new Task(0, new GeoPoint(56.838181, 35.797557), "Пожар в гостинице", TypeGroupTask.personal, 500, TypeTask.epidemic, StatusTask.not_executed,requirements));
        tasks.add(new Task(1, new GeoPoint(56.838191, 35.798557), "Пожар в доме", TypeGroupTask.group, 500, TypeTask.epidemic, StatusTask.not_executed, requirements));
        tasks.add(new Task(2, new GeoPoint(56.838181, 35.799557), "Пожар в доме", TypeGroupTask.premium, 500, TypeTask.epidemic, StatusTask.not_executed, requirements));
    }

    public static boolean isAuth() {
        return false;
    }

    public static boolean addBuild(String name, GeoPoint point, TypeBuilding typeBuilding) {
        if (user.isNoMoney(typeBuilding.toCost())) return false;
        user.minMoney(typeBuilding.toCost());
        Building b = new Building(buildings.size(), name, typeBuilding, point);
        buildings.add(b);
        return true;
    }

    public static boolean addCar(Building building, TypeCar typeCar) {
        if (user.isNoMoney(typeCar.toCost())) user.minMoney(typeCar.toCost());
        Car car = new Car(carIndex++, typeCar.name(), typeCar, StatusCar.Available, building.getPosition());
        building.addCar(car);
        return true;
    }


    public static Task getTask(int taskId) {
        if(taskId>tasks.size()-1 || tasks.size()==0)
            return null;
        return tasks.get(taskId);
    }

    public static ArrayList<CarCheck> getFreeCars() {
        ArrayList<CarCheck> cars = new ArrayList<>();
        for (Building build : buildings) {
            for (Car car : build.getCars()) {
                if (car.getStatus() == StatusCar.Moving || car.getStatus() == StatusCar.Available)
                    cars.add(new CarCheck(car));
            }

        }
        return cars;
    }

    public static User getUser() {
        return user;
    }

    public static void removeTask(int id) {
        tasks.remove(id);
    }

    public static void AddTestBuild() {
        new ArrayList<Car>();
        addBuild("БООООЛЛЬНИЦА", new GeoPoint(56.867, 35.945, 149.249), TypeBuilding.hospital);
        addCar(buildings.get(0),TypeCar.ambulance);
        addCar(buildings.get(0),TypeCar.ambulance);
        addCar(buildings.get(0),TypeCar.ambulance);
        addBuild("Пожарка", new GeoPoint(57.867, 35.945, 149.249), TypeBuilding.fire_station);
        addCar(buildings.get(1),TypeCar.fireTrack);
        addCar(buildings.get(1),TypeCar.fireTrack);
        addCar(buildings.get(1),TypeCar.fireTrack);
        addBuild("Пожарка", new GeoPoint(57.867, 36.945, 149.249), TypeBuilding.police);
        addCar(buildings.get(2),TypeCar.police);
        addTask();
    }

    public static ArrayList<Car> getInMovement() {
        return inMovement;
    }
    public static void addInMovement(Car car) {
        inMovement.add(car);
    }

    public static void removeInMovement(Car car){
        inMovement.remove(car);
    }
}

