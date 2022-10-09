package com.sozmi.dispetcher.model;

/**
 * Класс, описывающий машину как объект
 */
public class Car {
    /**Пользовательское название машины*/
    private String name;
    /**Тип машины по её классификации*/
    private TypeCar type;
    /**Статус машины*/
    private Status status;
    /**Конструктор
     * @param name пользовательское название машины
     * @param type тип машины по её классификации
     * @param status статус машины*/
    public Car(String name, TypeCar type, Status status) {
        setName(name);
        setType(type);
        setStatus(status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeCar getType() {
        return type;
    }
    public void setType(TypeCar type) {
        this.type= type;
    }

    public String getStatus() {
       return status.toString();
    }
    public int getColor() {
        return status.toColor();
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public int getImage() {
        return type.toImageId();
    }
}
