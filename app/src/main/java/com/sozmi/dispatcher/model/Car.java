package com.sozmi.dispatcher.model;

/**
 * Класс, описывающий машину как объект
 */
public class Car {
    private String name;
    private TypeCar type;
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

    /**
     * Получение пользовательского названия машины
     * @return Пользовательское название машины
     */
    public String getName() {
        return name;
    }


    /**
     * Установка пользовательского названия машины
     * @param name название машины
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получение типа машины
     * @return тип машины
     */
    public TypeCar getType() {
        return type;
    }

    /**
     * Установка типа машины
     * @param type тип машины
     */
    public void setType(TypeCar type) {
        this.type = type;
    }


    /**
     * Получение статуса машины
     * @return статус машины
     */
    public String getStatus() {
       return status.toString();
    }

    /**
     * Получение цвета статуса машины
     * @return цвет статуса машины
     */
    public int getColor() {
        return status.toColor();
    }


    /**
     * Установка статуса машины
     * @param status статус машины
     */
    public void setStatus(Status status) {
        this.status = status;
    }


    /**
     * Получение иконки машины
     * @return id ресурса
     */
    public int getImage() {
        return type.toImageId();
    }
}
