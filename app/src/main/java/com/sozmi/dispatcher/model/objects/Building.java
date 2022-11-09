package com.sozmi.dispatcher.model.objects;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;


/**
 * Класс, описывающий здания
 */
public class Building extends Object<TypeBuilding> {
    private ArrayList<Car> cars;

    /**
     * Конструктор здания без машин
     *
     * @param name  название здания
     * @param type  тип здания по его классификации
     * @param point координаты здания
     */
    public Building(int id, String name, TypeBuilding type, GeoPoint point) {
        super(id, name,point,type, type.toCost());
        cars = new ArrayList<>();
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

    public TypeCar getTypeCar() {
        return getType().toCar();
    }
}
