package com.sozmi.dispatcher.model.objects;

import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;


/**
 * Класс, описывающий здания
 */
public class Building extends Object<TypeBuilding> {
    private ArrayList<Car> cars = new ArrayList<>();

    /**
     * Конструктор здания без машин
     *
     * @param name  название здания
     * @param type  тип здания по его классификации
     * @param point координаты здания
     */
    public Building(int id, String name, TypeBuilding type, GeoPoint point) {
        super(id, name,point,type, type.toCost());
    }

    public Building(String data){
        super();
        var d = data.split("\\|");
        if (d.length ==4) {
            setID(Integer.parseInt(d[0]));
            var position =d[1].split(",");
            setPosition(new GeoPoint(Double.parseDouble(position[0].replace("(","")),Double.parseDouble(position[1].replace(")",""))));
            setName(d[2]);
            setType(TypeBuilding.valueOf(d[3]));
            setCost(getType().toCost());
        } else {
            try {
                Log.d("SOCKET", "Get message: "+data);
                throw new Exception("Передали не здание");

            } catch (Exception e) {
                Log.d("SOCKET", e.getMessage());
                Log.d("SOCKET", "Get message: "+data);
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

    /**
     * Установка списка машин, относящихся к зданию
     *
     * @param cars список машин, относящихся к зданию
     */
    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    public void addCar(Car car) {
        cars.add(car);
    }
    public void addCar(String str_cars) {
        String[] str = str_cars.split(";");
        for (String sc : str)
            addCar(new Car(sc));

    }
    public TypeCar getTypeCar() {
        return getType().toCar();
    }
}
