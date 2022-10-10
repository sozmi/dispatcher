package com.sozmi.dispetcher.model;

import java.util.ArrayList;

public class Server {
    public static ArrayList<Building> getBuildings(){
        ArrayList<Car> car = new ArrayList<>();
        car.add(new Car("Машинка 1",TypeCar.police,Status.Moving));
        car.add(new Car("Машинка 2",TypeCar.ambulance,Status.OnCall));
        car.add(new Car("Машинка 3",TypeCar.fireTrack,Status.Available));
        car.add(new Car("Машинка 4",TypeCar.fireTrack,Status.Unavailable));
        Building build1 = new Building("Здание одно", TypeBuilding.fire_station, car);
        Building build2 = new Building("Здание два", TypeBuilding.fire_station, car);
        Building build3 = new Building("Здание три", TypeBuilding.fire_station, car);
        Building build4 = new Building("Здание одно", TypeBuilding.police, car);
        Building build5 = new Building("Здание два", TypeBuilding.police, car);
        Building build6 = new Building("Здание три", TypeBuilding.police, car);
        Building build7 = new Building("Здание одно", TypeBuilding.hospital, car);
        Building build8 = new Building("Здание два", TypeBuilding.hospital, car);
        Building build9 = new Building("Здание три", TypeBuilding.hospital, car);

        ArrayList<Building>  lst= new ArrayList<>();
        lst.add(build1);
        lst.add(build2);
        lst.add(build3);
        lst.add(build4);
        lst.add(build5);
        lst.add(build6);
        lst.add(build7);
        lst.add(build8);
        lst.add(build9);
        lst.add(build1);
        lst.add(build2);
        lst.add(build3);
        lst.add(build4);
        lst.add(build5);
        lst.add(build6);
        lst.add(build7);
        lst.add(build8);
        lst.add(build9);
        return lst;
    }

    public static boolean isAuth(){
        return false;
    }
}

