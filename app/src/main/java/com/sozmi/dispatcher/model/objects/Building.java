package com.sozmi.dispatcher.model.objects;

import android.util.Log;

import com.sozmi.dispatcher.model.listeners.CarListener;
import com.sozmi.dispatcher.model.server.ServerData;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Класс, описывающий здания
 */
public class Building extends Object<TypeBuilding> implements Serializable {
    private final ArrayList<Car> cars = new ArrayList<>();

    /**
     * Конструктор здания без машин
     *
     * @param name  название здания
     * @param type  тип здания по его классификации
     * @param point координаты здания
     */
    public Building(int id, String name, TypeBuilding type, GeoPoint point) {
        super(id, name, point, type, type.toCost());
    }

    public Building(String data) {
        super();
        var d = data.split("\\|");
        if (d.length == 4) {
            setID(Integer.parseInt(d[0]));
            var position = d[1].split(",");
            setPosition(new GeoPoint(Double.parseDouble(position[0].replace("(", "")), Double.parseDouble(position[1].replace(")", ""))));
            setName(d[2]);
            setType(TypeBuilding.valueOf(d[3]));
            setCost(getType().toCost());
        } else {
            try {
                Log.d("SOCKET", "Get message: " + data);
                throw new Exception("Передали не здание");

            } catch (Exception e) {
                Log.d("SOCKET", e.getMessage());
                Log.d("SOCKET", "Get message: " + data);
                e.printStackTrace();
            }
        }
    }

    /**
     * Получение списка машин, относящихся к зданию
     *
     * @return список машин, относящихся к зданию
     */
    public ArrayList<Car> getCars() {
        return cars;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCar(String str_cars) {
        String[] str = str_cars.split(";");
        for (String sc : str){
            Car car = new Car(sc, this);
            car.addListener(new CarListener() {
                @Override
                public void onStatusChanged(Car car) {
                    if (StatusCar.OnCall == car.getStatus() || car.getStatus() == StatusCar.Available)
                        ServerData.removeInMovement(car);
                    else if (car.getStatus() == StatusCar.Moving || car.getStatus() == StatusCar.MovingOnCall)
                        ServerData.addInMovement(car);
                }

                @Override
                public void onPositionChanged(Car car) {

                }
            }, "ClassServer");
            addCar(car);
        }


    }

    public TypeCar getTypeCar() {
        return getType().toCar();
    }
}
