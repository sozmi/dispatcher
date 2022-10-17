package com.sozmi.dispatcher.model;
import java.util.ArrayList;


/**
 * Класс, описывающий здания
 */
public class Building {
    private String name;
    private TypeBuilding type;
    private ArrayList<Car> cars;

    /**
     * Конструктор
     * @param name название здания
     * @param type тип здания по его классификации
     * @param cars список машин, относящихся к зданию
     */
    public Building(String name, TypeBuilding type, ArrayList<Car> cars) {
        setName(name);
        this.cars = cars;
        setType(type);
    }

    /**
     * Получение названия здания
     * @return название здания
     */
    public String getName() {
        return name;
    }

    /**
     * Установка название здания
     * @param name название здания
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получение типа здания
     * @return тип здания
     */
    public TypeBuilding getType() {
        return type;
    }

    /**
     * Установка типа здания
     * @param type тип здания
     */
    public void setType(TypeBuilding type) {
        this.type = type;
    }

    /**
     * Получение списка машин, относящихся к зданию
     * @return список машин, относящихся к зданию
     */
    public ArrayList<Car> getCars() {
        return cars;
    }

    /**
     * Установка списка машин, относящихся к зданию
     * @param cars список машин, относящихся к зданию
     */
    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    /**
     * Получение id иконки здания
     * @return id ресурса
     */
    public int getImage() {
        return type.toImageId();
    }
}
