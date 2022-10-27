package com.sozmi.dispatcher.model.objects;

import org.osmdroid.util.GeoPoint;

/**
 * Класс, описывающий машину как объект
 */
public class Car extends Object<TypeCar> {
    private StatusCar status;

    /**
     * Конструктор
     *
     * @param name   пользовательское название машины
     * @param type   тип машины по её классификации
     * @param status статус машины
     * @param point координаты машины
     */
    public Car(String name, TypeCar type, StatusCar status, GeoPoint point) {
        super(name, point, type);
        setStatus(status);
    }

    /**
     * Получение статуса машины
     *
     * @return статус машины
     */
    public String getStatus() {
        return status.toString();
    }

    /**
     * Получение цвета статуса машины
     *
     * @return цвет статуса машины
     */
    public int getColor() {
        return status.toColor();
    }


    /**
     * Установка статуса машины
     *
     * @param status статус машины
     */
    public void setStatus(StatusCar status) {
        this.status = status;
    }

    /**
     * Получение стоимости машины
     * @return стоимость машны
     */
    public int getCost(){
        return  -1;
    }
}
