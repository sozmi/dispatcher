package com.sozmi.dispatcher.model.objects;

import org.osmdroid.util.GeoPoint;

public class Object<T extends BaseType> {
    protected String name;
    protected GeoPoint point;
    protected T type;

    protected Object(String name,GeoPoint point,T type) {
        setName(name);
        setPoint(point);
        setType(type);
    }

    /**
     * Установка название объекта
     *
     * @param name название объекта
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получение названия объекта
     *
     * @return название объекта
     */
    public String getName() {
        return name;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    /**
     * Установка типа объекта
     *
     * @param type тип объекта
     */
    public void setType(T type) {
        this.type = type;
    }

    /**
     * Получение типа объекта
     *
     * @return тип объекта
     */
    public T getType() {
        return type;
    }

    /**
     * Получение id иконки объекта для отображения на карте
     *
     * @return id ресурса
     */
    public int getImage() {
        return type.toImageId();
    }
}