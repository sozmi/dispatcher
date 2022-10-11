package com.sozmi.dispetcher.model;
import java.util.ArrayList;


/**
 * Класс, описывающий здания
 */
public class Building {
    private final String name;
    private final TypeBuilding type;
    private final ArrayList<Car> cars;

    /**
     * @param name название здания
     * @param type тип здания по его классификации
     * @param cars список машин, относящихся к зданию
     */
    public Building(String name, TypeBuilding type, ArrayList<Car> cars) {
        this.name=name;
        this.cars = cars;
        this.type = type;
    }

    /**
     * Получение названия здания
     * @return название здания
     */
    public String getName() {
        return name;
    }
    /**
     * Получение типа здания
     * @return тип здания
     */
    public TypeBuilding getType() {
        return type;
    }

    /**
     * Получение списка машин, относящихся к зданию
     * @return список машин, относящихся к зданию
     */
    public ArrayList<Car> getCars() {
        return cars;
    }
    /**
     * Получение id иконки здания
     * @return id ресурса
     */
    public int getImage() {
        return type.toImageId();
    }
}
