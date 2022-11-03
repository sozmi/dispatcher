package com.sozmi.dispatcher.model.objects;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Класс, описывающий машину как объект
 */
public class Car extends Object<TypeCar> {
    private StatusCar status;
    private Route route;
    private Route reverse_root;
    /**
     * Конструктор
     *
     * @param name   пользовательское название машины
     * @param type   тип машины по её классификации
     * @param status статус машины
     * @param point координаты машины
     */
    public Car(int id, String name, TypeCar type, StatusCar status, GeoPoint point) {
        super(id, name, point, type, type.toCost());
        setStatus(status);
    }

    public void setRoute(Route route) {
        this.route = route;
    }
    private Queue<GeoPoint> reverse(Queue<GeoPoint> queue) {
        List<GeoPoint> collect = new ArrayList<>(queue);
        Collections.reverse(collect);
        return new LinkedList<>(collect);
    }
    public Route getRoute() {
        return route;
    }

    public Route getReverse_root() {
        return reverse_root;
    }

    public void setReverse_root() {
        this.reverse_root = new Route(route);
        this.reverse_root.setPoints(reverse(reverse_root.getPoints()));
    }

    /**
     * Получение статуса машины
     *
     * @return статус машины
     */
    public String getStatusToString() {
        return status.toString();
    }

    /**
     * Получение статуса машины
     *
     * @return статус машины
     */
    public StatusCar getStatus() {
        return status;
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

}
