package com.sozmi.dispatcher.model.objects;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;


/**
 * Класс, описывающий здания
 */
public class Building {
    private String name;
    private TypeBuilding type;
    private GeoPoint point;
    private ArrayList<Car> cars;
    /**
     * Конструктор здания без машин
     *
     * @param name название здания
     * @param type тип здания по его классификации
     * @param point координаты здания
     */
    public Building(String name, TypeBuilding type, GeoPoint point) {
        setName(name);
        cars = new ArrayList<>();
        setPoint(point);
        setType(type);
    }
    /**
     * Конструктор
     *
     * @param name название здания
     * @param type тип здания по его классификации
     * @param cars список машин, относящихся к зданию
     * @param point координаты здания
     */
    public Building(String name, TypeBuilding type, ArrayList<Car> cars, GeoPoint point) {
        setName(name);
        setCars(cars);
        setPoint(point);
        setType(type);
    }

    /**
     * Получение названия здания
     *
     * @return название здания
     */
    public String getName() {
        return name;
    }

    /**
     * Установка название здания
     *
     * @param name название здания
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получение типа здания
     *
     * @return тип здания
     */
    public TypeBuilding getType() {
        return type;
    }

    /**
     * Установка типа здания
     *
     * @param type тип здания
     */
    public void setType(TypeBuilding type) {
        this.type = type;
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

    /**
     * Получение id иконки здания
     *
     * @return id ресурса
     */
    public int getImage() {
        return type.toImageId();
    }

    /**
     * Получение стоимости здания
     * @return стоимость здания
     */
    public int getCost(){
        return  -1;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public void addCar(Car car){
        cars.add(car);
    }

    public TypeCar getTypeCar() {
        return getType().toCar();
    }
}
